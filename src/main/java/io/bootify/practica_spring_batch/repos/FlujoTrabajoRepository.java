package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.FlujoTrabajo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlujoTrabajoRepository extends JpaRepository<FlujoTrabajo, Long> {

    FlujoTrabajo findFirstByLotes(ControlLote controlLote);

    List<FlujoTrabajo> findAllByLotes(ControlLote controlLote);

}
