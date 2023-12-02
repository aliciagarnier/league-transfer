package com.example.demodatabasepj.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;


@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name = "match")
public class Match implements Serializable {

    public Match(League league, Club hostTeam, Club guestTeam, Integer hostTeamGoals,
                 Integer guestTeamGoals, LocalDate date) {

        this.league = league;
        this.hostTeam = hostTeam;
        this.guestTeam = guestTeam;
        this.hostTeamGoals = hostTeamGoals;
        this.guestTeamGoals = guestTeamGoals;
        this.date = date;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_league")
    private League league;

    @ManyToOne
    @JoinColumn(name = "id_host_team")
    private Club hostTeam;

    @ManyToOne
    @JoinColumn(name = "id_guest_team")
    private Club guestTeam;

    @Column(name = "host_team_goals")
    private Integer hostTeamGoals;

    @Column(name = "guest_team_goals")
    private Integer guestTeamGoals;


    private LocalDate date;




}
