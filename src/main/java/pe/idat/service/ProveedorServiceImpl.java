package pe.idat.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import pe.idat.repository.ProveedorRepository;
import pe.idat.entity.Proveedor;

import java.util.List;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Override
    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    @Override
    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id).orElse(null);
    }

    @Override
    public Proveedor buscarPorRuc(String ruc) {
        return proveedorRepository.findByRuc(ruc);
    }

    @Override
    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }
}
