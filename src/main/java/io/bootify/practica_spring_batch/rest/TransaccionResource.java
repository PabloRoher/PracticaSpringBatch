package io.bootify.practica_spring_batch.rest;

import io.bootify.practica_spring_batch.model.TransaccionDTO;
import io.bootify.practica_spring_batch.service.TransaccionService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/transaccions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TransaccionResource {

    private final TransaccionService transaccionService;

    public TransaccionResource(final TransaccionService transaccionService) {
        this.transaccionService = transaccionService;
    }

    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> getAllTransaccions() {
        return ResponseEntity.ok(transaccionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransaccionDTO> getTransaccion(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(transaccionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransaccion(
            @RequestBody @Valid final TransaccionDTO transaccionDTO) {
        final Long createdId = transaccionService.create(transaccionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTransaccion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TransaccionDTO transaccionDTO) {
        transaccionService.update(id, transaccionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable(name = "id") final Long id) {
        transaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
