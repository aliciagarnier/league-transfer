package com.example.demodatabasepj.service;


import com.example.demodatabasepj.dtos.ClubRecordDTO;

import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;


import com.example.demodatabasepj.exception.club.DuplicatedClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


import java.math.BigDecimal;
import java.util.Optional;

import java.util.UUID;


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
        Club result = service.addClub(new Club("Clube#1", null, null));
        assertEquals(result.getName(), "Clube#1");
    }


    @Test
    @DisplayName("#addClub > When the input is valid > When the club already exists > Throw exception")
    void addClubDuplicatedClub(){
        Mockito.when(repository.findClubByName("Clube#01")).thenReturn(null);
        Club result = service.addClub(new Club("Clube#01", null, null));
        Mockito.when(repository.findClubByName("Clube#01")).thenReturn(result);
        assertThrows(DuplicatedClubException.class, ()->
                service.addClub(new Club("Clube#01", null, null)));
    }

    @Test
    @DisplayName("#deleteClub > When the club does not exists > Throw exception")
    void deleteClubWhenClubDoesNotExists(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.existsById(id)).thenReturn(Boolean.FALSE);
        assertThrows(ClubDoesNotExistsException.class, ()-> service.deleteClub(id));
    }

    @Test
    @DisplayName("#deleteClub > When the club exists > Delete club")
    void deleteClubWhenClubExists(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.existsById(id)).thenReturn(Boolean.TRUE);
        service.deleteClub(id);
    }

    @Test
    @DisplayName("#updateClub > When the club exists > When the name does not conflict > update club")
    void updateClubWhenClubExistsNameDoesNotConflict(){
        UUID id = new UUID(1L, 1L);
        Club club = new Club(id, "ClubMocked", null, null);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(club));
        Mockito.when(repository.findClubByName("ClubMocked")).thenReturn(club);
        Club updateClub = service.updateClub(id, new ClubRecordDTO("ClubMocked", "Stadium1", null));
        assertAll(
                ()-> assertEquals(updateClub.getName(), "ClubMocked"),
                ()-> assertEquals(updateClub.getStadium(), "Stadium1"),
                ()-> assertNull(updateClub.getMarketValue())
        );
    }

    @Test
    @DisplayName("#updateClub > When the club does not exists > throw exception")
    void updateClubWhenClubDoesNotExistThrowException(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClubDoesNotExistsException.class,
                ()-> service.updateClub(id, new ClubRecordDTO("name", null,null)));
    }

    @Test
    @DisplayName("#updateClub > When the club exists > When the name conflicts > throw exception")
    void updateClubWhenTheClubExistsConflictedNameThrowException(){
        UUID id = new UUID(1L, 1L);
        Club club = new Club(id, "ClubMocked", null, null);
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(club));
        Mockito.when(repository.findClubByName("ClubMocked2"))
                .thenReturn(new Club("ClubMocked2", "Stadium1", null));
        assertThrows(DuplicatedClubException.class,
                ()-> service.updateClub(id, new ClubRecordDTO("ClubMocked2", null, null)));

    }

    @Test
    @DisplayName("#getOneClub > When the club exists > return club")
    void getOneClubWhenTheClubExistsReturnClub(){
        UUID id = new UUID(1L, 1L);
        Club new_club = new Club(id, "ClubMocked", null, null);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(new_club));
        Optional<Club> club = service.getOneClub(id);
        assertEquals("ClubMocked", club.get().getName());
    }

    @Test
    @DisplayName("#countClubsByName > when the keyword is null > count all clubs")
    public void countClubsByNameWhenKeywordIsNullCountAllClubs(){
        Mockito.when(repository.count()).thenReturn(10L);
        Long clubs = service.countClubsByName(null);
        Assertions.assertEquals(10L, clubs);
    }

    @Test
    @DisplayName("#countClubsByName > when the keyword is not null > count clubs by name")
    public void countClubsByNameWhenKeywordIsNotNullCountClubsByName(){
        Mockito.when(repository.countAllByName("FC")).thenReturn(5L);
        Long clubs = service.countClubsByName("FC");
        Assertions.assertEquals(5L, clubs);
    }

    @Test
    @DisplayName("#getCurrentMarketValue > when id is invalid > throw exception")
    public void getCurrentMarketValueWhenIdiSInvalidThrowException(){
        Mockito.when(repository.getCurrentMarketValue(null)).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> service.getClubCurrentMV(null));
    }

    @Test
    @DisplayName("#getCurrentMarketValue > when id is valid > return mv")
    public void getCurrentMarketValueWhenIdIsValidReturnMV(){
        UUID club_id = UUID.randomUUID();
        Mockito.when(repository.getCurrentMarketValue(club_id)).thenReturn(new BigDecimal(10000));
        BigDecimal mv = service.getClubCurrentMV(club_id);
        Assertions.assertEquals(new BigDecimal(10000), mv);
    }
}