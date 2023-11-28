package com.example.demodatabasepj.controller;

import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.exception.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

//@RestController
@Controller
public class ClubController {

    private final ClubService service;

    @Autowired
    public ClubController(ClubService service){
        this.service = service;
    }


    @PostMapping("/club")
    public ModelAndView saveClub(@Valid ClubRecordDTO clubDTO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return new ModelAndView("redirect:/club");
        }

        Club clubModel = new Club();
        BeanUtils.copyProperties(clubDTO, clubModel);
        try {
            Club new_club = service.addClub(clubModel);
            return new ModelAndView("redirect:/club");


            //return ResponseEntity.status(HttpStatus.CREATED).body(new_club);
        }catch (DuplicatedClubException e){
            return new ModelAndView("redirect:/club/DuplicatedClub");
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Club already exists");
        }
    }

   @DeleteMapping("/club/{id}")
    public ModelAndView deleteClub(@PathVariable(value = "id") UUID id){
        try {
            service.deleteClub(id);
            return new ModelAndView("redirect:/club");
            //return ResponseEntity.status(HttpStatus.OK).body("Club deleted successfully.");
        }catch (ClubDoesNotExistsException e){
            return new ModelAndView("redirect:/club");
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club does not exists");
        }
   }

   @PutMapping("/club/{id}")
    public ModelAndView updateClub(@PathVariable(value = "id") UUID id,
                                   @Valid ClubRecordDTO dto){

        try {
            Club club = service.updateClub(id, dto);
            ModelAndView mv = new ModelAndView("redirect:/club/" + id);
            mv.addObject("club", club);
            return mv;

            //return ResponseEntity.status(HttpStatus.OK).body(club);
        } catch (ClubDoesNotExistsException e){
            return new ModelAndView("redirect:/club");
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Club not found.");
        } catch (DuplicatedClubException e){
            return new ModelAndView("redirect:/club/" + id);
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("update: Club already exists, try another name.");
        }
   }

   @GetMapping("/club/{id}")
    public ModelAndView getOneClub(@PathVariable(value = "id") UUID id){
        Optional<Club> club = service.getOneClub(id);

        if(club.isEmpty()){
           return new ModelAndView("redirect:/club");
        }

        ModelAndView mv = new ModelAndView("clubProfile");
        mv.addObject("club", club.get());
        return mv;
   }

   @GetMapping("/club")
    public ModelAndView getAllClub(@Param("keyword") String keyword){
        ModelAndView mv =  new ModelAndView("club");
        mv.addObject("clubs", service.getAllClubs(keyword));
        mv.addObject("clubQtd", service.countClubsByName(keyword));
        //return ResponseEntity.status(HttpStatus.OK).body(service.getAllClubs());

       return mv;
   }
}
