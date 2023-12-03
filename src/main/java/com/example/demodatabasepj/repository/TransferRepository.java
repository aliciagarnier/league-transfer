package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT transfer FROM Transfer transfer WHERE transfer.player.name LIKE %?1%" +
            "OR transfer.join.name LIKE %?1%" +
            "OR transfer.left.name LIKE %?1%")
    Page<Transfer> searchAllByPlayerNameOrJoinNameOrLeftName(String keyword, Pageable pageable);

    @Query("SELECT COUNT(transfer) FROM Transfer transfer WHERE transfer.player.name LIKE %?1%" +
            "OR transfer.join.name LIKE %?1%" +
            "OR transfer.left.name LIKE %?1%")
    Long countAllByPlayerNameOrJoinNameOrLeftName(String keyword);

    //Selecionar a ultima transferencia
    @Query("SELECT MAX(transfer.date) FROM Transfer transfer WHERE transfer.player.id = ?1 GROUP BY transfer.player.id")
    Optional<LocalDate> findLastPlayerTransfer(UUID id);

    @Query("SELECT transfer FROM Transfer transfer WHERE transfer.player.id = ?1 " +
            "AND transfer.join.ID_club = ?2 " +
            "AND transfer.left.ID_club = ?3 " +
            "AND transfer.date = ?4")
    Optional<Transfer> findTransferByPlayerDateJoinAndLeftClub (UUID PlayerID, UUID clubJoin, UUID clubOut, LocalDate date);


    @Query("SELECT transfer FROM Transfer transfer WHERE transfer.player.id = ?1 ORDER BY transfer.date DESC")
    List<Transfer> findAllByPlayerId(UUID player_id);


}
