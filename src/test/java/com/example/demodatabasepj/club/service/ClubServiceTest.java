package com.example.demodatabasepj.club.service;

import com.example.demodatabasepj.exceptions.club.DuplicatedClubException;
import com.example.demodatabasepj.exceptions.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;



public class ClubServiceTest
{

    @InjectMocks
    private ClubService service;

    @Mock
    private ClubRepository repository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


   @Test
   @DisplayName("#addClub > When the input is valid > When the club does not exists > return added Club")
   void addClubInputValidUniqueClub(){
        Mockito.when(repository.findClubByName("Clube#1")).thenReturn(null);
        Club result = service.addClub("Clube#1", null, null);
        assertEquals(result.getName(), "Clube#1");
   }

   @Test
   @DisplayName("#addClub > When the input is invalid > Throw exception")
   void addClubInvalidInputThrowException(){
        assertAll(
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(null, null, null)),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub("", null, null)),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub("", "Stadium#1", new BigDecimal(100))),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(null, "Stadium#1", new BigDecimal(100)))
        );
   }

   @Test
   @DisplayName("#addClub > When the input is valid > When the club already exists > Throw exception")
   void addClubDuplicatedClub(){
        Mockito.when(repository.findClubByName("Clube#01")).thenReturn(null);
        Club result = service.addClub("Clube#01", null, null);
        Mockito.when(repository.findClubByName("Clube#01")).thenReturn(result);
        assertThrows(DuplicatedClubException.class, ()->
                service.addClub("Clube#01", null, null));
   }

}
