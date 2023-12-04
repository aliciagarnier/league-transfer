package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.ClubRecordDTO;

import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.repository.ClubRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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

    public Optional<Club> getOneClub(UUID id) {
        return repository.findById(id);
    }

    public Boolean existClub(UUID id) {
        if(getOneClub(id).isPresent())
        {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public Boolean existClub(Optional<Club> optional) {
        if (optional.isPresent())
        {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Page<Club> getAllClubs(String keyword, int pageNumber, String sortField, String sortDir){
        /*if(Objects.isNull(sortDir) || Objects.isNull(sortField) || Objects.isNull(keyword)){
            Sort sort = Sort.by("marketValue").descending();
            Pageable pageable = PageRequest.of(pageNumber - 1 , 8, sort);
            return repository.findAll(pageable);
        }*/
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1 , 8, sort);

        if (Objects.isNull(keyword)){
            return repository.findAll(pageable);
        }

        return repository.searchAllByName(keyword, pageable);
    }

    public Page<Club> getAllClubsByName(String keyword, int pageNumber, String sortField, String sortDir){
        /*if(Objects.isNull(sortDir) || Objects.isNull(sortField) || Objects.isNull(keyword)){
            Sort sort = Sort.by("marketValue").descending();
            Pageable pageable = PageRequest.of(pageNumber - 1 , 8, sort);
            return repository.findAll(pageable);
        }*/
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1 , 6, sort);

        return repository.searchAllByName(keyword, pageable);
    }


    public Long countClubsByName(String keyword){ // versatil
        if(Objects.isNull(keyword)){
            return repository.count();
        }
        return repository.countAllByName(keyword);
    }


}
