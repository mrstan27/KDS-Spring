package pe.idat.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Integer> {
}