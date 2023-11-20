package com.example.demodatabasepj.service;


import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.exception.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demodatabasepj.exceptions.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exceptions.club.DuplicatedClubException;
import com.example.demodatabasepj.exceptions.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import java.util.Objects;
import java.util.UUID;


@Service
public class ClubService {

    private final ClubRepository repository;

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
            throw new DuplicatedClubException("Club already exists.");
        }

        Club new_club = new Club(name, stadium, marketValue);
        repository.save(new_club);
        return new_club;
    }

    public void deleteClub(UUID id){
        boolean doesClubExists = repository.existsById(id);
        if(!doesClubExists){
            throw new ClubDoesNotExistsException("Club with id: " + id + " does not exists");
        }
        repository.deleteById(id);
    }
}
