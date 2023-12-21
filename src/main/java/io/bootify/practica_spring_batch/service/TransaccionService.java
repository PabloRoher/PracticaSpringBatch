package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.ControlLote;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.domain.Transaccion;
import io.bootify.practica_spring_batch.model.TransaccionDTO;
import io.bootify.practica_spring_batch.repos.ControlLoteRepository;
import io.bootify.practica_spring_batch.repos.CuentaRepository;
import io.bootify.practica_spring_batch.repos.TransaccionRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;
    private final ControlLoteRepository controlLoteRepository;

    public TransaccionService(final TransaccionRepository transaccionRepository,
            final CuentaRepository cuentaRepository,
            final ControlLoteRepository controlLoteRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
        this.controlLoteRepository = controlLoteRepository;
    }

    public List<TransaccionDTO> findAll() {
        final List<Transaccion> transaccions = transaccionRepository.findAll(Sort.by("id"));
        return transaccions.stream()
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .toList();
    }

    public TransaccionDTO get(final Long id) {
        return transaccionRepository.findById(id)
                .map(transaccion -> mapToDTO(transaccion, new TransaccionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = new Transaccion();
        mapToEntity(transaccionDTO, transaccion);
        return transaccionRepository.save(transaccion).getId();
    }

    public void update(final Long id, final TransaccionDTO transaccionDTO) {
        final Transaccion transaccion = transaccionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transaccionDTO, transaccion);
        transaccionRepository.save(transaccion);
    }

    public void delete(final Long id) {
        transaccionRepository.deleteById(id);
    }

    private TransaccionDTO mapToDTO(final Transaccion transaccion,
            final TransaccionDTO transaccionDTO) {
        transaccionDTO.setId(transaccion.getId());
        transaccionDTO.setCuentaID(transaccion.getCuentaID());
        transaccionDTO.setMonto(transaccion.getMonto());
        transaccionDTO.setSaldoPostTransaccion(transaccion.getSaldoPostTransaccion());
        transaccionDTO.setCuenta(transaccion.getCuenta() == null ? null : transaccion.getCuenta().getId());
        transaccionDTO.setControlLote(transaccion.getControlLote() == null ? null : transaccion.getControlLote().getId());
        return transaccionDTO;
    }

    private Transaccion mapToEntity(final TransaccionDTO transaccionDTO,
            final Transaccion transaccion) {
        transaccion.setCuentaID(transaccionDTO.getCuentaID());
        transaccion.setMonto(transaccionDTO.getMonto());
        transaccion.setSaldoPostTransaccion(transaccionDTO.getSaldoPostTransaccion());
        final Cuenta cuenta = transaccionDTO.getCuenta() == null ? null : cuentaRepository.findById(transaccionDTO.getCuenta())
                .orElseThrow(() -> new NotFoundException("cuenta not found"));
        transaccion.setCuenta(cuenta);
        final ControlLote controlLote = transaccionDTO.getControlLote() == null ? null : controlLoteRepository.findById(transaccionDTO.getControlLote())
                .orElseThrow(() -> new NotFoundException("controlLote not found"));
        transaccion.setControlLote(controlLote);
        return transaccion;
    }

}
