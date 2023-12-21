package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.AuditoriaTransaccion;
import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.model.AuditoriaTransaccionDTO;
import io.bootify.practica_spring_batch.repos.AuditoriaTransaccionRepository;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AuditoriaTransaccionService {

    private final AuditoriaTransaccionRepository auditoriaTransaccionRepository;
    private final ControlLoteRepository controlLoteRepository;

    public AuditoriaTransaccionService(
            final AuditoriaTransaccionRepository auditoriaTransaccionRepository,
            final ControlLoteRepository controlLoteRepository) {
        this.auditoriaTransaccionRepository = auditoriaTransaccionRepository;
        this.controlLoteRepository = controlLoteRepository;
    }

    public List<AuditoriaTransaccionDTO> findAll() {
        final List<AuditoriaTransaccion> auditoriaTransaccions = auditoriaTransaccionRepository.findAll(Sort.by("id"));
        return auditoriaTransaccions.stream()
                .map(auditoriaTransaccion -> mapToDTO(auditoriaTransaccion, new AuditoriaTransaccionDTO()))
                .toList();
    }

    public AuditoriaTransaccionDTO get(final Long id) {
        return auditoriaTransaccionRepository.findById(id)
                .map(auditoriaTransaccion -> mapToDTO(auditoriaTransaccion, new AuditoriaTransaccionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        final AuditoriaTransaccion auditoriaTransaccion = new AuditoriaTransaccion();
        mapToEntity(auditoriaTransaccionDTO, auditoriaTransaccion);
        return auditoriaTransaccionRepository.save(auditoriaTransaccion).getId();
    }

    public void update(final Long id, final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        final AuditoriaTransaccion auditoriaTransaccion = auditoriaTransaccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(auditoriaTransaccionDTO, auditoriaTransaccion);
        auditoriaTransaccionRepository.save(auditoriaTransaccion);
    }

    public void delete(final Long id) {
        auditoriaTransaccionRepository.deleteById(id);
    }

    private AuditoriaTransaccionDTO mapToDTO(final AuditoriaTransaccion auditoriaTransaccion,
            final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        auditoriaTransaccionDTO.setId(auditoriaTransaccion.getId());
        auditoriaTransaccionDTO.setTransaccionID(auditoriaTransaccion.getTransaccionID());
        auditoriaTransaccionDTO.setLoteID(auditoriaTransaccion.getLoteID());
        auditoriaTransaccionDTO.setEstadoTransaccion(auditoriaTransaccion.getEstadoTransaccion());
        auditoriaTransaccionDTO.setControlLote(auditoriaTransaccion.getControlLote() == null ? null : auditoriaTransaccion.getControlLote().getId());
        return auditoriaTransaccionDTO;
    }

    private AuditoriaTransaccion mapToEntity(final AuditoriaTransaccionDTO auditoriaTransaccionDTO,
            final AuditoriaTransaccion auditoriaTransaccion) {
        auditoriaTransaccion.setTransaccionID(auditoriaTransaccionDTO.getTransaccionID());
        auditoriaTransaccion.setLoteID(auditoriaTransaccionDTO.getLoteID());
        auditoriaTransaccion.setEstadoTransaccion(auditoriaTransaccionDTO.getEstadoTransaccion());
        final ControlLote controlLote = auditoriaTransaccionDTO.getControlLote() == null ? null : controlLoteRepository.findById(auditoriaTransaccionDTO.getControlLote())
                .orElseThrow(() -> new NotFoundException("controlLote not found"));
        auditoriaTransaccion.setControlLote(controlLote);
        return auditoriaTransaccion;
    }

}
