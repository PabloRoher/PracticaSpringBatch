package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    Optional<Transaccion> findByCuentaID(String cuentaID);
    Transaccion findFirstByCuenta(Cuenta cuenta);

    Transaccion findFirstByControlLote(ControlLote controlLote);

}
