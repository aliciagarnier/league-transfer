package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.MatchGoalRecordDTO;
import com.example.demodatabasepj.dtos.MatchRecordDTO;
import com.example.demodatabasepj.enumerator.GoalType;
import com.example.demodatabasepj.models.Match;
import com.example.demodatabasepj.models.MatchGoals;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.service.LeagueService;
import com.example.demodatabasepj.service.MatchGoalService;
import com.example.demodatabasepj.service.MatchService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@Controller
public class MatchController {

    private final MatchService matchService;
    private final LeagueService leagueService;
    private final MatchGoalService matchGoalService;



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

        List<Player> matchPlayers = matchService.getMatchPlayers(
                match.get().getHostTeam().getID_club(), match.get().getGuestTeam().getID_club());

        int hostTeamGoals = matchGoalService.getSpecificClubGoalsInMatch(match_id, match.get().getHostTeam().getID_club());
        int guestTeamGoals = matchGoalService.getSpecificClubGoalsInMatch(match_id, match.get().getGuestTeam().getID_club());

        List<MatchGoals> goals = matchGoalService.getCurrentMatchGoals(match_id);

        ModelAndView mv = new ModelAndView("matchProfile");
        mv.addObject("match", match.get());
        mv.addObject("goalEnum", GoalType.values());
        mv.addObject("players", matchPlayers);
        mv.addObject("hostTeamGoals", hostTeamGoals);
        mv.addObject("guestTeamGoals", guestTeamGoals);
        mv.addObject("goals", goals);
        return mv;
    }


    @PostMapping("/match/{match_id}")
    public ModelAndView addGoal(@PathVariable("match_id") UUID match_id,
                                @Valid MatchGoalRecordDTO matchGoalRecordDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return new ModelAndView("redirect:/match/" + match_id);
        }

        ModelAndView mv = new ModelAndView("redirect:/match/" + match_id);
        matchGoalService.assignGoal(matchGoalRecordDTO);
        return mv;
    }

    @PostMapping("/match/{match_id}/{goal_id}")
    public ModelAndView revokeGoal(@PathVariable("match_id") UUID match_id, @PathVariable("goal_id") UUID goal_id){

        matchGoalService.revokeGoal(match_id, goal_id);
        return new ModelAndView("redirect:/match/"+ match_id);
    }

}
