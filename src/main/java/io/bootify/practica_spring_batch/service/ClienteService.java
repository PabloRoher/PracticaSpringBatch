package io.bootify.practica_spring_batch.service;

import io.bootify.practica_spring_batch.domain.Cliente;
import io.bootify.practica_spring_batch.domain.Cuenta;
import io.bootify.practica_spring_batch.model.ClienteDTO;
import io.bootify.practica_spring_batch.repos.ClienteRepository;
import io.bootify.practica_spring_batch.repos.CuentaRepository;
import io.bootify.practica_spring_batch.util.NotFoundException;
import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public ClienteService(final ClienteRepository clienteRepository,
            final CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<ClienteDTO> findAll() {
        final List<Cliente> clientes = clienteRepository.findAll(Sort.by("id"));
        return clientes.stream()
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .toList();
    }

    public ClienteDTO get(final Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> mapToDTO(cliente, new ClienteDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ClienteDTO clienteDTO) {
        final Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente).getId();
    }

    public void update(final Long id, final ClienteDTO clienteDTO) {
        final Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(clienteDTO, cliente);
        clienteRepository.save(cliente);
    }

    public void delete(final Long id) {
        clienteRepository.deleteById(id);
    }

    private ClienteDTO mapToDTO(final Cliente cliente, final ClienteDTO clienteDTO) {
        clienteDTO.setId(cliente.getId());
        clienteDTO.setNombre(cliente.getNombre());
        clienteDTO.setDireccion(cliente.getDireccion());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setEmail(cliente.getEmail());
        clienteDTO.setCuentas(cliente.getCuentas().stream()
                .map(cuenta -> cuenta.getId())
                .toList());
        return clienteDTO;
    }

    private Cliente mapToEntity(final ClienteDTO clienteDTO, final Cliente cliente) {
        cliente.setNombre(clienteDTO.getNombre());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setEmail(clienteDTO.getEmail());
        final List<Cuenta> cuentas = cuentaRepository.findAllById(
                clienteDTO.getCuentas() == null ? Collections.emptyList() : clienteDTO.getCuentas());
        if (cuentas.size() != (clienteDTO.getCuentas() == null ? 0 : clienteDTO.getCuentas().size())) {
            throw new NotFoundException("one of cuentas not found");
        }
        cliente.setCuentas(cuentas.stream().collect(Collectors.toSet()));
        return cliente;
    }

}
