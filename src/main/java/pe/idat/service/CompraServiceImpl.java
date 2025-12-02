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
            int stockAntiguo = p.getStockActual();
            int cantidadRecibida = detalle.getCantidad();
            p.setStockActual(stockAntiguo + cantidadRecibida);
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

    // === NUEVO MÉTODO PARA REGISTRAR FACTURA ===
    @Override
    @Transactional
    public void registrarFacturaFisica(Integer compraId, String numeroFactura) {
        Compra compra = compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        
        // Actualizamos los datos
        compra.setNumeroDocumentoFisico(numeroFactura);
        compra.setTipoDocumento("FACTURA"); // Cambia de Orden a Factura oficial
        
        compraRepository.save(compra);
    }
}