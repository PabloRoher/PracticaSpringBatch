package io.bootify.practica_spring_batch.repos;

import io.bootify.practica_spring_batch.domain.ControlLote;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ControlLoteRepository extends JpaRepository<ControlLote, Long> {
}
