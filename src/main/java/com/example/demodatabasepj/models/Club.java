package com.example.demodatabasepj.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "club")
public class Club implements Serializable {

    public Club(String name, String stadium, BigDecimal marketValue)
    {
        this.playerClub = new HashSet<>();
        this.name = name;
        this.stadium = stadium;
        this.marketValue = marketValue;
    }

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "club_id")
    private UUID ID_club;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "stadium", nullable = true)
    private String stadium;

    @Column(name = "market_value", nullable = true)
    private BigDecimal marketValue;

    //Association between them is bidirectional
    @OneToMany(mappedBy = "club")
    private Set<PlayerClub> playerClub;

    @OneToMany
    private Set<Transfer> transfers;

    @OneToMany




}
