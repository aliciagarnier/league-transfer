package com.example.demodatabasepj.models.pk;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Embeddable
public class ClubLeaguePK implements Serializable {

    @EqualsAndHashCode.Include
    private UUID club_id;

    @EqualsAndHashCode.Include
    private UUID league_id;

    @EqualsAndHashCode.Include
    private LocalDate date;

}
