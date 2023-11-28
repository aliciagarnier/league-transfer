package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.ClubRecordDTO;

import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.exception.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
public class ClubService {

    private final ClubRepository repository;

    @Autowired
    public ClubService(ClubRepository repository){
        this.repository = repository;
    }

    public Club addClub(Club club){
        Club duplicatedClub = repository.findClubByName(club.getName());

        if (!Objects.isNull(duplicatedClub)){
            throw new DuplicatedClubException("Club already exists.");
        }

        repository.save(club);
        return club;
    }

    public void deleteClub(UUID id){
        boolean doesClubExists = repository.existsById(id);
        if(!doesClubExists){
            throw new ClubDoesNotExistsException("Club with id: " + id + " does not exists");
        }
        repository.deleteById(id);
    }

    public Club updateClub(UUID id, ClubRecordDTO dto){
        Optional<Club> club = repository.findById(id);
        if(club.isEmpty()){
            throw new ClubDoesNotExistsException("Cannot find any club");
        }
        Club clubModel = club.get();
        String oldName = clubModel.getName();
        BeanUtils.copyProperties(dto, clubModel);

        if(!oldName.equals(dto.name())){
            Club duplicatedClub = repository.findClubByName(clubModel.getName());
            if (!Objects.isNull(duplicatedClub)){
                throw new DuplicatedClubException("Club already exists, try another name.");
            }
        }
        repository.save(clubModel);
        return clubModel;
    }


    public Optional<Club> getOneClub(UUID id){
        return repository.findById(id);
    }

    public List<Club> getAllClubs(String keyword){
        if(Objects.isNull(keyword)){
            return repository.findAll();
        }
        return repository.searchAllByName(keyword);
    }

    public Long countClubsByName(String keyword){
        if(Objects.isNull(keyword)){
            return repository.count();
        }
        return repository.countAllByName(keyword);
    }
}
