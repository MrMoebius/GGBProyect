package org.davide.ggbproyect.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "juegos_copias")
@Data
public class JuegosCopia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_copia", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_juego", nullable = false)
    private Juego idJuego;

    @Size(max = 50)
    @Column(name = "codigo_interno", length = 50)
    private String codigoInterno;

    @Size(max = 30)
    @ColumnDefault("'DISPONIBLE'")
    @Column(name = "estado", length = 30)
    private String estado;

}