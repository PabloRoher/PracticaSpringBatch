package io.bootify.practica_spring_batch.model;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CuentaDTO {

    private Long id;

    @Size(max = 255)
    private String numeroCuenta;

    @Size(max = 255)
    private String titularCuenta;

    private Long saldo;

}
