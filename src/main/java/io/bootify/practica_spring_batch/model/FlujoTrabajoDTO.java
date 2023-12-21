package io.bootify.practica_spring_batch.model;

import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FlujoTrabajoDTO {

    private Long id;

    @Size(max = 255)
    private String nombre;

    @Size(max = 255)
    private String paso;

    private Estado estadoPaso;

    private List<Long> lotes;

}
