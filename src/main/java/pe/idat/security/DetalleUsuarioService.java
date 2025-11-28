package pe.idat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import pe.idat.entity.Usuario;
import pe.idat.repository.UsuarioRepository;

import java.util.Collections;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 1. Buscamos el usuario por email
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));

        // 2. Convertimos el ROL de tu BD a un "Authority" de Spring
        // Spring espera roles con el formato "ROLE_Nombre" o simple strings
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getNombreRol());

        // 3. Retornamos el objeto User de Spring Security con los datos de TU base de datos
        return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                usuario.getActivo(), // enabled (activo)
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                Collections.singletonList(authority) // Lista de roles
        );
    }
}