package pe.idat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import pe.idat.dto.CompraDTO;
import pe.idat.dto.DetalleDTO;
import pe.idat.entity.Compra;
import pe.idat.entity.DetalleCompra;
import pe.idat.entity.MovimientoAlmacen;
import pe.idat.entity.Producto;
import pe.idat.entity.Proveedor;
import pe.idat.entity.Usuario;
import pe.idat.repository.CompraRepository;
import pe.idat.repository.MovimientoAlmacenRepository;
import pe.idat.repository.ProductoRepository;
import pe.idat.repository.ProveedorRepository;
import pe.idat.repository.UsuarioRepository;

import java.time.LocalDateTime;

@Service
public class CompraServiceImpl implements CompraService {

    @Autowired
    private CompraRepository compraRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProveedorRepository proveedorRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private MovimientoAlmacenRepository movimientoRepository;

    @Override
    @Transactional
    public Compra registrarCompra(CompraDTO dto, String emailUsuario) {
        // ESTE MÉTODO YA NO SE DEBERÍA USAR DIRECTAMENTE SI TODO PASA POR COTIZACIÓN
        // PERO LO DEJAMOS POR COMPATIBILIDAD
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setMontoTotal(dto.getTotal().doubleValue());
        
        compra.setEstado("PENDIENTE"); // Nace pendiente de aprobación
        compra.setTipoDocumento("ORDEN_COMPRA"); 
        compra.setEstadoLogistico("PENDIENTE");

        for (DetalleDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioCompra(item.getPrecio().doubleValue());
            
            compra.agregarDetalle(detalle);
        }

        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        // 1. VALIDACIÓN DE APROBACIÓN
        if (!"APROBADA".equals(compra.getEstado())) {
            throw new RuntimeException("¡ALTO! No puedes recibir mercadería de una orden no aprobada o rechazada.");
        }

        // 2. VALIDACIÓN LOGÍSTICA
        if ("RECIBIDO".equals(compra.getEstadoLogistico())) {
            throw new RuntimeException("Esta compra ya fue recepcionada anteriormente.");
        }

        Usuario almacenero = usuarioRepository.findByEmail(emailAlmacenero)
                .orElseThrow(() -> new RuntimeException("Usuario almacen no encontrado"));

        // 3. ACTUALIZAR STOCK
        for (DetalleCompra detalle : compra.getDetalle()) {
            Producto p = detalle.getProducto();
            p.setStockActual(p.getStockActual() + detalle.getCantidad());
            productoRepository.save(p);
        }

        // 4. REGISTRAR EN KARDEX (MOVIMIENTOS)
        MovimientoAlmacen mov = new MovimientoAlmacen();
        mov.setFechaMovimiento(LocalDateTime.now());
        mov.setTipoMovimiento("ENTRADA"); 
        mov.setMotivo("Recepción de Compra #" + compra.getCompraId() + " - Prov: " + compra.getProveedor().getRazonSocial());
        mov.setUsuario(almacenero);
        mov.setCompraReferencia(compra);
        
        movimientoRepository.save(mov);

        compra.setEstadoLogistico("RECIBIDO");
        compraRepository.save(compra);
    }
    
    @Override
    @Transactional
    public void registrarFacturaFisica(Integer compraId, String numeroFactura) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        if (!"APROBADA".equals(compra.getEstado())) {
            throw new RuntimeException("No se puede facturar una orden no aprobada.");
        }
        
        compra.setNumeroDocumentoFisico(numeroFactura);
        compra.setTipoDocumento("FACTURA"); 
        
        compraRepository.save(compra);
    }
    
    @Override
    public List<Compra> listarCotizaciones() {
        return compraRepository.findByTipoDocumento("COTIZACION");
    }

    @Override
    @Transactional
    public void registrarCotizacion(CompraDTO dto, String emailUsuario) {
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra cotizacion = new Compra();
        cotizacion.setProveedor(proveedor);
        cotizacion.setUsuario(usuario);
        cotizacion.setMontoTotal(dto.getTotal().doubleValue());
        
        cotizacion.setEstado("BORRADOR");
        cotizacion.setTipoDocumento("COTIZACION"); 
        cotizacion.setEstadoLogistico("NA");       

        for (DetalleDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioCompra(item.getPrecio().doubleValue());
            
            cotizacion.agregarDetalle(detalle);
        }
        compraRepository.save(cotizacion);
    }

    @Override
    @Transactional
    public void convertirCotizacionAOrden(Integer compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Cotización no encontrada"));
        
        if (!"COTIZACION".equals(compra.getTipoDocumento())) {
            throw new RuntimeException("Este documento no es una cotización.");
        }

        // TRANSFORMACIÓN: De Cotización a Orden
        compra.setTipoDocumento("ORDEN_COMPRA");
        
        // AQUÍ ESTÁ EL CAMBIO IMPORTANTE:
        // Antes pasaba a APROBADA, ahora pasa a PENDIENTE de aprobación del Admin.
        compra.setEstado("PENDIENTE"); 
        compra.setEstadoLogistico("PENDIENTE"); 
        
        compraRepository.save(compra);
    }

    // --- NUEVA LÓGICA DE APROBACIÓN ---

    @Override
    @Transactional
    public void aprobarOrdenCompra(Integer compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        if (!"ORDEN_COMPRA".equals(compra.getTipoDocumento())) {
            throw new RuntimeException("Solo se pueden aprobar Órdenes de Compra.");
        }
        
        compra.setEstado("APROBADA");
        compraRepository.save(compra);
    }

    @Override
    @Transactional
    public void rechazarOrdenCompra(Integer compraId) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        
        // No se elimina, solo cambia de estado
        compra.setEstado("RECHAZADA");
        // Opcional: Podrías poner estado logistico en CANCELADO si quisieras ser explícito
        compraRepository.save(compra);
    }
}