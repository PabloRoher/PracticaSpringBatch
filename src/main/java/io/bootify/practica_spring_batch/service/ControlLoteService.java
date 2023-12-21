package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.AuditoriaTransaccion;
import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.FlujoTrabajo;
import io.bootify.practica_spring_batch.domain.Transaccion;
import io.bootify.practica_spring_batch.model.ControlLoteDTO;
import io.bootify.practica_spring_batch.repos.AuditoriaTransaccionRepository;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.repos.FlujoTrabajoRepository;
import io.bootify.practica_spring_batch.repos.TransaccionRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import io.bootify.practica_spring_batch.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ControlLoteService {

    private final ControlLoteRepository controlLoteRepository;
    private final FlujoTrabajoRepository flujoTrabajoRepository;
    private final TransaccionRepository transaccionRepository;
    private final AuditoriaTransaccionRepository auditoriaTransaccionRepository;

    public ControlLoteService(final ControlLoteRepository controlLoteRepository,
            final FlujoTrabajoRepository flujoTrabajoRepository,
            final TransaccionRepository transaccionRepository,
            final AuditoriaTransaccionRepository auditoriaTransaccionRepository) {
        this.controlLoteRepository = controlLoteRepository;
        this.flujoTrabajoRepository = flujoTrabajoRepository;
        this.transaccionRepository = transaccionRepository;
        this.auditoriaTransaccionRepository = auditoriaTransaccionRepository;
    }

    public List<ControlLoteDTO> findAll() {
        final List<ControlLote> controlLotes = controlLoteRepository.findAll(Sort.by("id"));
        return controlLotes.stream()
                .map(controlLote -> mapToDTO(controlLote, new ControlLoteDTO()))
                .toList();
    }

    public ControlLoteDTO get(final Long id) {
        return controlLoteRepository.findById(id)
                .map(controlLote -> mapToDTO(controlLote, new ControlLoteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ControlLoteDTO controlLoteDTO) {
        final ControlLote controlLote = new ControlLote();
        mapToEntity(controlLoteDTO, controlLote);
        return controlLoteRepository.save(controlLote).getId();
    }

    public void update(final Long id, final ControlLoteDTO controlLoteDTO) {
        final ControlLote controlLote = controlLoteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(controlLoteDTO, controlLote);
        controlLoteRepository.save(controlLote);
    }

    public void delete(final Long id) {
        final ControlLote controlLote = controlLoteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        flujoTrabajoRepository.findAllByLotes(controlLote)
                .forEach(flujoTrabajo -> flujoTrabajo.getLotes().remove(controlLote));
        controlLoteRepository.delete(controlLote);
    }

    private ControlLoteDTO mapToDTO(final ControlLote controlLote,
            final ControlLoteDTO controlLoteDTO) {
        controlLoteDTO.setId(controlLote.getId());
        controlLoteDTO.setEstado(controlLote.getEstado());
        controlLoteDTO.setRegistrosProcesados(controlLote.getRegistrosProcesados());
        controlLoteDTO.setUltimoRegistroProcesadoID(controlLote.getUltimoRegistroProcesadoID());
        return controlLoteDTO;
    }

    private ControlLote mapToEntity(final ControlLoteDTO controlLoteDTO,
            final ControlLote controlLote) {
        controlLote.setEstado(controlLoteDTO.getEstado());
        controlLote.setRegistrosProcesados(controlLoteDTO.getRegistrosProcesados());
        controlLote.setUltimoRegistroProcesadoID(controlLoteDTO.getUltimoRegistroProcesadoID());
        return controlLote;
    }

    public String getReferencedWarning(final Long id) {
        final ControlLote controlLote = controlLoteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Transaccion controlLoteTransaccion = transaccionRepository.findFirstByControlLote(controlLote);
        if (controlLoteTransaccion != null) {
            return WebUtils.getMessage("controlLote.transaccion.controlLote.referenced", controlLoteTransaccion.getId());
        }
        final AuditoriaTransaccion controlLoteAuditoriaTransaccion = auditoriaTransaccionRepository.findFirstByControlLote(controlLote);
        if (controlLoteAuditoriaTransaccion != null) {
            return WebUtils.getMessage("controlLote.auditoriaTransaccion.controlLote.referenced", controlLoteAuditoriaTransaccion.getId());
        }
        final FlujoTrabajo lotesFlujoTrabajo = flujoTrabajoRepository.findFirstByLotes(controlLote);
        if (lotesFlujoTrabajo != null) {
            return WebUtils.getMessage("controlLote.flujoTrabajo.lotes.referenced", lotesFlujoTrabajo.getId());
        }
        return null;
    }

}
