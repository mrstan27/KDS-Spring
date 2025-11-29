package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.MovimientoAlmacen;

import java.util.List;

@Repository
public interface MovimientoAlmacenRepository extends JpaRepository<MovimientoAlmacen, Integer> {

    // 1. Buscar movimientos por ID de Compra
    // Esto es CRÍTICO para evitar duplicados: "¿Ya existe una entrada para la Compra #50?"
    // Spring Data entiende el guion bajo "_" como: "Entra a compraReferencia y busca su compraId"
    List<MovimientoAlmacen> findByCompraReferencia_CompraId(Integer compraId);

    // 2. Buscar por tipo (Para reportes: "Dame todas las ENTRADAS")
    List<MovimientoAlmacen> findByTipoMovimiento(String tipoMovimiento);
    
    // 3. Buscar movimientos realizados por un usuario específico (Auditoría)
    List<MovimientoAlmacen> findByUsuario_UsuarioId(String usuarioId);
}