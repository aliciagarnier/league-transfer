package com.example.demodatabasepj.club.service;

import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.exceptions.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exceptions.club.DuplicatedClubException;
import com.example.demodatabasepj.exceptions.club.InvalidClubException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.service.ClubService;
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
   @DisplayName("#addClub > When the input is invalid > Throw exception")
   void addClubInvalidInputThrowException(){
        assertAll(
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(new Club())),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(new Club("", null, null))),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(new Club("", "Stadium#1", new BigDecimal(100)))),
                () -> assertThrows(InvalidClubException.class, ()->
                        service.addClub(new Club(null, "Stadium#1", new BigDecimal(100))))
        );
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
                ()-> assertNull(updateClub.getMv())
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

}
