package pe.idat.service;

import java.util.List; // <--- ¡ESTA LÍNEA ES LA QUE TE FALTA!
import pe.idat.dto.CompraDTO;
import pe.idat.entity.Compra;

public interface CompraService {
    
    // Paso 1: Generar la Orden
    Compra registrarCompra(CompraDTO compraDTO, String emailUsuario);
    
    // Paso 2: Registrar Factura Física
    void registrarFacturaFisica(Integer compraId, String numeroFactura);

    // Paso 3: Confirmar llegada del camión
    void confirmarRecepcionMercaderia(Integer compraId, String emailAlmacenero);
    
    // === NUEVOS MÉTODOS PARA COTIZACIONES ===
    List<Compra> listarCotizaciones();
    
    void registrarCotizacion(CompraDTO compraDTO, String emailUsuario);
    
    void convertirCotizacionAOrden(Integer compraId);
}