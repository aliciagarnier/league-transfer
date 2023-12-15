package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.MatchGoalRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.GoalType;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.match.GoalNotFoundException;
import com.example.demodatabasepj.exception.match.MatchNotFoundException;
import com.example.demodatabasepj.exception.player.PlayerNotFoundException;
import com.example.demodatabasepj.models.*;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.repository.MatchGoalRepository;
import com.example.demodatabasepj.repository.MatchRepository;
import com.example.demodatabasepj.repository.PlayerRepository;
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
import java.util.Optional;
import java.util.UUID;

public class MatchGoalServiceTest {

    @InjectMocks
    private MatchGoalService service;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchGoalRepository matchGoalRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#assignGoal > when the match does not exists > throw an exception")
    public void assignGoalWhenTheMatchDoesNotExistsThrowAnException(){
        UUID match_id = UUID.randomUUID();
        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.empty());
        MatchGoalRecordDTO dto = new MatchGoalRecordDTO(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                GoalType.NORMAL
        );
        Assertions.assertThrows(MatchNotFoundException.class, ()-> service.assignGoal(dto));
    }

    @Test
    @DisplayName("#assignGoal > when the player does not exists > throw an exception")
    public void assignGoalWhenThePlayerDoesNotExistsThrowAnException(){
        UUID match_id = UUID.randomUUID();
        UUID player_id = UUID.randomUUID();
        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.of(new Match()));
        MatchGoalRecordDTO dto = new MatchGoalRecordDTO(
                match_id,
                player_id,
                UUID.randomUUID(),
                GoalType.NORMAL
        );

        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(PlayerNotFoundException.class, ()-> service.assignGoal(dto));
    }

    @Test
    @DisplayName("#assignGoal > when the club does not exists > throw an exception")
    public void assignGoalWhenTheClubDoesNotExistsThrowAnException(){
        UUID match_id = UUID.randomUUID();
        UUID player_id = UUID.randomUUID();
        UUID club_id = UUID.randomUUID();
        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.of(new Match()));
        MatchGoalRecordDTO dto = new MatchGoalRecordDTO(
                match_id,
                player_id,
                club_id,
                GoalType.NORMAL
        );

        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(new Player()));

        Mockito.when((clubRepository.findById(club_id))).thenReturn(Optional.empty());

        Assertions.assertThrows(ClubDoesNotExistsException.class, ()-> service.assignGoal(dto));
    }


    @Test
    @DisplayName("#assignGoal > when the dto is valid > assign goal")
    public void assignGoalWhenDataIsValidAssignGoal(){
        UUID match_id = UUID.randomUUID();
        UUID player_id = UUID.randomUUID();
        UUID club_host_id = UUID.randomUUID();
        UUID club_guest_id = UUID.randomUUID();

        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
                );

        Club HostClub = new Club(club_host_id, "Club1", "Stadium1", new BigDecimal(0));
        Club GuestClub = new Club(club_guest_id, "Club2", "Stadium2", new BigDecimal(0));
        Match match = new Match(match_id,
                new League(),
                HostClub,
                GuestClub,
                0,
                0,
                LocalDate.now(),
                null);

        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.of(match));
        MatchGoalRecordDTO dto = new MatchGoalRecordDTO(
                match_id,
                player_id,
                club_host_id,
                GoalType.NORMAL
        );

        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));

        Mockito.when((clubRepository.findById(club_host_id))).thenReturn(Optional.of(HostClub));

        Assertions.assertDoesNotThrow(()-> service.assignGoal(dto));
    }


    @Test
    @DisplayName("#revokeGoal > when the goal does not exists > throw an exception")
    public void revokeGoalWhenTheGoalDoesNotExistsThrowException(){
        UUID match_id = UUID.randomUUID();
        Assertions.assertThrows(GoalNotFoundException.class, ()->service.revokeGoal(match_id, null));
    }

    @Test
    @DisplayName("#revokeGoal > when the match does not exists > throw an exception")
    public void revokeGoalWhenTheMatchDoesNotExistsThrowException(){
        UUID goal_id = UUID.randomUUID();
        Assertions.assertThrows(MatchNotFoundException.class, ()->service.revokeGoal(null, goal_id));
    }


    @Test
    @DisplayName("#revokeGoal > when the match is not found > throw an exception")
    public void revokeGoalWhenTheMatchIsNotFoundThrowException(){
        UUID goal_id = UUID.randomUUID();
        UUID match_id = UUID.randomUUID();
        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(MatchNotFoundException.class, ()->service.revokeGoal(match_id, goal_id));
    }

    @Test
    @DisplayName("#revokeGoal > when the goal is not found > throw an exception")
    public void revokeGoalWhenTheGoalIsNotFoundThrowException(){
        UUID goal_id = UUID.randomUUID();
        UUID match_id = UUID.randomUUID();
        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.of(new Match()));
        Mockito.when(matchGoalRepository.findById(goal_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(GoalNotFoundException.class, ()->service.revokeGoal(match_id, goal_id));
    }

    @Test
    @DisplayName("#revokeGoal > when the data is valid > revoke goal")
    public void revokeGoalWhenTheDataIsValidRevokeGoal(){
        UUID match_id = UUID.randomUUID();
        UUID club_host_id = UUID.randomUUID();
        UUID club_guest_id = UUID.randomUUID();
        UUID goal_id = UUID.randomUUID();
        UUID player_id = UUID.randomUUID();
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );


        Club HostClub = new Club(club_host_id, "Club1", "Stadium1", new BigDecimal(0));
        Club GuestClub = new Club(club_guest_id, "Club2", "Stadium2", new BigDecimal(0));
        Match match = new Match(match_id, new League(), HostClub, GuestClub,
                0, 0, LocalDate.now(), null);

        MatchGoals goal = new MatchGoals(goal_id, match, player, HostClub, GoalType.NORMAL);

        Mockito.when(matchRepository.findById(match_id)).thenReturn(Optional.of(match));

        Mockito.when(matchGoalRepository.findById(goal_id)).thenReturn(Optional.of(goal));

        Assertions.assertDoesNotThrow(()->service.revokeGoal(match_id, goal_id));
    }

    @Test
    @DisplayName("#getSpecificClubGoalsInMatch > when the match is invalid > throw exception")
    public void getSpecificClubGoalsInMatchMatchInvalidThrowException(){
        UUID club_id = UUID.randomUUID();
        Mockito.when(matchGoalRepository.countAllByMatchAndClub(null,club_id))
                .thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.getSpecificClubGoalsInMatch(null, club_id));
    }

    @Test
    @DisplayName("#getSpecificClubGoalsInMatch > when the club is invalid > throw exception")
    public void getSpecificClubGoalsInMatchClubInvalidThrowException(){
        UUID match_id = UUID.randomUUID();
        Mockito.when(matchGoalRepository.countAllByMatchAndClub(match_id,null))
                .thenThrow(IllegalArgumentException.class);

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> service.getSpecificClubGoalsInMatch(match_id, null));
    }

    @Test
    @DisplayName("#getSpecificClubGoalsInMatch > when the club and match are valid > return goal count")
    public void getSpecificClubGoalsInMatchDataValidReturnGoalCount(){
        UUID match_id = UUID.randomUUID();
        UUID club_id = UUID.randomUUID();
        Mockito.when(matchGoalRepository.countAllByMatchAndClub(match_id,club_id))
                .thenReturn(3);

        int numberOfGoals = service.getSpecificClubGoalsInMatch(match_id,club_id);
        Assertions.assertEquals(3, numberOfGoals);
    }



}
