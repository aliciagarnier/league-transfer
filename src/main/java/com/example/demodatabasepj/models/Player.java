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
@Table(name = "Player")
public class Player implements Serializable {

    private static final long serialVersionUid = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private LocalDate birthdate;
    private Position position;
    private Foot foot;
    private final double height;
    private BigDecimal marketValue;
    private String nacionality;






}
