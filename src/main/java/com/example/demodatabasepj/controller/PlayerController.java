package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.exception.player.InvalidBirthDateException;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.service.PlayerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.boot.Banner;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

@Controller
public class PlayerController {
    private final PlayerService playerService;
    public PlayerController(PlayerService playerService) {

        this.playerService = playerService;
    }
    @PostMapping("/player")
    public ModelAndView savePlayer(@Valid PlayerRecordDTO playerRecordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            ModelAndView mv = new ModelAndView("redirect:/player");
            return mv;
        }
        playerService.addPlayer(playerRecordDTO.name(),
                playerRecordDTO.birthdate(), playerRecordDTO.position(), playerRecordDTO.foot(),
                playerRecordDTO.height(), playerRecordDTO.marketValue(), playerRecordDTO.nacionality());
        return new ModelAndView("redirect:/player");
    }



    @GetMapping("/player")
    public ModelAndView getAllPlayers(@Param("keyword") String keyword)
    {
        ModelAndView mv = new ModelAndView("player");
        mv.addObject("keyword", keyword);
        mv.addObject("players", playerService.findAll(keyword));
        mv.addObject("playerQtd", playerService.countPlayersByName(keyword));
        mv.addObject("posEnum", Position.values());
        return mv;
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
