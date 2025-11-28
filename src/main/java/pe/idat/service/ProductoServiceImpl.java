package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.idat.entity.Producto;
import pe.idat.entity.Categoria;
import pe.idat.repository.ProductoRepository;
import pe.idat.repository.CategoriaRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }
    
    @Override
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }
}