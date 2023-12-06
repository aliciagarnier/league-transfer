package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Match;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {


    @Query("SELECT match FROM Match match WHERE match.league.id = ?1 ORDER BY match.date DESC LIMIT 20")
    public List<Match> getTopMatchesByDate(UUID league);


    @Query("SELECT match FROM Match match ORDER BY match.date DESC LIMIT 10")
    public List<Match> getMatchByDate();


    @Transactional
    @Modifying
    @Query("UPDATE Match match SET match.hostTeamGoals = match.hostTeamGoals + 1 WHERE match.id = ?1")
    void updateHostTeamGoals(UUID match);


    @Transactional
    @Modifying
    @Query("UPDATE Match match SET match.guestTeamGoals = match.guestTeamGoals + 1 WHERE match.id = ?1")
    void updateGuestTeamGoals(UUID match);


    @Transactional
    @Modifying
    @Query("UPDATE Match match SET match.hostTeamGoals = match.hostTeamGoals - 1 WHERE match.id = ?1")
    void updateHostTeamGoalsRevoke(UUID match);


    @Transactional
    @Modifying
    @Query("UPDATE Match match SET match.guestTeamGoals = match.guestTeamGoals - 1 WHERE match.id = ?1")
    void updateGuestTeamGoalsRevoke(UUID match);




    // Desempenho médio de um clube dentro e fora de casa (poderia ser dos clubes em geral, por exemplo).

    // Desempenho em casa
    @Query("SELECT AVG(match.hostTeamGoals) FROM Match match WHERE match.hostTeam.ID_club = ?1")
    public Double clubPerformanceAsHostTeam(UUID club);

    // Desempenho fora de casa.
    @Query("SELECT AVG(match.guestTeamGoals) FROM Match match WHERE match.guestTeam.ID_club = ?1")
    public Double clubPerformanceAsGuestTeam(UUID club);
}
