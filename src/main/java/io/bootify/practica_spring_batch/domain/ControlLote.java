package io.bootify.practica_spring_batch.domain;

import io.bootify.practica_spring_batch.model.Estado;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "ControlLotes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class ControlLote {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column
    private Long registrosProcesados;

    @Column
    private Long ultimoRegistroProcesadoID;

    @OneToMany(mappedBy = "controlLote")
    private Set<Transaccion> transacciones;

    @OneToMany(mappedBy = "controlLote")
    private Set<AuditoriaTransaccion> auditorias;

    @ManyToMany(mappedBy = "lotes")
    private Set<FlujoTrabajo> flujosTrabajo;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
