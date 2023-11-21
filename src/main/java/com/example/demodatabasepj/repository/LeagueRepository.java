package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {


    @Query("SELECT league FROM  League league WHERE league.name = ?1")
    League findLeagueByName(String name);
}
