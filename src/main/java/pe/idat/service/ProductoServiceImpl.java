package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // <--- IMPORTANTE

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
    @Transactional(readOnly = true) // Solo lectura, más rápido
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    @Transactional // Escritura (si falla, deshace cambios)
    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto obtenerProductoPorId(Integer id) {
        return productoRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // El nuevo método que agregamos
    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarPorNombreCategoria(String nombreCategoria) {
        return productoRepository.findByCategoria_NombreCategoria(nombreCategoria);
    }
}