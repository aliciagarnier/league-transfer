package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.ClubLeagueDTO;
import com.example.demodatabasepj.exception.clubLeague.DuplicatedAssignmentBetweenClubLeagueException;
import com.example.demodatabasepj.exception.clubLeague.InvalidClubLeagueException;
import com.example.demodatabasepj.exception.clubLeague.InvalidClubLeagueTransferDateException;
import com.example.demodatabasepj.models.*;
import com.example.demodatabasepj.models.pk.ClubLeaguePK;
import com.example.demodatabasepj.repository.ClubLeagueRepository;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.repository.LeagueRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor
@Service
public class ClubLeagueService {

    private final ClubLeagueRepository clubLeagueRepository;
    private final ClubRepository clubRepository;
    private final LeagueRepository leagueRepository;



    public ClubLeague assignClubToLeague(ClubLeagueDTO clubLeagueDTO){
        //apenas permitir associacao liga e clube antes da data atual
        if(clubLeagueDTO.date().isAfter(LocalDate.now())){
            throw new InvalidClubLeagueTransferDateException("Invalid date of assignment!");
        }

        //verificar se transferencia ja ocorreu
        ClubLeaguePK clubLeaguePK = new ClubLeaguePK(clubLeagueDTO.club_id(), clubLeagueDTO.league_id(), clubLeagueDTO.date());
        Optional<ClubLeague> clubLeagueTuple = clubLeagueRepository.findById(clubLeaguePK);
        if(clubLeagueTuple.isPresent()){
            throw new DuplicatedAssignmentBetweenClubLeagueException("Cannot assign club to league twice.");
        }

        //adicionar tupla em clube_league
        Optional<League> league = leagueRepository.findById(clubLeagueDTO.league_id());
        Optional<Club> club = clubRepository.findById(clubLeagueDTO.club_id());
        if(league.isEmpty() || club.isEmpty()){
            throw new InvalidClubLeagueException("Cannot find club or league"); //decompor em 2 exception
        }

        ClubLeague clubLeague = new ClubLeague(clubLeaguePK, club.get(), league.get());


        return clubLeagueRepository.save(clubLeague);
    }


    public List<Club> findCurrentSquadClub(UUID league_id){
        return clubLeagueRepository.findAllClubsByCurrentYear(league_id);
    };

    public Optional<League> findClubCurrentLeague(UUID club_id){
        return clubLeagueRepository.findClubCurrentLeague(club_id);
    }


}
