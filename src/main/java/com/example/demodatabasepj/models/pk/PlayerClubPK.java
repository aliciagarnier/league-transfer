package com.example.demodatabasepj.models.pk;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


@Embeddable
public class PlayerClubPK implements Serializable {

    @EqualsAndHashCode.Include
    @Column(name = "club_id")
    private UUID club_id;

    @EqualsAndHashCode.Include
    @Column(name = "player_id")
    private UUID player_id;

    @EqualsAndHashCode.Include
    @Column(name = "date")
    private LocalDate date;


}
