package com.example.demodatabasepj.service;

import com.example.demodatabasepj.exceptions.club.DuplicatedClubException;
import com.example.demodatabasepj.exceptions.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.Objects;


@Service
public class ClubService {

    private ClubRepository repository;

    @Autowired
    public ClubService(ClubRepository repository){
        this.repository = repository;
    }
    public Club addClub(String name, String stadium, BigDecimal marketValue){

        if(Objects.isNull(name) || name.isEmpty()){
           throw new InvalidClubException("Club name must be valid");
        }
        Club duplicatedClub = repository.findClubByName(name);

        if (!Objects.isNull(duplicatedClub)){
            throw new DuplicatedClubException("Club already exists");
        }

        Club new_club = new Club(name, stadium, marketValue);
        repository.save(new_club);
        return new_club;
    }

    public boolean deleteClub(String name){
        return Boolean.FALSE;
    }
}
