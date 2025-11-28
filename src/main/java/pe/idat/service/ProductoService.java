package pe.idat.service;
import java.util.List;
import pe.idat.entity.Producto;
import pe.idat.entity.Categoria;

public interface ProductoService {
    List<Producto> listarProductos();
    Producto guardarProducto(Producto producto);
    Producto obtenerProductoPorId(Integer id);
    void eliminarProducto(Integer id);
    List<Categoria> listarCategorias(); 
}