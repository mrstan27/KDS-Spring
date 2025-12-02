package pe.idat.service;

import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;

public interface CompraService {
    
    Compra registrarCompra(CompraDTO compraDTO, String emailUsuario);
    
    void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero);
    
    // NUEVO: Método para registrar el número de factura físico
    void registrarFacturaFisica(Integer compraId, String numeroFactura);
}