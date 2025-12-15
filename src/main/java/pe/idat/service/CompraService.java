package pe.idat.service;

import java.util.List;
import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;

public interface CompraService {

    Compra registrarCompra(CompraDTO compraDTO, String emailUsuario);
    void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero);
    void registrarFacturaFisica(Integer compraId, String numeroFactura);
    
    // Cotizaciones
    List<Compra> listarCotizaciones();
    void registrarCotizacion(CompraDTO compraDTO, String emailUsuario);
    void convertirCotizacionAOrden(Integer compraId);
    
    // --- NUEVOS MÉTODOS DE APROBACIÓN ---
    void aprobarOrdenCompra(Integer compraId);
    void rechazarOrdenCompra(Integer compraId);
}