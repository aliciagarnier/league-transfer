package com.example.demodatabasepj.club.service;

import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import static org.junit.jupiter.api.Assertions.*;



public class ClubServiceTest
{

    ClubService service;

    @BeforeEach
    void setup(){
        service = new ClubService();
    }


   @Test
   @DisplayName("#addClub > When the input is valid > When the club does not exists > return added Club")
   void addClubInputValidUniqueClub(){
        Club result = service.addClub("Clube#1");
        assertEquals(result.getName(), "Clube#1");

   }


}
