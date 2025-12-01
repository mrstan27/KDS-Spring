package pe.idat.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
	
	// Esto le permite al Service pedir productos filtrados por el nombre de su categoría.
    List<Producto> findByCategoria_NombreCategoria(String nombreCategoria);
}