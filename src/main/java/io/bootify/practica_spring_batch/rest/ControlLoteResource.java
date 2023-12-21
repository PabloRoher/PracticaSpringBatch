package io.bootify.practica_spring_batch.rest;

import io.bootify.practica_spring_batch.model.ControlLoteDTO;
import io.bootify.practica_spring_batch.service.ControlLoteService;
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
@RequestMapping(value = "/api/controlLotes", produces = MediaType.APPLICATION_JSON_VALUE)
public class ControlLoteResource {

    private final ControlLoteService controlLoteService;

    public ControlLoteResource(final ControlLoteService controlLoteService) {
        this.controlLoteService = controlLoteService;
    }

    @GetMapping
    public ResponseEntity<List<ControlLoteDTO>> getAllControlLotes() {
        return ResponseEntity.ok(controlLoteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ControlLoteDTO> getControlLote(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(controlLoteService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createControlLote(
            @RequestBody @Valid final ControlLoteDTO controlLoteDTO) {
        final Long createdId = controlLoteService.create(controlLoteDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateControlLote(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ControlLoteDTO controlLoteDTO) {
        controlLoteService.update(id, controlLoteDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteControlLote(@PathVariable(name = "id") final Long id) {
        controlLoteService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
