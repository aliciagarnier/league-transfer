package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {



}