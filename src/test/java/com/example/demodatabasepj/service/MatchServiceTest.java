package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.MatchRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.league.LeagueDoesNotExistsException;
import com.example.demodatabasepj.exception.match.InvalidMatchDateException;
import com.example.demodatabasepj.exception.match.InvalidScoreException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MatchServiceTest {


    @InjectMocks
    private MatchService service;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ClubService clubService;

    @Mock
    private LeagueService leagueService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#addMatch > When the parameters are valid > Return the match")
    public void addMatchWhenTheParametersAreValidReturnTheMatch () {

        UUID league_id = UUID.randomUUID();
        UUID hostTeam_id = UUID.randomUUID();
        UUID guestTeam_id = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam_id)).thenReturn(Boolean.TRUE);
        Mockito.when(clubService.existClub(guestTeam_id)).thenReturn(Boolean.TRUE);
        Mockito.when(leagueService.existLeague(league_id)).thenReturn(Boolean.TRUE);

        Mockito.when(leagueService.getOneLeague(league_id)).thenReturn(
                Optional.of(new League(league_id, "Liga1", "Brazil",
                        "Americas",  new BigDecimal(0)))
        );

        Mockito.when(clubService.getOneClub(hostTeam_id)).thenReturn(Optional.of(
                new Club(hostTeam_id,"Club1", "Estadio1", new BigDecimal(0))
        ));

        Mockito.when(clubService.getOneClub(guestTeam_id)).thenReturn(Optional.of(
                new Club(guestTeam_id,"Club2", "Estadio2", new BigDecimal(0))
        ));

        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league_id, hostTeam_id, guestTeam_id, 2, 3,
                LocalDate.of(2022, 12, 9));

        assertDoesNotThrow(() -> service.addMatch(matchRecordDTO));
    }

    @Test
    @DisplayName("#addMatch > When the parameters are null > Throw an exception")
    public void addMatchWhenTheParametersAreNullThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        assertAll (
                () -> assertThrows(IllegalArgumentException.class, () -> service.addMatch(new MatchRecordDTO(null,
                        null, null, 2, 3, LocalDate.now()))),
                () -> assertThrows(IllegalArgumentException.class, () -> service.addMatch(new MatchRecordDTO(league,
                        null, null, 2, 3, LocalDate.now()))),
                () -> assertThrows(IllegalArgumentException.class, () -> service.addMatch(new MatchRecordDTO(null,
                        hostTeam, null, 2, 3, LocalDate.now()))),
                () -> assertThrows(IllegalArgumentException.class, () -> service.addMatch(new MatchRecordDTO(null,
                        null, guestTeam, 2, 3, LocalDate.now())))
        );
    }


    @Test
    @DisplayName("#addMatch > When the club does not exist > Throw an ClubDoesNotExistsException")
    public void addMatchWhenTheClubDoesNotExistThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam)).thenReturn(Boolean.FALSE);
        Mockito.when(clubService.existClub(guestTeam)).thenReturn(Boolean.FALSE);


        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league, hostTeam, guestTeam, 2, 3,
                LocalDate.of(2022, 12, 9));

        assertThrows(ClubDoesNotExistsException.class, () -> service.addMatch(matchRecordDTO));
    }

    @Test
    @DisplayName("#addClub > When the league does not exist > Throw an LeagueDoesNotExistsException")
    public void addMatchWhenTheLeagueDoesNotExistThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(clubService.existClub(guestTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(leagueService.existLeague(league)).thenReturn(Boolean.FALSE);


        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league, hostTeam, guestTeam, 2, 3,
                LocalDate.of(2022, 12, 9));

        assertThrows(LeagueDoesNotExistsException.class, () -> service.addMatch(matchRecordDTO));
    }

    @Test
    @DisplayName("#addMatch > When the teams are the same > Throw an IllegalArgumentException")
    public void addMatchWhenTheTeamsAreTheSameThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(clubService.existClub(guestTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(leagueService.existLeague(league)).thenReturn(Boolean.TRUE);


        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league, guestTeam, guestTeam, 2, 3,
                LocalDate.of(2022, 12, 9));

        assertThrows(IllegalArgumentException.class, () -> service.addMatch(matchRecordDTO));
    }

    @Test
    @DisplayName("#addMatch > When the score is invalid > Throw an InvalidScoreException")
    public void addMatchWhenTheScoreIsInvalidThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(clubService.existClub(guestTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(leagueService.existLeague(league)).thenReturn(Boolean.TRUE);


        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league, hostTeam, guestTeam, -2, 3,
                LocalDate.of(2022, 12, 9));

        assertThrows(InvalidScoreException.class, () -> service.addMatch(matchRecordDTO));
    }

    @Test
    @DisplayName("#addMatch > When the match date is invalid > Throw an InvalidMatchDateException")
    public void addMatchWhenTheMatchDateIsInvalidThrowAnException () {

        UUID league = UUID.randomUUID();
        UUID hostTeam = UUID.randomUUID();
        UUID guestTeam = UUID.randomUUID();

        Mockito.when(clubService.existClub(hostTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(clubService.existClub(guestTeam)).thenReturn(Boolean.TRUE);
        Mockito.when(leagueService.existLeague(league)).thenReturn(Boolean.TRUE);


        MatchRecordDTO matchRecordDTO = new MatchRecordDTO(league, hostTeam, guestTeam, 2, 3,
                LocalDate.of(2024, 12, 9));

        assertThrows(InvalidMatchDateException.class, () -> service.addMatch(matchRecordDTO));
    }





}