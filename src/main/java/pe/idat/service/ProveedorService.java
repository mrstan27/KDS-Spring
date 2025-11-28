package pe.idat.service;

import pe.idat.entity.Proveedor;
import java.util.List;

public interface ProveedorService {

    List<Proveedor> listarTodos();

    Proveedor guardar(Proveedor proveedor);

    Proveedor buscarPorId(Integer id);

    Proveedor buscarPorRuc(String ruc);

    void eliminar(Integer id);
}
