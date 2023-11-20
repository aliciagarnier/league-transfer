package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
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
    public ResponseEntity<Player> savePlayer(@RequestBody @Valid PlayerRecordDTO playerRecordDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(playerService.addPlayer(playerRecordDTO.name(),
                playerRecordDTO.birthdate(),playerRecordDTO.position(), playerRecordDTO.foot(),
                playerRecordDTO.height(), playerRecordDTO.marketValue(), playerRecordDTO.nacionality()));
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





}
