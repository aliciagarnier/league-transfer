package com.example.demodatabasepj.models;


import com.example.demodatabasepj.enumerator.GoalType;
import jakarta.persistence.*;
import lombok.*;


import java.io.Serializable;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@EqualsAndHashCode(onlyExplicitlyIncluded = true)


@Entity
@Table(name = "match_goals")
public class MatchGoals implements Serializable {

    @Id // id_match, id_player and id_club THATs not a primary key. (eu acho.)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_match")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "id_player")
    private Player player;

    @ManyToOne
    @JoinColumn(name = "id_club")
    private Club club;

    @Column(name = "goal_type")
    private GoalType type;

}
