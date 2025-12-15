package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Producto;
import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    
    List<Producto> findByCategoria_NombreCategoria(String nombreCategoria);
    
    // NUEVO: Buscar productos por ID de proveedor
    List<Producto> findByProveedor_ProveedorId(Integer proveedorId);
}