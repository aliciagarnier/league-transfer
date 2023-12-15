package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.ClubLeagueDTO;
import com.example.demodatabasepj.dtos.ClubRecordDTO;
import com.example.demodatabasepj.exception.clubLeague.DuplicatedAssignmentBetweenClubLeagueException;
import com.example.demodatabasepj.exception.clubLeague.InvalidClubLeagueException;
import com.example.demodatabasepj.exception.clubLeague.InvalidClubLeagueTransferDateException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.ClubLeague;
import com.example.demodatabasepj.models.League;
import com.example.demodatabasepj.models.pk.ClubLeaguePK;
import com.example.demodatabasepj.repository.ClubLeagueRepository;
import com.example.demodatabasepj.repository.ClubRepository;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ClubLeagueServiceTest {

    @InjectMocks
    private ClubLeagueService service;


    @Mock
    private ClubLeagueRepository clubLeagueRepository;
    @Mock
    private ClubRepository clubRepository;

    @Mock
    private LeagueRepository leagueRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("#assignClubToLeague > when assignment is before current date > throw an exception")
    public void assignClubToLeagueWhenAssignmentBeforeCurrentDate(){
        UUID id1 = new UUID(1L, 1L);
        UUID id2 = new UUID(1L, 2L);
        ClubLeagueDTO clubLeagueDTO = new ClubLeagueDTO(id1, id2, LocalDate.now().plusDays(1));
        Assertions.assertThrows(InvalidClubLeagueTransferDateException.class,
                ()-> service.assignClubToLeague(clubLeagueDTO));
    }

    @Test
    @DisplayName("#assignClubToLeague > when the transfer already happened > throw an exception")
    public void assignClubToLeagueWhenTheTransferAlreadyHappenedThrowException(){
        UUID id1 = new UUID(1L, 1L);
        UUID id2 = new UUID(1L, 2L);
        ClubLeagueDTO clubLeagueDTO = new ClubLeagueDTO(id1, id2, LocalDate.now().minusDays(1));
        ClubLeaguePK pk = new ClubLeaguePK(clubLeagueDTO.club_id(), clubLeagueDTO.league_id(), clubLeagueDTO.date());
        Mockito.when(clubLeagueRepository.findById(pk)).thenReturn(Optional.of(new ClubLeague()));
        Assertions.assertThrows(DuplicatedAssignmentBetweenClubLeagueException.class,
                ()-> service.assignClubToLeague(clubLeagueDTO));
    }

    @Test
    @DisplayName("#assignClubToLeague > when the league is invalid > throw an exception")
    public void assignClubToLeagueWhenTheLeagueIsInvalidThrowException(){
        UUID league_id = new UUID(1L, 1L);
        UUID club_id = new UUID(1L, 2L);
        ClubLeagueDTO clubLeagueDTO = new ClubLeagueDTO(league_id, club_id, LocalDate.now().minusDays(1));
        ClubLeaguePK pk = new ClubLeaguePK(clubLeagueDTO.club_id(), clubLeagueDTO.league_id(), clubLeagueDTO.date());

        Mockito.when(clubLeagueRepository.findById(pk)).thenReturn(Optional.empty());

        Mockito.when(leagueRepository.findById(league_id)).thenReturn(Optional.empty());
        Mockito.when(clubRepository.findById(club_id)).thenReturn(Optional.of(new Club()));

        Assertions.assertThrows(InvalidClubLeagueException.class,
                ()-> service.assignClubToLeague(clubLeagueDTO));
    }


    @Test
    @DisplayName("#assignClubToLeague > when the club is invalid > throw an exception")
    public void assignClubToLeagueWhenTheClubIsInvalidThrowException(){
        UUID league_id = new UUID(1L, 1L);
        UUID club_id = new UUID(1L, 2L);
        ClubLeagueDTO clubLeagueDTO = new ClubLeagueDTO(league_id, club_id, LocalDate.now().minusDays(1));
        ClubLeaguePK pk = new ClubLeaguePK(clubLeagueDTO.club_id(), clubLeagueDTO.league_id(), clubLeagueDTO.date());

        Mockito.when(clubLeagueRepository.findById(pk)).thenReturn(Optional.empty());

        Mockito.when(leagueRepository.findById(league_id)).thenReturn(Optional.of(new League()));
        Mockito.when(clubRepository.findById(club_id)).thenReturn(Optional.empty());

        Assertions.assertThrows(InvalidClubLeagueException.class,
                ()-> service.assignClubToLeague(clubLeagueDTO));
    }


    @Test
    @DisplayName("#assignClubToLeague > when the club and league is valid > return clubLeague")
    public void assignClubToLeagueWhenTheClubAndLeagueIsValidReturnClubLeague(){
        UUID league_id = new UUID(1L, 1L);
        UUID club_id = new UUID(1L, 2L);
        ClubLeagueDTO clubLeagueDTO = new ClubLeagueDTO(league_id, club_id, LocalDate.now().minusDays(1));
        ClubLeaguePK pk = new ClubLeaguePK(clubLeagueDTO.club_id(), clubLeagueDTO.league_id(), clubLeagueDTO.date());

        Mockito.when(clubLeagueRepository.findById(pk)).thenReturn(Optional.empty());

        Mockito.when(leagueRepository.findById(league_id)).thenReturn(Optional.of(
                new League(league_id, "Liga", "Brazil", "Americas", new BigDecimal(0))));
        Mockito.when(clubRepository.findById(club_id)).thenReturn(Optional.of(
                new Club(club_id, "Club1", "Maracana", new BigDecimal(0))));


        Assertions.assertDoesNotThrow(()->service.assignClubToLeague(clubLeagueDTO));
    }

    @Test
    @DisplayName("#findCurrentSquadClub > when the squad is empty >  return empty list")
    public void findCurrentSquadClubWhenTheSquadIsEmptyReturnEmptyList(){
        UUID league_id = new UUID(1L, 1L);
        Mockito.when(clubLeagueRepository.findAllClubsByCurrentYear(league_id)).thenReturn(new ArrayList<>());
        List<Club> clubs = service.findCurrentSquadClub(league_id);
        Assertions.assertEquals(0, clubs.size());
    }


    @Test
    @DisplayName("#findCurrentSquadClub > when the squad is not empty >  return list of clubs")
    public void findCurrentSquadClubWhenTheSquadIsNotEmptyReturnClubs(){
        UUID league_id = new UUID(1L, 1L);
        Mockito.when(clubLeagueRepository.findAllClubsByCurrentYear(league_id)).thenReturn(new ArrayList<>(){
            {
                add(new Club(new UUID(1L, 2L),
                        "Club1", "Maracana", new BigDecimal(0)));
                add(new Club(new UUID(1L, 3L),
                        "Club2", "Alianz Park", new BigDecimal(0)));
            }
        });

        List<Club> clubs = service.findCurrentSquadClub(league_id);
        Assertions.assertEquals(2, clubs.size());
    }


    @Test
    @DisplayName("#getLeagueCurrentMV > when the league is invalid > throw exception")
    public void getLeagueCurrentMVWhenTheLeagueIsInvalidThrowException(){
        Mockito.when(clubLeagueRepository.getLeagueMV(null)).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> service.getLeagueCurrentMV(null));
    }

    @Test
    @DisplayName("#getLeagueCurrentMV > when the league is valid > return mv")
    public void getLeagueCurrentMVWhenTheLeagueIsValidReturnMarketValue(){
        UUID league_id = new UUID(1L, 1L);
        Mockito.when(clubLeagueRepository.getLeagueMV(league_id)).thenReturn(new BigDecimal(1000));
        BigDecimal mv = service.getLeagueCurrentMV(league_id);
        Assertions.assertEquals(new BigDecimal(1000), mv);
    }

}
