package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.idat.entity.Categoria;
import pe.idat.repository.CategoriaRepository;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria guardarCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria obtenerCategoriaPorId(Integer id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarCategoria(Integer id) {
        // NOTA: Si intentas borrar una categoría que ya tiene productos, 
        // la base de datos lanzará un error por seguridad (Integridad Referencial).
        categoriaRepository.deleteById(id);
    }
}