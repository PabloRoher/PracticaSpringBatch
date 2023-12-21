package io.bootify.practica_spring_batch.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClienteDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String direccion;

    @Size(max = 255)
    private String telefono;

    @Size(max = 255)
    private String email;

    private List<Long> cuentas;

}
