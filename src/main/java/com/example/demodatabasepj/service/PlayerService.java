package com.example.demodatabasepj.service;

import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.repository.PlayerRepository;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player addPlayer(@NotNull @NotBlank String name, LocalDate birthdate, Position position, Foot foot, double height,
                            @NotNull @PositiveOrZero BigDecimal marketValue, String nacionality) {

        if (birthdate.isAfter(LocalDate.now())) {
            throw new RuntimeException("Invalid birthdate");
        }

        Player new_player = new Player(name, birthdate, position, foot, height, marketValue, nacionality);
        playerRepository.save(new_player);
        return new_player;
    }

    public List<Player> findAll() {
        return playerRepository.findAll();
    }
    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Player findById(UUID id) {

        return playerRepository.getReferenceById(id);
    }

}
