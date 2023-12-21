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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
@Table(name = "FlujoTrabajoes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class FlujoTrabajo {

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
    private String nombre;

    @Column
    private String paso;

    @Column
    @Enumerated(EnumType.STRING)
    private Estado estadoPaso;

    @ManyToMany
    @JoinTable(
            name = "FlujoTrabajoControlLotes",
            joinColumns = @JoinColumn(name = "flujoTrabajoId"),
            inverseJoinColumns = @JoinColumn(name = "controlLoteId")
    )
    private Set<ControlLote> lotes;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
