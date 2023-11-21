package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.dtos.LeagueRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.service.LeagueService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class LeagueController {

    private final LeagueService leagueService;

    public LeagueController(LeagueService service){
        this.leagueService = service;
    }

    @PostMapping("/league")
    public ResponseEntity<Object> saveLeague(@RequestBody @Valid LeagueRecordDTO leagueRecordDTO){
        League leagueModel = new League();
        BeanUtils.copyProperties(leagueRecordDTO, leagueModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.addLeague(leagueModel));
    }

    @DeleteMapping("/league/{id}")
    public ResponseEntity<Object> deleteLeague(@PathVariable(value = "id")UUID id){
        try {
            leagueService.deleteLeague(id);
            return ResponseEntity.status(HttpStatus.OK).body("League deleted successfully.");
        }catch (ClubDoesNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("League does not exists");
        }
    }

    @PutMapping("/league/{id}")
    public ResponseEntity<Object> updateLeague(@PathVariable(value = "id") UUID id,
                                               @RequestBody @Valid LeagueRecordDTO leagueDto){
        try {
            League league = leagueService.updateLeague(id, leagueDto);
            return ResponseEntity.status(HttpStatus.OK).body(league);
        } catch (ClubDoesNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("League not found.");
        } catch (DuplicatedClubException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("update: League already exists, try another name.");
        }
    }

    @GetMapping("/league/{id}")
    public ResponseEntity<Object> getOneLeague(@PathVariable(value = "id")UUID id){
        try {
            Optional<League> league = leagueService.getOneLeague(id);
            return ResponseEntity.status(HttpStatus.OK).body(league);
        } catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("League not found");
        }
    }

    @GetMapping("/league")
    public ResponseEntity<List<League>> getAllLeague(){
        return ResponseEntity.status(HttpStatus.OK).body(leagueService.getAllLeagues());
    }

}
