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

    public Club(UUID id, String name, String stadium, BigDecimal marketValue){
        this.name = name;
        this.stadium = stadium;
        this.marketValue = marketValue;
        this.ID_club = id;
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

    @Column(name = "market_value")
    private BigDecimal marketValue = BigDecimal.ZERO;

    // Association between them is bidirectional.
    @OneToMany(mappedBy = "club")
    private Set<PlayerClub> playerClub;

    // all transfers that one player join this club.
    @OneToMany(mappedBy = "join")
    private Set<Transfer> joinTransfers;

    //all transfers that one player left this club.
    @OneToMany(mappedBy = "left")
    private Set<Transfer> leftTransfers;

    // all matches that this club was a host team.
    @OneToMany(mappedBy = "hostTeam")
    private Set<Match> hostMatches;

    // all matches that this club was a guest team.
    @OneToMany(mappedBy = "guestTeam")
    private Set<Match> guestMatches;

    // all goals that a player on the club scored.
    @OneToMany(mappedBy = "club")
    private Set<MatchGoals> goals;




}
