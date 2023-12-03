package com.example.demodatabasepj.models;


import com.example.demodatabasepj.models.pk.PlayerClubPK;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

 @OnDelete(action = OnDeleteAction.CASCADE)
 @MapsId("club_id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "club_id")
    private Club club;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @MapsId("player_id")
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "date_out")
    private LocalDate date_out;

}
