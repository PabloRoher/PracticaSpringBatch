package io.bootify.practica_spring_batch.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransaccionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String cuentaID;

    private Long monto;

    @Size(max = 255)
    private String saldoPostTransaccion;

    @NotNull
    private Long cuenta;

    private Long controlLote;


}
