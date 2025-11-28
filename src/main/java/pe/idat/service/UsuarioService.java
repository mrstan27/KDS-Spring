package pe.idat.service;

import java.util.List;
import pe.idat.entity.Usuario;

public interface UsuarioService {
    public List<Usuario> listarUsuarios();
    public Usuario guardarUsuario(Usuario usuario);
    public Usuario obtenerUsuarioPorId(String id);
    public void eliminarUsuario(String id);
    
    // Método auxiliar para listar roles en el formulario
    public List<pe.idat.entity.Rol> listarRoles();
}