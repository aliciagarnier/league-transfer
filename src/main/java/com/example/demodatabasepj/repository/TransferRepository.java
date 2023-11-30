package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository // querys
public interface TransferRepository extends JpaRepository<Transfer, UUID> {
}
