package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.LeagueRecordDTO;
import com.example.demodatabasepj.exception.league.DuplicatedLeagueException;
import com.example.demodatabasepj.exception.league.LeagueDoesNotExistsException;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.repository.LeagueRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class LeagueService {

    private final LeagueRepository repository;

    @Autowired
    public LeagueService(LeagueRepository repository){
        this.repository = repository;
    }


    public League addLeague(League league){

        League duplicatedLeague = repository.findLeagueByName(league.getName());
        if(!Objects.isNull(duplicatedLeague)){
            throw new DuplicatedLeagueException("League already exists");
        }
        repository.save(league);
        return league;
    }

    public void deleteLeague(UUID id){
        boolean doesLeagueExists = repository.existsById(id);
        if(!doesLeagueExists){
            throw new LeagueDoesNotExistsException("League does not exists.");
        }
        repository.deleteById(id);
    }

    public League updateLeague(UUID id, LeagueRecordDTO dto){
        Optional<League> league = repository.findById(id);
        if(league.isEmpty()){
            throw new LeagueDoesNotExistsException("Cannot find any club");
        }
        League leagueModel = league.get();
        String oldName = leagueModel.getName();
        BeanUtils.copyProperties(dto, leagueModel);

        if(!oldName.equals(dto.name())){
            League duplicatedLeague = repository.findLeagueByName(leagueModel.getName());
            if (!Objects.isNull(duplicatedLeague)){
                throw new DuplicatedLeagueException("Club already exists, try another name.");
            }
        }
        repository.save(leagueModel);
        return leagueModel;
    }

    public Optional<League> getOneLeague(UUID id){
        return repository.findById(id);
    }

    public List<League> getAllLeagues(){
        return repository.findAll();
    }


}
