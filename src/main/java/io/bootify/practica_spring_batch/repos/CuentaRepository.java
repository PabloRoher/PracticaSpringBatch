package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    boolean existsByNumeroCuentaIgnoreCase(String numeroCuenta);

}
