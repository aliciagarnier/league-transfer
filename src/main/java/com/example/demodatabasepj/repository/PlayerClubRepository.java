package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.PlayerClub;
import com.example.demodatabasepj.models.pk.PlayerClubPK;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PlayerClubRepository extends JpaRepository<PlayerClub, PlayerClubPK> {

    @Query("SELECT pc FROM PlayerClub pc WHERE pc.playerClubPK.player_id = ?1" +
            " AND pc.playerClubPK.club_id = ?2" +
            " AND pc.date_out IS NULL")
    Optional<PlayerClub> findPlayerClubByClubAndPlayerAndDate_outNull(UUID player_id, UUID club_id);


    @Transactional
    @Modifying
    @Query("UPDATE PlayerClub pc SET pc.date_out = ?3 WHERE pc.playerClubPK.player_id = ?1" +
            " AND pc.playerClubPK.club_id = ?2" +
            " AND pc.date_out IS NULL")
    void updatePlayerClubByDate_out(UUID player_id, UUID club_id, LocalDate date);
}
