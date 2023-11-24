package com.example.demodatabasepj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demodatabasepj.models.Player;
import java.util.UUID;


@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {


}
