package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.FlujoTrabajo;
import io.bootify.practica_spring_batch.model.FlujoTrabajoDTO;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.repos.FlujoTrabajoRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class FlujoTrabajoService {

    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final ControlLoteRepository controlLoteRepository;

    public FlujoTrabajoService(final FlujoTrabajoRepository flujoTrabajoRepository,
            final ControlLoteRepository controlLoteRepository) {
        this.flujoTrabajoRepository = flujoTrabajoRepository;
        this.controlLoteRepository = controlLoteRepository;
    }

    public List<FlujoTrabajoDTO> findAll() {
        final List<FlujoTrabajo> flujoTrabajoes = flujoTrabajoRepository.findAll(Sort.by("id"));
        return flujoTrabajoes.stream()
                .map(flujoTrabajo -> mapToDTO(flujoTrabajo, new FlujoTrabajoDTO()))
                .toList();
    }

    public FlujoTrabajoDTO get(final Long id) {
        return flujoTrabajoRepository.findById(id)
                .map(flujoTrabajo -> mapToDTO(flujoTrabajo, new FlujoTrabajoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final FlujoTrabajoDTO flujoTrabajoDTO) {
        final FlujoTrabajo flujoTrabajo = new FlujoTrabajo();
        mapToEntity(flujoTrabajoDTO, flujoTrabajo);
        return flujoTrabajoRepository.save(flujoTrabajo).getId();
    }

    public void update(final Long id, final FlujoTrabajoDTO flujoTrabajoDTO) {
        final FlujoTrabajo flujoTrabajo = flujoTrabajoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(flujoTrabajoDTO, flujoTrabajo);
        flujoTrabajoRepository.save(flujoTrabajo);
    }

    public void delete(final Long id) {
        flujoTrabajoRepository.deleteById(id);
    }

    private FlujoTrabajoDTO mapToDTO(final FlujoTrabajo flujoTrabajo,
            final FlujoTrabajoDTO flujoTrabajoDTO) {
        flujoTrabajoDTO.setId(flujoTrabajo.getId());
        flujoTrabajoDTO.setNombre(flujoTrabajo.getNombre());
        flujoTrabajoDTO.setPaso(flujoTrabajo.getPaso());
        flujoTrabajoDTO.setEstadoPaso(flujoTrabajo.getEstadoPaso());
        flujoTrabajoDTO.setLotes(flujoTrabajo.getLotes().stream()
                .map(controlLote -> controlLote.getId())
                .toList());
        return flujoTrabajoDTO;
    }

    private FlujoTrabajo mapToEntity(final FlujoTrabajoDTO flujoTrabajoDTO,
            final FlujoTrabajo flujoTrabajo) {
        flujoTrabajo.setNombre(flujoTrabajoDTO.getNombre());
        flujoTrabajo.setPaso(flujoTrabajoDTO.getPaso());
        flujoTrabajo.setEstadoPaso(flujoTrabajoDTO.getEstadoPaso());
        final List<ControlLote> lotes = controlLoteRepository.findAllById(
                flujoTrabajoDTO.getLotes() == null ? Collections.emptyList() : flujoTrabajoDTO.getLotes());
        if (lotes.size() != (flujoTrabajoDTO.getLotes() == null ? 0 : flujoTrabajoDTO.getLotes().size())) {
            throw new NotFoundException("one of lotes not found");
        }
        flujoTrabajo.setLotes(lotes.stream().collect(Collectors.toSet()));
        return flujoTrabajo;
    }

}
