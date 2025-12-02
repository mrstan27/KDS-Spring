package pe.idat.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest; // IMPORTANTE
import pe.idat.entity.Cliente;
import pe.idat.entity.Usuario;
import pe.idat.repository.ClienteRepository;
import pe.idat.repository.UsuarioRepository;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private HttpServletRequest request; // Para leer el input oculto

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // LEEMOS LA MARCA DEL FORMULARIO
        String tipoAcceso = request.getParameter("tipoAcceso");
        
        // =======================================================
        // CASO 1: LOGIN DE ADMIN (Solo busca en USUARIOS)
        // =======================================================
        if (tipoAcceso != null && tipoAcceso.equals("admin")) {
            Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);

            if (usuario != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombreRol());
                return new User(usuario.getEmail(), usuario.getPasswordHash(), usuario.getActivo(), 
                                true, true, true, Collections.singletonList(authority));
            }
            // Si no lo encuentra, NO busca en clientes. Lanza error.
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        // =======================================================
        // CASO 2: LOGIN DE CLIENTE (Solo busca en CLIENTES)
        // =======================================================
        if (tipoAcceso != null && tipoAcceso.equals("cliente")) {
            Cliente cliente = clienteRepository.findByCorreo(email);

            if (cliente != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("CLIENTE");
                return new User(cliente.getCorreo(), cliente.getPassword(), true, 
                                true, true, true, Collections.singletonList(authority));
            }
            // Si no lo encuentra, NO busca en admins. Lanza error.
            throw new UsernameNotFoundException("Cliente no encontrado");
        }

        throw new UsernameNotFoundException("Tipo de acceso desconocido");
    }
}