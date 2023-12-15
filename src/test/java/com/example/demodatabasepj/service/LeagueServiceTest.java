package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.LeagueRecordDTO;
import com.example.demodatabasepj.exception.league.DuplicatedLeagueException;
import com.example.demodatabasepj.exception.league.LeagueDoesNotExistsException;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.repository.LeagueRepository;
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

public class LeagueServiceTest {

    @InjectMocks
    private LeagueService service;


    @Mock
    private LeagueRepository repository;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);

    }


    @Test
    @DisplayName("#addLeague > when the league already is exists > throw exception")
    public void addLeagueWhenLeagueAlreadyExistsThrowError(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findLeagueByName("Liga"))
                .thenReturn(new League(id, "Liga", "Brazil", "Americas",  new BigDecimal(0)));

        LeagueRecordDTO dto = new LeagueRecordDTO("Liga", "Brazil", "Americas");
        Assertions.assertThrows(DuplicatedLeagueException.class, ()-> service.addLeague(dto));
    }



    @Test
    @DisplayName("#addLeague > when the league does not exists > save league > return saved league")
    public void addLeagueWhenTheLeagueDoesNotExistsReturnLeague(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findLeagueByName("Liga")).thenReturn(null);
        LeagueRecordDTO dto = new LeagueRecordDTO("Liga", "Brazil", "Americas");
        League league = service.addLeague(dto);
        Assertions.assertAll(
                ()-> Assertions.assertEquals("Liga", league.getName()),
                ()-> Assertions.assertEquals("Brazil", league.getCountry()),
                ()-> Assertions.assertEquals("Americas", league.getRegion())
        );
    }


    @Test
    @DisplayName("#deleteLeague > when the league does not exists > throw an exception")
    public void deleteLeagueWhenTheLeagueDoesNotExistsThrowException(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.existsById(id)).thenReturn(Boolean.FALSE);
        Assertions.assertThrows(LeagueDoesNotExistsException.class, ()-> service.deleteLeague(id));
    }

    @Test
    @DisplayName("#deleteLeague > when the league exists > delete league")
    public void deleteLeagueWhenTheLeagueExists(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.existsById(id)).thenReturn(Boolean.TRUE);
        Assertions.assertDoesNotThrow(()-> service.deleteLeague(id));
    }

    @Test
    @DisplayName("#updateLeague > when the league does not exists > throw an exception")
    public void updateLeagueWhenTheLeagueDoesNotExistsThrowError(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        LeagueRecordDTO dto = new LeagueRecordDTO("LigaUpt", "BrazilUpt", "AmericasUpt");
        Assertions.assertThrows(LeagueDoesNotExistsException.class, ()-> service.updateLeague(id, dto));
    }


    @Test
    @DisplayName("#updateLeague > when the league name is already in use > throw an exception")
    public void updateLeagueWhenTheLeagueNameAlreadyInUseThrowError(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(
                new League(id, "Liga", "Brazil", "Americas", new BigDecimal(0))));
        Mockito.when(repository.findLeagueByName("LigaUpt")).thenReturn(new League());
        LeagueRecordDTO dto = new LeagueRecordDTO("LigaUpt", "BrazilUpt", "AmericasUpt");
        Assertions.assertThrows(DuplicatedLeagueException.class, ()-> service.updateLeague(id, dto));
    }


    @Test
    @DisplayName("#updateLeague > when the league exists >when the league name is not in use > update league")
    public void updateLeagueWhenTheLeagueExistsNameNotInUse(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(
                new League(id, "Liga", "Brazil", "Americas", new BigDecimal(0))));
        Mockito.when(repository.findLeagueByName("LigaUpt")).thenReturn(null);
        LeagueRecordDTO dto = new LeagueRecordDTO("LigaUpt", "BrazilUpt", "AmericasUpt");
        League updatedLeague = service.updateLeague(id, dto);
        Assertions.assertAll(
                () -> Assertions.assertEquals("LigaUpt", updatedLeague.getName()),
                () -> Assertions.assertEquals("BrazilUpt", updatedLeague.getCountry()),
                () -> Assertions.assertEquals("AmericasUpt", updatedLeague.getRegion()),
                () -> Assertions.assertEquals(id, updatedLeague.getId())
        );
    }

    @Test
    @DisplayName("#getOneLeague > when the league exists > return the league")
    public void getOneLeagueWhenTheLeagueExistsReturnLeague(){
        UUID id = new UUID(1L, 1L);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(
                new League(id, "League", "Brazil", "Americas", new BigDecimal(0))));
        Optional<League> league = service.getOneLeague(id);
        Assertions.assertAll(
                () -> Assertions.assertEquals("League", league.get().getName()),
                () -> Assertions.assertEquals("Brazil", league.get().getCountry()),
                () -> Assertions.assertEquals("Americas", league.get().getRegion()),
                () -> Assertions.assertEquals(id, league.get().getId())
        );
    }






}
