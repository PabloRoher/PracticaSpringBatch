package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.AuditoriaTransaccion;
import io.bootify.practica_spring_batch.domain.ControlLote;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AuditoriaTransaccionRepository extends JpaRepository<AuditoriaTransaccion, Long> {

    AuditoriaTransaccion findFirstByControlLote(ControlLote controlLote);

}
