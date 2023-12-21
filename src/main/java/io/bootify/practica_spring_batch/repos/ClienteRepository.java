package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.Cliente;
import io.bootify.practica_spring_batch.domain.Cuenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Cliente findFirstByCuentas(Cuenta cuenta);

    List<Cliente> findAllByCuentas(Cuenta cuenta);

}
