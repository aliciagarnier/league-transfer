package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.MatchGoals;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MatchGoalRepository extends JpaRepository<MatchGoals, UUID> {


    @Query("SELECT COUNT(mg.id) FROM MatchGoals mg WHERE mg.match.id = ?1 AND mg.club.ID_club = ?2")
    int countAllByMatchAndClub(UUID match, UUID club);


    @Query("SELECT mg FROM MatchGoals mg WHERE mg.match.id = ?1")
    List<MatchGoals> getMatchGoalsByMatchId(UUID match_id);
}
