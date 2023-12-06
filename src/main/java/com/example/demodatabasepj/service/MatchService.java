package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.MatchGoalRecordDTO;
import com.example.demodatabasepj.dtos.MatchRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.league.LeagueDoesNotExistsException;
import com.example.demodatabasepj.exception.match.InvalidMatchDateException;
import com.example.demodatabasepj.exception.match.InvalidScoreException;
import com.example.demodatabasepj.models.*;
import com.example.demodatabasepj.repository.MatchRepository;
import com.example.demodatabasepj.repository.PlayerClubRepository;
import com.example.demodatabasepj.repository.PlayerRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor

@Service
public class MatchService {

    private final MatchRepository repository;

    private final LeagueService leagueService;

    private final ClubService clubService;
    private final PlayerClubRepository playerClubRepository;


    public Match addMatch (MatchRecordDTO matchRecordDTO) {

        // reduzir os ifs e ficar mais entendível.
        UUID leagueID = matchRecordDTO.leagueID();
        UUID hostTeamID = matchRecordDTO.hostTeamID();
        UUID guestTeamID = matchRecordDTO.guestTeamID();
        Integer hostTeamGoals = matchRecordDTO.hostTeamGoals();
        Integer guestTeamGoals = matchRecordDTO.guestTeamGoals();
        LocalDate date = matchRecordDTO.date();


        if (Objects.isNull(leagueID) || Objects.isNull(hostTeamID) || Objects.isNull(guestTeamID))
        {
            throw new IllegalArgumentException("");
        }

        if (!clubService.existClub(hostTeamID) || !clubService.existClub(guestTeamID)) {
            throw new ClubDoesNotExistsException("Both clubs must exist for the match be declared.");
        }

        if(!leagueService.existLeague(leagueID))
        {
            throw new LeagueDoesNotExistsException("The league must exist for the match be declared.");
        }

        if (hostTeamID.equals(guestTeamID))
        {
            throw new IllegalArgumentException("The host and guest team are equals.");
        }

        // Se tiver a opção de agendar uma partida por exemplo aqui vai ter que ter validação se é null ou não.
        if (Integer.signum(hostTeamGoals) == -1 || Integer.signum(guestTeamGoals) == -1)
        {
            throw new InvalidScoreException("The match score must be positive or zero.");
        }

        if(!Objects.isNull(date) && date.isAfter(LocalDate.now())) // Na verdade não acredito que isso seja necessário,
            // já que as partidas são agendadas, se o sistema permitir isso, por exemplo.
        {
            throw new InvalidMatchDateException("The match must have already happened.");
        }

        // Aqui também estou permitindo uma data nula. Caso não seja possível basta substituir (adaptar o if).

        Match match = new Match(leagueService.getOneLeague(leagueID).get(), clubService.getOneClub(hostTeamID).get(),
                clubService.getOneClub(guestTeamID).get(), hostTeamGoals, guestTeamGoals, date);

         return repository.save(match);

    }

    public Optional<Match> findMatchById(UUID id) { return repository.findById(id); }


    public List<Match> getLatestMatchesFromLeague(UUID league_id){
        return repository.getTopMatchesByDate(league_id);
    }

    public List<Match> getAllLatestMatches(){
        return repository.getMatchByDate();
    }


    public List<Player> getMatchPlayers(UUID clubHost_id, UUID clubGuest_id){
        return playerClubRepository.findAllByTwoClubsAndDate_outNull(clubHost_id, clubGuest_id);
    }
}
