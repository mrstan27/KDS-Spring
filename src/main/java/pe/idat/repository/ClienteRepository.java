package pe.idat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.idat.entity.Cliente;


@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    Cliente findByNumeroDocumento(String numeroDocumento);
}

