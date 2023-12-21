package io.bootify.practica_spring_batch.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ControlLoteDTO {

    private Long id;
    private Estado estado;
    private Long registrosProcesados;
    private Long ultimoRegistroProcesadoID;

}
