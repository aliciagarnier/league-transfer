package com.example.demodatabasepj.repository;


import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.ClubLeague;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.models.pk.ClubLeaguePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClubLeagueRepository  extends JpaRepository<ClubLeague, ClubLeaguePK> {


    @Query("SELECT cl.club FROM ClubLeague cl WHERE YEAR(cl.id.date) = YEAR(CURRENT DATE) AND cl.id.league_id = ?1")
    List<Club> findAllClubsByCurrentYear(UUID league_id);


    @Query("SELECT cl.league FROM ClubLeague cl WHERE YEAR(cl.id.date) = YEAR(CURRENT DATE) AND cl.club.ID_club = ?1")
    Optional<League> findClubCurrentLeague(UUID club_id);
}
