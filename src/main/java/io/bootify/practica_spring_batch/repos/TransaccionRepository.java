package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.domain.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {

    Transaccion findFirstByCuenta(Cuenta cuenta);

    Transaccion findFirstByControlLote(ControlLote controlLote);

}
