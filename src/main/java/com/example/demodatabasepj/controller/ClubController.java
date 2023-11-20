package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.exceptions.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exceptions.club.DuplicatedClubException;
import com.example.demodatabasepj.exceptions.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ClubController {

    ClubService service;

    @Autowired
    public ClubController(ClubService service){
        this.service = service;
    }


    @PostMapping("/club")
    public ResponseEntity<Object> saveClub(@RequestBody @Valid ClubRecordDTO clubDTO) {
        Club clubModel = new Club();
        BeanUtils.copyProperties(clubDTO, clubModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.addClub(
                clubModel.getName(), clubModel.getStadium(), clubModel.getMarketValue()));

        try {
            Club new_club = service.addClub(clubModel.getName(), clubModel.getStadium(), clubModel.getMv());
            return ResponseEntity.status(HttpStatus.CREATED).body(new_club);
        }catch (InvalidClubException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Club");
        }catch (DuplicatedClubException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Club already exists");
        }
    }

   @DeleteMapping("/club/{id}")
    public ResponseEntity<Object> deleteClub(@PathVariable(value = "id") UUID id){
        try {
            service.deleteClub(id);
            return ResponseEntity.status(HttpStatus.OK).body("Club deleted successfully.");
        }catch (ClubDoesNotExistsException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club does not exists");
        }
   }
}
