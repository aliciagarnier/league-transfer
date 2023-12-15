package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.PlayerRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.exception.player.InvalidBirthDateException;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.repository.PlayerRepository;

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


public class PlayerServiceTest {
    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#addPlayer > When the necessary parameters are passed > Persist the player successfully")
    void addPlayerWhenOnlyNameAndMarketValueArePassedPersistThePlayer() {
        assertDoesNotThrow(() -> service.addPlayer("Player #1", LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));

    }

    @Test
    @DisplayName("#addPlayer > When the name parameter is invalid > Throw an InvalidPlayerException")
    void addPlayerWhenTheNameIsInvalidThrowAnException() {
        assertThrows(InvalidPlayerException.class, () -> service.addPlayer("", LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));

        assertThrows(InvalidPlayerException.class, () -> service.addPlayer(null, LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));
    }

    @Test
    @DisplayName("#addPlayer > When the market value parameter is invalid > Throw an InvalidPlayerException")
    void addPlayerWhenTheMarketValueIsInvalidThrowAnException() {
        assertAll(
                () -> assertThrows(InvalidPlayerException.class, () -> service.addPlayer("Player #1",
                        LocalDate.of(1999, 5, 13), Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85,
                        BigDecimal.valueOf(-1), "Brazilian")),
                () -> assertThrows(InvalidPlayerException.class, () -> service.addPlayer("", LocalDate.of(1999, 5, 13),
                        Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, null, "Brazilian"))
        );
    }

    @Test
    @DisplayName("#addPlayer > When the birthdate is invalid > Throw an InvalidBirthdateException")
    void addPlayerWhenTheBirthdateIsInvalidThrowAnException() {
        assertAll(
                () -> assertThrows(InvalidBirthDateException.class, () -> service.addPlayer("Player 1",
                        LocalDate.of(2010, 12, 10), Position.CENTREFORWARD, Foot.LEFT,
                        1.85, BigDecimal.valueOf(100), "Brazilian")),
                () -> assertThrows(InvalidBirthDateException.class, () -> service.addPlayer("Player 2",
                        LocalDate.of(2024, 2, 9), Position.CENTREBACK, Foot.LEFT,
                        1.75, BigDecimal.valueOf(12200), "Brazilian"))
        );
    }

    @Test
    @DisplayName("#updatePlayer > When the player record is valid > Return the updated player")
    public void updatePlayerWhenPlayerRecordIsValidReturnTheUpdatedPlayer() {
        Player player = new Player("Player 1",
                LocalDate.of(2010, 12, 10), Position.CENTREFORWARD, Foot.LEFT,
                1.85, BigDecimal.valueOf(100), "Brazilian");

        Mockito.when(playerRepository.save(player)).thenReturn(player);

        PlayerRecordDTO playerRecordDTO = new PlayerRecordDTO("Player 1",
                LocalDate.of(2010, 12, 10), Position.CENTREFORWARD, Foot.LEFT,
                1.85, BigDecimal.valueOf(100), "Brazilian");

        Player response = service.updatePlayer(playerRecordDTO, player);

        assertAll(
                () -> assertEquals(response.getName(), playerRecordDTO.name()),
                () -> assertEquals(response.getBirthdate(), playerRecordDTO.birthdate()),
                () -> assertEquals(response.getPosition(), playerRecordDTO.position()),
                () -> assertEquals(response.getFoot(), playerRecordDTO.foot()),
                () -> assertEquals(response.getHeight(), playerRecordDTO.height()),
                () -> assertEquals(response.getMarketValue(), playerRecordDTO.marketValue()),
                () -> assertEquals(response.getNacionality(), playerRecordDTO.nacionality())
        );

    }

    @Test
    @DisplayName("#updatePlayer > When the player record is invalid > Throw an IllegalArgumentException")
    public void updatePlayerWhenRecordIsInvalidReturnAnException() {
        assertThrows(IllegalArgumentException.class, () -> service.updatePlayer(null,
                new Player()));
    }


    @Test
    @DisplayName("#findById > When the id is null > Throw and IllegalArgumentException")
    void findByIdWhenTheIdIsNullThrowAnException() {
        Mockito.when(playerRepository.findById(null)).thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
    }

    @Test
    @DisplayName("#existPlayer > When the player is found > Return true")
    void existPlayerWhenThePlayerIsFoundReturnTrue() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(playerRepository.findById(uuid)).thenReturn(Optional.of(new Player()));
        assertTrue(service.existPlayer(uuid));
    }

    @Test
    @DisplayName("#existPlayer > When the player is not found > Return false")
    void existPlayerWhenThePlayerIsNotFoundReturnFalse() {
        UUID uuid = UUID.randomUUID();
        Mockito.when(playerRepository.findById(uuid)).thenReturn(Optional.empty());
        assertFalse(service.existPlayer(uuid));
    }

}