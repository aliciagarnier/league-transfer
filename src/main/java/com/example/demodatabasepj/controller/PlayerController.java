package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
import com.example.demodatabasepj.exception.player.InvalidBirthDateException;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.service.PlayerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class PlayerController {
    private final PlayerService playerService;
    public PlayerController(PlayerService playerService) {

        this.playerService = playerService;
    }
    @PostMapping("/player")
    public ResponseEntity<Object> savePlayer(@RequestBody @Valid PlayerRecordDTO playerRecordDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(playerService.addPlayer(playerRecordDTO.name(),
                    playerRecordDTO.birthdate(), playerRecordDTO.position(), playerRecordDTO.foot(),
                    playerRecordDTO.height(), playerRecordDTO.marketValue(), playerRecordDTO.nacionality()));

        } catch (InvalidPlayerException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid player");
        } catch (InvalidBirthDateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The given birthdate is invalid.");
        }

    }
    @GetMapping("/player")
    public ResponseEntity<List<Player>> getAllPlayers () {
        return ResponseEntity.status(HttpStatus.OK).body(playerService.findAll());
    }

    @GetMapping("/player/{id}")
    public ResponseEntity<Object> getOnePlayer(@PathVariable(value="id") UUID id) {
        Optional<Player> player = playerService.findById(id);
            if(player.isEmpty())
            {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
            }

         return ResponseEntity.status(HttpStatus.OK).body(player.get());
    }
    @PutMapping("/player/{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid PlayerRecordDTO playerRecordDTO) {
        Optional<Player> optionalPlayer = playerService.findById(id);

        if(optionalPlayer.isPresent())
        {
            return ResponseEntity.status(HttpStatus.OK).body(playerService.
                    updatePlayer(playerRecordDTO, optionalPlayer.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
    }

    @DeleteMapping("/player/{id}")
    public ResponseEntity<Object> deletePlayer(@PathVariable(value="id") UUID id) {
        Optional<Player> optionalPlayer = playerService.findById(id);

        if(optionalPlayer.isPresent()) {
            playerService.delete(optionalPlayer.get());
            return ResponseEntity.status(HttpStatus.OK).body("Player deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
    }





}
