package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
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

}
