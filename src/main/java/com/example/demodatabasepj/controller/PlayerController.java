package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.exception.player.InvalidBirthDateException;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.service.PlayerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
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

    @Autowired
    public PlayerController(PlayerService playerService) {

        this.playerService = playerService;
    }
    @PostMapping("/player")
    public ModelAndView savePlayer(@Valid PlayerRecordDTO playerRecordDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/player");
        }
        Player player = playerService.addPlayer(playerRecordDTO.name(),
                playerRecordDTO.birthdate(), playerRecordDTO.position(), playerRecordDTO.foot(),
                playerRecordDTO.height(), playerRecordDTO.marketValue(), playerRecordDTO.nacionality());
        return new ModelAndView("redirect:/player/" + player.getId());
    }



    @GetMapping("/player")
    public ModelAndView getAllPlayers(@Param("keyword") String keyword)
    {
        ModelAndView mv = new ModelAndView("player");
        Page<Player> page = playerService.findAll(keyword, 1,"marketValue", "desc");


        mv.addObject("keyword", keyword);
        mv.addObject("players", page.getContent());
        mv.addObject("playerQtd", playerService.countPlayersByName(keyword));
        mv.addObject("posEnum", Position.values());
        mv.addObject("currentPage", 1);
        mv.addObject("totalPages", page.getTotalPages());
        mv.addObject("sortField", "marketValue");
        mv.addObject("sortDir", "desc");
        return mv;
    }

    @GetMapping("/player/page/{pageNumber}")
    public ModelAndView getAllPlayers(@Param("keyword") String keyword, @Param("sortField") String sortField,
                                      @Param("sortDir") String sortDir, @PathVariable("pageNumber") int currentPage)
    {
        ModelAndView mv = new ModelAndView("player");
        Page<Player> page = playerService.findAll(keyword, currentPage, sortField, sortDir);


        mv.addObject("keyword", keyword);
        mv.addObject("players", page.getContent());
        mv.addObject("playerQtd", playerService.countPlayersByName(keyword));
        mv.addObject("posEnum", Position.values());
        mv.addObject("currentPage", currentPage);
        mv.addObject("totalPages", page.getTotalPages());
        mv.addObject("sortField", sortField);
        mv.addObject("sortDir", sortDir);
        return mv;
    }

    @GetMapping("/player/{id}")
    public ModelAndView getOnePlayer(@PathVariable(value="id") UUID id) {
        Optional<Player> player = playerService.findById(id);
        Optional<Club> currentClubOptional = playerService.getCurrentClub(id);

        if(player.isEmpty())
        {
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
            return new ModelAndView("redirect:/player");
        }
         //return ResponseEntity.status(HttpStatus.OK).body(player.get());
        ModelAndView mv = new ModelAndView("playerProfile");
        mv.addObject("posEnum", Position.values());
        mv.addObject("player", player.get());
        mv.addObject("currentDate", LocalDate.now());
        currentClubOptional.ifPresent(club -> mv.addObject("currentClub", club));

        return mv;



    }
    @PutMapping("/player/{id}")
    public ModelAndView updatePlayer(@PathVariable(value = "id") UUID id,
                                                 @Valid PlayerRecordDTO playerRecordDTO) {
        Optional<Player> optionalPlayer = playerService.findById(id);

        if(optionalPlayer.isPresent())
        {
            ModelAndView mv = new ModelAndView("/player/" + id);
            mv.addObject("player", playerService.updatePlayer(playerRecordDTO, optionalPlayer.get()));
            //return ResponseEntity.status(HttpStatus.OK).body(playerService
            // updatePlayer(playerRecordDTO, optionalPlayer.get()));
        }

        return new ModelAndView("redirect:/player/" + id);
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
    }

    @DeleteMapping("/player/{id}")
    public ModelAndView deletePlayer(@PathVariable(value="id") UUID id) {
        Optional<Player> optionalPlayer = playerService.findById(id);

        if(optionalPlayer.isPresent()) {
            ModelAndView mv = new ModelAndView("redirect:/player");
            playerService.delete(optionalPlayer.get());
            return mv;
            //return ResponseEntity.status(HttpStatus.OK).body("Player deleted successfully");
        }

        return new ModelAndView("redirect:/player/"+ id);
        //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Player not found.");
    }





}
