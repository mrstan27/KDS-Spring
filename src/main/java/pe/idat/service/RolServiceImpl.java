package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.idat.entity.Rol;
import pe.idat.repository.RolRepository;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public List<Rol> listarRoles() {
        return rolRepository.findAll();
    }

    @Override
    public Rol guardarRol(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public Rol obtenerRolPorId(Integer id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarRol(Integer id) {
        rolRepository.deleteById(id);
    }
}