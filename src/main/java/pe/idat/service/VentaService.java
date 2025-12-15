package pe.idat.service;

import pe.idat.dto.VentaDTO;
import pe.idat.dto.ItemCarrito; // Importamos el carrito
import pe.idat.entity.Venta;
import java.util.List;

public interface VentaService {
    // Para el vendedor en tienda física (POS)
    Venta registrarVenta(VentaDTO ventaDTO, String emailVendedor);
    
    // NUEVO: Para el cliente en la web (Ecommerce)
    Venta registrarVentaWeb(List<ItemCarrito> items, String emailCliente);
}