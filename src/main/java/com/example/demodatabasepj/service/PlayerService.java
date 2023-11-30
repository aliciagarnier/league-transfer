package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.exception.player.InvalidBirthDateException;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.repository.PlayerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    @Autowired
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }


    // Como saber se o player existe?

    public Player addPlayer(String name, LocalDate birthdate, Position position, Foot foot, double height,
                          BigDecimal marketValue, String nacionality) {

        if(Objects.isNull(name) || name.isBlank() || Objects.isNull(marketValue)) {
            throw new InvalidPlayerException("The name and market value must be non-null and non-empty");
        }

        if(marketValue.signum() == -1) {
            throw new InvalidPlayerException("Market value must be positive.");
        }

        if (!Objects.isNull(birthdate) && birthdate.isAfter(LocalDate.now())) {
            throw new InvalidBirthDateException("Invalid birthdate");
        }

        Player new_player = new Player(name, birthdate, position, foot, height, marketValue, nacionality);
        return playerRepository.save(new_player);
    }


    // How can I compare the equality of two players, for example?
    public Player updatePlayer (PlayerRecordDTO playerRecordDTO, Player player) {

        // Insert here validations.

        BeanUtils.copyProperties(playerRecordDTO, player);
        return playerRepository.save(player);
    }

    public List<Player> findAll(String keyword) {
        if(Objects.isNull(keyword)){
            return playerRepository.findAll();
        }
        return playerRepository.searchAllByName(keyword);
    }

    public void delete(Player player) {
        playerRepository.delete(player);
    }

    public Optional<Player> findById(UUID id) {
        return playerRepository.findById(id);
    }


    public long countPlayers(){
        return playerRepository.count();
    }

    public long countPlayersByName(String keyword){
        if(Objects.isNull(keyword)){
            return playerRepository.count();
        }
        return playerRepository.countAllByName(keyword);
    }


}
