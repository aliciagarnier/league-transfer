package com.example.demodatabasepj.models;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.UUID;
import java.util.Set;
import java.io.Serializable;


@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor


@EqualsAndHashCode(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "matches")
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

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "id_league")
    private League league;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "id_host_team")
    private Club hostTeam;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne
    @JoinColumn(name = "id_guest_team")
    private Club guestTeam;

    @Column(name = "host_team_goals")
    private Integer hostTeamGoals;

    @Column(name = "guest_team_goals")
    private Integer guestTeamGoals;

    @Column(name = "match_date")
    private LocalDate date;

    // all goals that occured in the match
    @OneToMany(mappedBy = "match")
    Set<MatchGoals> goals;




}
