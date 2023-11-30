package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.League;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface LeagueRepository extends JpaRepository<League, UUID> {


    @Query("SELECT league FROM  League league WHERE league.name = ?1")
    League findLeagueByName(String name);

    @Query("SELECT league FROM League league WHERE league.name LIKE %?1% AND league.region LIKE %?2%")
    Page<League> searchAllByNameAndRegion(String name,String region, Pageable pageable);

    @Query("SELECT league FROM League league WHERE league.region LIKE %?1%")
    Page<League> findAllByRegion(String region, Pageable pageable);
    @Query("SELECT COUNT(league) FROM League league WHERE league.name LIKE %?1%")
    Long countAllByName(String name);
}
