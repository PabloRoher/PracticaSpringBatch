package io.bootify.practica_spring_batch.rest;

import io.bootify.practica_spring_batch.model.AuditoriaTransaccionDTO;
import io.bootify.practica_spring_batch.service.AuditoriaTransaccionService;
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
@RequestMapping(value = "/api/auditoriaTransaccions", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuditoriaTransaccionResource {

    private final AuditoriaTransaccionService auditoriaTransaccionService;

    public AuditoriaTransaccionResource(
            final AuditoriaTransaccionService auditoriaTransaccionService) {
        this.auditoriaTransaccionService = auditoriaTransaccionService;
    }

    @GetMapping
    public ResponseEntity<List<AuditoriaTransaccionDTO>> getAllAuditoriaTransaccions() {
        return ResponseEntity.ok(auditoriaTransaccionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaTransaccionDTO> getAuditoriaTransaccion(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(auditoriaTransaccionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAuditoriaTransaccion(
            @RequestBody @Valid final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        final Long createdId = auditoriaTransaccionService.create(auditoriaTransaccionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAuditoriaTransaccion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AuditoriaTransaccionDTO auditoriaTransaccionDTO) {
        auditoriaTransaccionService.update(id, auditoriaTransaccionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAuditoriaTransaccion(
            @PathVariable(name = "id") final Long id) {
        auditoriaTransaccionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
