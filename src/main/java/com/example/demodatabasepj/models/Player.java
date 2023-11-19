package com.example.demodatabasepj.models;

import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import lombok.*;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@AllArgsConstructor

@Entity
@Table(name = "player")
public class Player implements Serializable {

    private static final long serialVersionUid = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name= "name")
    private String name;

    @Column(name= "birth_date")
    private LocalDate birthdate;

    @Column(name="position")
    private Position position;

    @Column(name="foot")
    private Foot foot;

    @Column()
    private final double height;

    @Column(name="market_value")
    private BigDecimal marketValue;

    @Column(name="nacionality")
    private String nacionality;






}
