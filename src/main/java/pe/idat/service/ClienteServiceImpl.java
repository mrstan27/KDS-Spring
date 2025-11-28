package pe.idat.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.idat.entity.Cliente;
import pe.idat.repository.ClienteRepository;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Integer cliente_id) {
        return clienteRepository.findById(cliente_id).orElse(null);
    }

    @Override
    public Cliente buscarPorNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento);
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminar(Integer id) {
        clienteRepository.deleteById(id);
    }
}
