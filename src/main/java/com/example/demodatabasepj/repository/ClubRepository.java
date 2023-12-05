package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {

    @Query("SELECT club FROM Club club WHERE club.name = ?1")
    Club findClubByName(String name);

    @Query("SELECT club FROM Club club WHERE club.name LIKE %?1%")
    Page<Club> searchAllByName(String name, Pageable pageable);

    @Query("SELECT COUNT(club) FROM Club club WHERE club.name LIKE %?1%")
    long countAllByName(String name);

    @Query("SELECT SUM(pc.player.marketValue) FROM PlayerClub pc WHERE pc.club.ID_club = ?1")
    BigDecimal getCurrentMarketValue(UUID club_id);

}
