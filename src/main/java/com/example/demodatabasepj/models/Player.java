package com.example.demodatabasepj.models;

import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "player")
public class Player implements Serializable {

    private static final long serialVersionUid = 1L;
    public Player (String name, LocalDate birthdate, Position position, Foot foot,
                   double height, BigDecimal marketValue, String nacionality)
    {
        this.name = name;
        this.birthdate = birthdate;
        this.position = position;
        this.foot = foot;
        this.height = height;
        this.marketValue = marketValue;
        this.nacionality = nacionality;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "player_id")
    private UUID id;

    @NotNull
    @Column(name= "name")
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name= "birth_date")
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    @Column(name="position")
    private Position position;

    @Column(name="foot")
    private Foot foot;

    @Column(name="height")
    private double height;

    @NotNull
    @Column(name="market_value")
    private BigDecimal marketValue;

    @Column(name="nacionality")
    private String nacionality;

    @OneToMany(mappedBy = "player")
    private Set<PlayerClub> playerClub;






}
