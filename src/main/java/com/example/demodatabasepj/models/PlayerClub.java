package com.example.demodatabasepj.models;


import com.example.demodatabasepj.models.pk.PlayerClubPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "player_club")
public class PlayerClub implements Serializable {

   @EmbeddedId
   private PlayerClubPK playerClubPK; // Primary key.

    @MapsId("club_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "club_id")
    private Club club;


    @MapsId("player_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player player;



}
