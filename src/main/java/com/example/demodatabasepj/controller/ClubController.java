package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubController {

    ClubService service;

    @Autowired
    public ClubController(ClubService service){
        this.service = service;
    }


    @PostMapping("/club")
    public ResponseEntity<Club> saveClub(@RequestBody @Valid ClubRecordDTO clubDTO) {
        Club clubModel = new Club();
        BeanUtils.copyProperties(clubDTO, clubModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addClub(
                clubModel.getName(), clubModel.getStadium(), clubModel.getMv()));
    }

}
