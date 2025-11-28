package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Usuario;
import java.util.Optional; // Importante

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    // Método mágico: Spring crea la query "SELECT * FROM usuarios WHERE email = ?"
    Optional<Usuario> findByEmail(String email);
}