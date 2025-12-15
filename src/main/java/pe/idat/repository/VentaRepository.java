package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Venta;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    
    // Método para buscar ventas por el ID del cliente y ordenarlas por fecha descendente
    List<Venta> findByCliente_ClienteIdOrderByFechaVentaDesc(Integer clienteId);
}