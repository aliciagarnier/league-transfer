package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ClubRepository extends JpaRepository<Club, UUID> {

    @Query("SELECT club FROM Club club WHERE club.name = ?1")
    Club findClubByName(String name);
}
