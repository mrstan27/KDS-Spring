package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Compra;
import java.util.List; // Importar List

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
    
    // Método mágico para filtrar por tipo (COTIZACION, ORDEN_COMPRA, FACTURA)
    List<Compra> findByTipoDocumento(String tipoDocumento);
}