package com.example.demodatabasepj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "club")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID ID_club;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "stadium", nullable = true)
    private String stadium;

    @Column(name = "market_value", nullable = true)
    private BigDecimal marketValue;


    public Club(String name, String stadium, BigDecimal marketValue){
        this.name = name;
        this.stadium = stadium;
        this.marketValue = marketValue;
    }

}
