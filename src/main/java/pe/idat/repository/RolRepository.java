package pe.idat.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.entity.Rol;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    // AGREGA ESTA LÍNEA SI NO ESTÁ:
    Optional<Rol> findByNombreRol(String nombreRol);
}