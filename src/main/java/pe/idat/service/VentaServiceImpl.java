package pe.idat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.idat.dto.DetalleDTO;
import pe.idat.dto.ItemCarrito;
import pe.idat.dto.VentaDTO;
import pe.idat.entity.*;
import pe.idat.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaServiceImpl implements VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private MovimientoAlmacenRepository movimientoRepository;

    // Lógica para Tienda Física (POS)
    @Override
    @Transactional
    public Venta registrarVenta(VentaDTO dto, String emailVendedor) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Usuario vendedor = usuarioRepository.findByEmail(emailVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setTipoVenta("TIENDA");
        venta.setMontoTotal(dto.getTotal().doubleValue());
        
        for (DetalleDTO item : dto.getItems()) {
            procesarDetalle(venta, item.getProductoId(), item.getCantidad(), item.getPrecio().doubleValue());
        }

        Venta ventaGuardada = ventaRepository.save(venta);
        registrarMovimiento(ventaGuardada, vendedor, "Venta Tienda");
        return ventaGuardada;
    }

    // Lógica para E-Commerce (Web)
    @Override
    @Transactional
    public Venta registrarVentaWeb(List<ItemCarrito> items, String emailCliente) {
        Cliente cliente = clienteRepository.findByCorreo(emailCliente);
        if (cliente == null) throw new RuntimeException("Cliente no identificado.");

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setVendedor(null); // No hay vendedor físico
        venta.setTipoVenta("WEB");
        
        double total = items.stream().mapToDouble(ItemCarrito::getSubtotal).sum();
        venta.setMontoTotal(total);

        for (ItemCarrito item : items) {
            procesarDetalle(venta, item.getProductoId(), item.getCantidad(), item.getPrecio());
        }
        
        Venta ventaGuardada = ventaRepository.save(venta);
        registrarMovimiento(ventaGuardada, null, "Venta Web - Cliente: " + cliente.getNombre());
        return ventaGuardada;
    }

    // Métodos auxiliares para no repetir código
    private void procesarDetalle(Venta venta, Integer productoId, Integer cantidad, Double precio) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + productoId));

        if (producto.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
        }

        // 1. Restar Stock
        producto.setStockActual(producto.getStockActual() - cantidad);
        productoRepository.save(producto);

        // 2. Crear Detalle
        DetalleVenta detalle = new DetalleVenta();
        detalle.setProducto(producto);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(cantidad * precio);
        
        venta.agregarDetalle(detalle);
    }

    private void registrarMovimiento(Venta venta, Usuario responsable, String motivoBase) {
        MovimientoAlmacen mov = new MovimientoAlmacen();
        mov.setFechaMovimiento(LocalDateTime.now());
        mov.setTipoMovimiento("SALIDA");
        mov.setMotivo(motivoBase + " #" + venta.getVentaId());
        mov.setUsuario(responsable); // Puede ser null en venta web
        movimientoRepository.save(mov);
    }
}