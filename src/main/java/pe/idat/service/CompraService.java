package pe.idat.service;

import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;

public interface CompraService {
    
    // Paso 1: Generar la Orden
    Compra registrarCompra(CompraDTO compraDTO, String emailUsuario);
    
    // Paso 2: Registrar Factura Física (NUEVO)
    void registrarFacturaFisica(Integer compraId, String numeroFactura);

    // Paso 3: Confirmar llegada del camión
    void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero);
}