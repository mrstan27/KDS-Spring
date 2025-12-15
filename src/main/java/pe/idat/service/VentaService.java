package pe.idat.service;

import pe.idat.dto.VentaDTO;
import pe.idat.dto.ItemCarrito;
import pe.idat.entity.Venta;
import java.util.List;

public interface VentaService {
    // Para el vendedor en tienda física (POS)
    Venta registrarVenta(VentaDTO ventaDTO, String emailVendedor);
    
    // Para el cliente en la web (Ecommerce)
    Venta registrarVentaWeb(List<ItemCarrito> items, String emailCliente);

    // --- NUEVO: Necesario para el reporte ---
    List<Venta> listarVentas();
}