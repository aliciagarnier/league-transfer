package com.example.demodatabasepj.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demodatabasepj.models.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query("SELECT player FROM Player player WHERE player.name LIKE %?1%")
        public List<Player> searchAllByName(String name);

    @Query("SELECT COUNT(player) FROM Player player WHERE player.name LIKE %?1%")
        public long countAllByName(String name);

}
