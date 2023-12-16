package com.example.demodatabasepj.service;

import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.repository.PlayerClubRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerClubServiceTest {
    @InjectMocks
    private PlayerClubService service;

    @Mock
    private PlayerClubRepository playerClubRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#getClubCurrentTeam > when club id is not valid > throw exception ")
    public void getClubCurrentTeamWhenClubIdIsNotValidThrowException(){
        Mockito.when(playerClubRepository.findAllByClubAndDate_outNull(null))
                .thenThrow(IllegalArgumentException.class);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.getClubCurrentTeam(null));
    }

    @Test
    @DisplayName("#getClubCurrentTeam > when club id is valid > return list of players ")
    public void getClubCurrentTeamWhenClubWhenClubIsValidReturnListOfPlayers(){
        UUID club_id = UUID.randomUUID();
        Mockito.when(playerClubRepository.findAllByClubAndDate_outNull(club_id))
                .thenReturn(new ArrayList<>(){
                    {add(new Player());}
                });
        List<Player> players = service.getClubCurrentTeam(club_id);
        Assertions.assertEquals(1, players.size());
    }
}
