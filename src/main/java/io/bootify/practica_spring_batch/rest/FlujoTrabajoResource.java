package io.bootify.practica_spring_batch.rest;

import io.bootify.practica_spring_batch.model.FlujoTrabajoDTO;
import io.bootify.practica_spring_batch.service.FlujoTrabajoService;
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
@RequestMapping(value = "/api/flujoTrabajos", produces = MediaType.APPLICATION_JSON_VALUE)
public class FlujoTrabajoResource {

    private final FlujoTrabajoService flujoTrabajoService;

    public FlujoTrabajoResource(final FlujoTrabajoService flujoTrabajoService) {
        this.flujoTrabajoService = flujoTrabajoService;
    }

    @GetMapping
    public ResponseEntity<List<FlujoTrabajoDTO>> getAllFlujoTrabajos() {
        return ResponseEntity.ok(flujoTrabajoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlujoTrabajoDTO> getFlujoTrabajo(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(flujoTrabajoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createFlujoTrabajo(
            @RequestBody @Valid final FlujoTrabajoDTO flujoTrabajoDTO) {
        final Long createdId = flujoTrabajoService.create(flujoTrabajoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateFlujoTrabajo(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final FlujoTrabajoDTO flujoTrabajoDTO) {
        flujoTrabajoService.update(id, flujoTrabajoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteFlujoTrabajo(@PathVariable(name = "id") final Long id) {
        flujoTrabajoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
