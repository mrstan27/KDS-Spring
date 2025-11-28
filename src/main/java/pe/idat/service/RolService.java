package pe.idat.service;

import java.util.List;
import pe.idat.entity.Rol;

public interface RolService {
    public List<Rol> listarRoles();
    public Rol guardarRol(Rol rol);
    public Rol obtenerRolPorId(Integer id);
    public void eliminarRol(Integer id);
}