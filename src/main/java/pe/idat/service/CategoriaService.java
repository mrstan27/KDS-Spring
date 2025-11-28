package pe.idat.service;

import java.util.List;
import pe.idat.entity.Categoria;

public interface CategoriaService {
    List<Categoria> listarCategorias();
    Categoria guardarCategoria(Categoria categoria);
    Categoria obtenerCategoriaPorId(Integer id);
    void eliminarCategoria(Integer id);
}