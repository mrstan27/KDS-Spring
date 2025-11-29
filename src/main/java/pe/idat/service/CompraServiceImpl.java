package pe.idat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private MovimientoAlmacenRepository movimientoRepository; // ¡Nuevo Repo!

    @Override
    @Transactional
    public Compra registrarCompra(CompraDTO dto, String emailUsuario) {
        
        // 1. Validar Datos
        Proveedor proveedor = proveedorRepository.findById(dto.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Crear la Cabecera (ORDEN DE COMPRA)
        Compra compra = new Compra();
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);
        compra.setMontoTotal(dto.getTotal().doubleValue());
        
        // Configuración Inicial del Flujo
        compra.setEstado("Registrada");
        compra.setTipoDocumento("ORDEN_COMPRA"); // Es solo una intención de compra
        compra.setEstadoLogistico("PENDIENTE");  // La mercadería NO ha llegado aún

        // 3. Crear los Detalles (Líneas de la orden)
        for (DetalleDTO item : dto.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.getProductoId()));
            
            DetalleCompra detalle = new DetalleCompra();
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioCompra(item.getPrecio().doubleValue());
            
            compra.agregarDetalle(detalle);
            
            // IMPORTANTE: AQUÍ YA NO ACTUALIZAMOS EL STOCK
            // El stock se mantiene igual hasta que llegue el camión.
        }

        // 4. Guardar la Orden
        return compraRepository.save(compra);
    }

    @Override
    @Transactional
    public void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero) {
        // A. Buscar la compra
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        // B. Validar que no haya sido recibida antes (Evitar doble stock)
        if ("RECIBIDO".equals(compra.getEstadoLogistico())) {
            throw new RuntimeException("Esta compra ya fue recepcionada anteriormente.");
        }

        // C. Identificar al responsable (Almacenero)
        Usuario almacenero = usuarioRepository.findByEmail(emailAlmacenero)
                .orElseThrow(() -> new RuntimeException("Usuario almacen no encontrado"));

        // D. ACTUALIZACIÓN DE INVENTARIO (Ahora sí sumamos)
        for (DetalleCompra detalle : compra.getDetalle()) {
            Producto p = detalle.getProducto();
            
            int stockAntiguo = p.getStockActual();
            int cantidadRecibida = detalle.getCantidad();
            
            p.setStockActual(stockAntiguo + cantidadRecibida);
            productoRepository.save(p);
        }

        // E. REGISTRAR EL MOVIMIENTO EN KARDEX (Traza de auditoría)
        MovimientoAlmacen mov = new MovimientoAlmacen();
        mov.setFechaMovimiento(LocalDateTime.now());
        mov.setTipoMovimiento("ENTRADA"); // Ingreso de mercadería
        mov.setMotivo("Recepción de Compra #" + compra.getCompraId() + " - Prov: " + compra.getProveedor().getRazonSocial());
        mov.setUsuario(almacenero);
        mov.setCompraReferencia(compra); // Vinculamos el movimiento a la compra
        
        movimientoRepository.save(mov);

        // F. Actualizar estado de la compra a FINALIZADO
        compra.setEstadoLogistico("RECIBIDO");
        compraRepository.save(compra);
    }
}