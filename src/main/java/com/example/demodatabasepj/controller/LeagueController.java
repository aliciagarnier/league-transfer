package com.example.demodatabasepj.controller;


import com.example.demodatabasepj.dtos.LeagueRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.exception.league.DuplicatedLeagueException;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.service.LeagueService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class LeagueController {

    private final LeagueService leagueService;

    @Autowired
    public LeagueController(LeagueService service){
        this.leagueService = service;
    }

    @PostMapping("/league")
    public ModelAndView saveLeague(@Valid LeagueRecordDTO leagueRecordDTO, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return new ModelAndView("redirect:/league");
        }

        League leagueModel = new League();
        BeanUtils.copyProperties(leagueRecordDTO, leagueModel);

        try {
            League new_league = leagueService.addLeague(leagueModel);
            return new ModelAndView("redirect:/league/"+ new_league.getId());
        } catch (DuplicatedLeagueException e){
            return new ModelAndView("redirect:/league");
        }

        //return ResponseEntity.status(HttpStatus.CREATED).body(leagueService.addLeague(leagueModel));
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
    public ModelAndView getOneLeague(@PathVariable(value = "id")UUID id){
        Optional<League> league = leagueService.getOneLeague(id);

        if (league.isEmpty()){
            return new ModelAndView("redirect:/league");
        }

        ModelAndView mv = new ModelAndView("leagueProfile");
        mv.addObject("league", league.get());
        return mv;
    }

    @GetMapping("/league")
    public ModelAndView getAllLeague(@Param("keyword") String keyword, @Param("region") String region){
        ModelAndView mv =  new ModelAndView("league");
        Page<League> page = leagueService.getAllLeaguesByNameAndRegion(keyword,
                1, "marketValue", "desc", region);


        mv.addObject("leagues", page.getContent());
        mv.addObject("leagueCount", leagueService.countLeagueByName(keyword));
        mv.addObject("currentPage", 1);
        mv.addObject("totalPages", page.getTotalPages());
        mv.addObject("sortField", "marketValue");
        mv.addObject("sortDir", "desc");
        mv.addObject("region", region);
        //return ResponseEntity.status(HttpStatus.OK).body(leagueService.getAllLeagues());
        return mv;
    }

}
