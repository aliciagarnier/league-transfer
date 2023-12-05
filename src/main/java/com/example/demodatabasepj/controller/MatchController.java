package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.MatchRecordDTO;
import com.example.demodatabasepj.models.Match;
import com.example.demodatabasepj.service.LeagueService;
import com.example.demodatabasepj.service.MatchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@Controller
public class MatchController {

    private final MatchService matchService;
    private final LeagueService leagueService;



    @PostMapping("/league/match/{league_id}")
    public ModelAndView createNewMatchByLeague(@PathVariable ("league_id")UUID league_id,
                                               @Valid MatchRecordDTO matchRecordDTO, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors()){
        return new ModelAndView("redirect:/league/" + league_id);
        }

        Match new_match = matchService.addMatch(matchRecordDTO);


        return new ModelAndView("redirect:/match/" + new_match.getId());
    }

    @GetMapping("/match/{match_id}")
    public ModelAndView getMatch(@PathVariable("match_id") UUID match_id){
        Optional<Match> match = matchService.findMatchById(match_id);

        if (match.isEmpty()){
            return new ModelAndView("redirect:/league");
        }

        ModelAndView mv = new ModelAndView("matchProfile");
        mv.addObject("match", match.get());
        return mv;
    }



}
