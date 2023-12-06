package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Match;
import com.example.demodatabasepj.models.MatchGoals;
import com.example.demodatabasepj.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatchGoalRepository extends JpaRepository<MatchGoals, UUID> {

    @Query("SELECT COUNT(mg.id) FROM MatchGoals mg WHERE mg.match.id = ?1 AND mg.club.ID_club = ?2")
    int countAllByMatchAndClub(UUID match, UUID club);
    @Query("SELECT mg FROM MatchGoals mg WHERE mg.match.id = ?1")
    List<MatchGoals> getMatchGoalsByMatchId(UUID match_id);

    // Encontrar todas as partidas que um determinado jogador realizou um gol
    @Query("SELECT match FROM Match match JOIN MatchGoals matchgoals ON match.id = matchgoals.match.id " +
            "WHERE matchgoals.player.id = ?1")
    public List<Match>findMatchesThatThePlayerScored(UUID player);

    // Jogadores que marcaram pelo menos um gol.
    @Query("SELECT player FROM Player player WHERE player.id = ANY " +
            "(SELECT matchgoals.player.id FROM MatchGoals matchgoals " +
            " WHERE matchgoals.type <> 'CONTRA')")
    Optional<List<Player>> playersWhoScoredAnyGoal();


}
