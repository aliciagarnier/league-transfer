package com.example.demodatabasepj.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


@Embeddable
public class PlayerClubPK implements Serializable {

    @Column(name = "club_id")
    private UUID club_id;
    @Column(name = "player_id")
    private UUID player_id;
    @Column(name = "date")
    private LocalDate date;


}
