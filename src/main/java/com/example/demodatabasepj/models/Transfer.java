package com.example.demodatabasepj.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder

@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "transfer")
public class Transfer implements Serializable {


    public Transfer (Player player, Club join, Club left, LocalDate date, BigDecimal fee) {
        this.player = player;
        this.join = join;
        this.left = left;
        this.date = date;
        this.fee = fee;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_player")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "id_club_join")
    private Club join;

    @ManyToOne
    @JoinColumn(name = "id_club_left")
    private Club left;

    private LocalDate date;

    private BigDecimal fee;


}
