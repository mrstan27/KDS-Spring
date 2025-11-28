package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// Importamos el encriptador
import org.springframework.security.crypto.password.PasswordEncoder;

import pe.idat.entity.Usuario;
import pe.idat.entity.Rol;
import pe.idat.repository.UsuarioRepository;
import pe.idat.repository.RolRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;

    // Inyectamos el encriptador que definimos en el Paso 4
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        // Lógica para detectar si es una EDICIÓN (tiene ID)
        if (usuario.getUsuarioId() != null && !usuario.getUsuarioId().isEmpty()) {
            
            // Caso 1: Viene sin contraseña (el usuario no quiere cambiarla)
            if (usuario.getPasswordHash() == null || usuario.getPasswordHash().isEmpty()) {
                // Buscamos al usuario original en la BD para recuperar su contraseña vieja
                Usuario usuarioAntiguo = usuarioRepository.findById(usuario.getUsuarioId()).orElse(null);
                if (usuarioAntiguo != null) {
                    usuario.setPasswordHash(usuarioAntiguo.getPasswordHash());
                }
            } 
            // Caso 2: El usuario escribió una nueva contraseña
            else {
                usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
            }
            
        } else {
            // Lógica para NUEVO USUARIO (No tiene ID)
            // Aquí la contraseña es obligatoria, así que la encriptamos siempre
            usuario.setPasswordHash(passwordEncoder.encode(usuario.getPasswordHash()));
        }
        
        return usuarioRepository.save(usuario);
    }

    // Ojo: Cambiamos Integer por String aquí también
    @Override
    public Usuario obtenerUsuarioPorId(String id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    // Y aquí también
    @Override
    public void eliminarUsuario(String id) {
        usuarioRepository.deleteById(id);
    }
    
    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }
}