package pe.idat.service;

import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;

public interface CompraService {
    
    // Paso 1: Generar la Orden (No mueve stock)
    Compra registrarCompra(CompraDTO compraDTO, String emailUsuario);
    
    // Paso 2: Registrar Factura Física (Nuevo)
    void registrarFacturaFisica(Integer compraId, String numeroFactura);

    // Paso 3: Confirmar llegada del camión (Mueve stock y genera Kardex)
    void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero);
}