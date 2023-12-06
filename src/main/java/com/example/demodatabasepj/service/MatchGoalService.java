package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.MatchGoalRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.match.GoalNotFoundException;
import com.example.demodatabasepj.exception.match.MatchNotFoundException;
import com.example.demodatabasepj.exception.player.PlayerNotFoundException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.Match;
import com.example.demodatabasepj.models.MatchGoals;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.repository.MatchGoalRepository;
import com.example.demodatabasepj.repository.MatchRepository;
import com.example.demodatabasepj.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@Service
public class MatchGoalService {

    private final MatchRepository matchRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;
    private final MatchGoalRepository matchGoalRepository;





    public MatchGoals assignGoal(MatchGoalRecordDTO matchGoalRecordDTO){
        Optional<Match> match = matchRepository.findById(matchGoalRecordDTO.match_id());
        if (match.isEmpty()){
            throw new MatchNotFoundException("Match not found!");
        }

        Optional<Player> player = playerRepository.findById(matchGoalRecordDTO.player_id());
        if(player.isEmpty()){
            throw new PlayerNotFoundException("Player not found!");
        }

        Optional<Club> club = clubRepository.findById(matchGoalRecordDTO.club_id());

        if(club.isEmpty()){
            throw new ClubDoesNotExistsException("Club not found!");
        }

        if(match.get().getHostTeam().getID_club().equals(club.get().getID_club())){
           matchRepository.updateHostTeamGoals(matchGoalRecordDTO.match_id());
        }else {
            matchRepository.updateGuestTeamGoals(matchGoalRecordDTO.match_id());
        }

        MatchGoals matchGoal = new MatchGoals();
        matchGoal.setMatch(match.get());
        matchGoal.setClub(club.get());
        matchGoal.setPlayer(player.get());
        matchGoal.setType(matchGoalRecordDTO.goalType());

        return matchGoalRepository.save(matchGoal);
    }

    public void revokeGoal(UUID match_id,UUID goal_id){
        if(Objects.isNull(goal_id)){
            throw new GoalNotFoundException("Goal id is null.");
        }
        if(Objects.isNull(match_id)){
            throw new MatchNotFoundException("Match id given is null.");
        }
        Optional<Match> match = matchRepository.findById(match_id);
        if (match.isEmpty()){
            throw new MatchNotFoundException("Match not found!");
        }
        Optional<MatchGoals> goal = matchGoalRepository.findById(goal_id);
        if(goal.isEmpty()){
            throw new GoalNotFoundException("Goal not found!");
        }

        //verifica se o gol foi do time de casa ou convidado;
        if(match.get().getHostTeam().getID_club().equals(goal.get().getClub().getID_club()))
        {
            matchRepository.updateHostTeamGoalsRevoke(match_id);
        }else {
            matchRepository.updateGuestTeamGoalsRevoke(match_id);
        }

        matchGoalRepository.deleteById(goal_id);
    }


    public int getSpecificClubGoalsInMatch(UUID match_id, UUID club_id){
        return matchGoalRepository.countAllByMatchAndClub(match_id, club_id);
    }


    public List<MatchGoals> getCurrentMatchGoals(UUID match_id){
        return matchGoalRepository.getMatchGoalsByMatchId(match_id);
    }

    public int getPlayerTotalGoals(UUID player_id){
        return matchGoalRepository.countAllGoalsByPlayerId(player_id);
    }

    public List<Match> getPlayerScoredMatches(UUID player_id){
        return matchGoalRepository.findMatchesThatThePlayerScored(player_id);
    }

}
