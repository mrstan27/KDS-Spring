package pe.idat.service;

import java.util.List;

import pe.idat.entity.Cliente;

public interface ClienteService {
    List<Cliente> listar();
    Cliente buscarPorId(Integer cliente_id);
    Cliente buscarPorNumeroDocumento(String numeroDocumento);
    Cliente guardar(Cliente cliente);
    void eliminar(Integer cliente_id);
}
