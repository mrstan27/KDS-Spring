package pe.idat.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.idat.entity.Cliente;
import pe.idat.entity.Usuario;
import pe.idat.repository.ClienteRepository;
import pe.idat.repository.UsuarioRepository;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository; // <--- AGREGAMOS ESTO

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // -----------------------------------------------------------
        // 1. PRIMER INTENTO: BUSCAR EN LA TABLA USUARIOS (Admin/Staff)
        // -----------------------------------------------------------
        // Usamos .orElse(null) para que NO lance error si no encuentra, y nos deje seguir buscando
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

        if (usuario != null) {
            // Si lo encontró aquí, es un empleado
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombreRol());
            
            return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.getActivo(),
                true, true, true,
                Collections.singletonList(authority)
            );
        }

        // -----------------------------------------------------------
        // 2. SEGUNDO INTENTO: BUSCAR EN LA TABLA CLIENTES
        // -----------------------------------------------------------
        // Importante: Asegúrate de tener findByCorreo en ClienteRepository
        Cliente cliente = clienteRepository.findByCorreo(email);

        if (cliente != null) {
            // Si lo encontró aquí, es un cliente
            // Le asignamos manualmente el rol "CLIENTE" para diferenciarlo
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("CLIENTE");

            return new User(
                cliente.getCorreo(),
                cliente.getPassword(), // La contraseña que ya registraste encriptada
                true, // Asumimos activo (o usa cliente.getEstado().equals("Activo"))
                true, true, true,
                Collections.singletonList(authority)
            );
        }

        // -----------------------------------------------------------
        // 3. SI NO ESTÁ EN NINGUNA -> ERROR
        // -----------------------------------------------------------
        throw new UsernameNotFoundException("Usuario o Cliente no encontrado con correo: " + email);
    }
}