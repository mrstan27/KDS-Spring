package pe.idat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List; // Asegúrate de tener este import

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
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setMontoTotal(dto.getTotal().doubleValue());
        
        compra.setEstado("Registrada");
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

        if ("RECIBIDO".equals(compra.getEstadoLogistico())) {
            throw new RuntimeException("Esta compra ya fue recepcionada anteriormente.");
        }

        Usuario almacenero = usuarioRepository.findByEmail(emailAlmacenero)
                .orElseThrow(() -> new RuntimeException("Usuario almacen no encontrado"));

        for (DetalleCompra detalle : compra.getDetalle()) {
            Producto p = detalle.getProducto();
            p.setStockActual(p.getStockActual() + detalle.getCantidad());
            productoRepository.save(p);
        }

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
    
    // === NUEVO MÉTODO ===
    @Override
    @Transactional
    public void registrarFacturaFisica(Integer compraId, String numeroFactura) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        compra.setNumeroDocumentoFisico(numeroFactura);
        compra.setTipoDocumento("FACTURA"); // Cambia el estado oficial
        
        compraRepository.save(compra);
    }
    
    @Override
    public List<Compra> listarCotizaciones() {
        // Solo devolvemos las que sean 'COTIZACION'
        return compraRepository.findByTipoDocumento("COTIZACION");
    }

    @Override
    @Transactional
    public void registrarCotizacion(CompraDTO dto, String emailUsuario) {
        // Reutilizamos lógica similar a registrarCompra, pero cambiamos el TIPO
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra cotizacion = new Compra();
        cotizacion.setProveedor(proveedor);
        cotizacion.setUsuario(usuario);
        cotizacion.setMontoTotal(dto.getTotal().doubleValue());
        
        // ESTADOS PARA COTIZACIÓN
        cotizacion.setEstado("Borrador");
        cotizacion.setTipoDocumento("COTIZACION"); // <--- LA CLAVE
        cotizacion.setEstadoLogistico("NA");       // No aplica logística aún

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
        compra.setEstado("Aprobada");
        compra.setEstadoLogistico("PENDIENTE"); // Ahora sí espera mercadería
        
        compraRepository.save(compra);
    }
}