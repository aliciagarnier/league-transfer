package com.example.demodatabasepj.models;


import com.example.demodatabasepj.models.pk.ClubLeaguePK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor

@Entity
@Table(name = "club_league")
public class ClubLeague implements Serializable {
    @EmbeddedId
    private ClubLeaguePK id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("club_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "club_id")
    private Club club;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("league_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "league_id")
    private League league;


}
