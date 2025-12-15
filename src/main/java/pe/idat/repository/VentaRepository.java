package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Venta;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {
    // Buscar ventas por cliente, ordenadas por fecha (la más reciente primero)
    List<Venta> findByCliente_ClienteIdOrderByFechaVentaDesc(Integer clienteId);
}