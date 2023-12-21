package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.Cliente;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.domain.Transaccion;
import io.bootify.practica_spring_batch.model.CuentaDTO;
import io.bootify.practica_spring_batch.repos.ClienteRepository;
import io.bootify.practica_spring_batch.repos.CuentaRepository;
import io.bootify.practica_spring_batch.repos.TransaccionRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import io.bootify.practica_spring_batch.util.WebUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final TransaccionRepository transaccionRepository;

    public CuentaService(final CuentaRepository cuentaRepository,
            final ClienteRepository clienteRepository,
            final TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<CuentaDTO> findAll() {
        final List<Cuenta> cuentas = cuentaRepository.findAll(Sort.by("id"));
        return cuentas.stream()
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .toList();
    }

    public CuentaDTO get(final Long id) {
        return cuentaRepository.findById(id)
                .map(cuenta -> mapToDTO(cuenta, new CuentaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = new Cuenta();
        mapToEntity(cuentaDTO, cuenta);
        return cuentaRepository.save(cuenta).getId();
    }

    public void update(final Long id, final CuentaDTO cuentaDTO) {
        final Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(cuentaDTO, cuenta);
        cuentaRepository.save(cuenta);
    }

    public void delete(final Long id) {
        final Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        clienteRepository.findAllByCuentas(cuenta)
                .forEach(cliente -> cliente.getCuentas().remove(cuenta));
        cuentaRepository.delete(cuenta);
    }

    private CuentaDTO mapToDTO(final Cuenta cuenta, final CuentaDTO cuentaDTO) {
        cuentaDTO.setId(cuenta.getId());
        cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaDTO.setTitularCuenta(cuenta.getTitularCuenta());
        cuentaDTO.setSaldo(cuenta.getSaldo());
        return cuentaDTO;
    }

    private Cuenta mapToEntity(final CuentaDTO cuentaDTO, final Cuenta cuenta) {
        cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuenta.setTitularCuenta(cuentaDTO.getTitularCuenta());
        cuenta.setSaldo(cuentaDTO.getSaldo());
        return cuenta;
    }

    public boolean numeroCuentaExists(final String numeroCuenta) {
        return cuentaRepository.existsByNumeroCuentaIgnoreCase(numeroCuenta);
    }

    public String getReferencedWarning(final Long id) {
        final Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Cliente cuentasCliente = clienteRepository.findFirstByCuentas(cuenta);
        if (cuentasCliente != null) {
            return WebUtils.getMessage("cuenta.cliente.cuentas.referenced", cuentasCliente.getId());
        }
        final Transaccion cuentaTransaccion = transaccionRepository.findFirstByCuenta(cuenta);
        if (cuentaTransaccion != null) {
            return WebUtils.getMessage("cuenta.transaccion.cuenta.referenced", cuentaTransaccion.getId());
        }
        return null;
    }

}
