package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;


@Repository // querys
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    @Query("SELECT transfer FROM Transfer transfer WHERE transfer.player.name LIKE %?1%" +
            "OR transfer.join.name LIKE %?1%" +
            "OR transfer.left.name LIKE %?1%")
    Page<Transfer> searchAllByPlayerNameOrJoinNameOrLeftName(String keyword, Pageable pageable);

    @Query("SELECT COUNT(transfer) FROM Transfer  transfer WHERE transfer.player.name LIKE %?1%" +
            "OR transfer.join.name LIKE %?1%" +
            "OR transfer.left.name LIKE %?1%")
    Long countAllByPlayerNameOrJoinNameOrLeftName(String keyword);

    //Selecionar a ultima transferencia
    @Query("SELECT MAX(transfer.date) FROM Transfer transfer")
    Optional<LocalDate> findLastTransfer();

}
