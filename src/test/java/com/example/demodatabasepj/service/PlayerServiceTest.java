package com.example.demodatabasepj.service;

import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.enumerator.Position;
import com.example.demodatabasepj.exception.player.InvalidPlayerException;
import com.example.demodatabasepj.repository.PlayerRepository;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


public class PlayerServiceTest {
    @Mock
    PlayerRepository playerRepository;

    @InjectMocks
    PlayerService service;

    @BeforeEach
    public void setup () {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("#addPlayer > When only the necessary parameters are passed > Persist the player sucessfully")
    void addPlayerWhenOnlyNameAndMarketValueArePassedPersistThePlayer () {
        assertDoesNotThrow(() -> service.addPlayer( "Player #1", LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));

    }
    @Test
    @DisplayName("#addPlayer > When the name parameter is invalid > Throw an InvalidPlayerException")
    void addPlayerWhenTheNameIsInvalidThrowAnException() {
        assertThrows(InvalidPlayerException.class, () -> service.addPlayer( "", LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));

        assertThrows(InvalidPlayerException.class, () -> service.addPlayer( null, LocalDate.of(1999, 5, 13),
                Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, BigDecimal.valueOf(19000000), "Brazilian"));
    }

    @Test
    @DisplayName("#addPlayer > When the market value parameter is invalid > Throw an InvalidPlayerException")
    void addPlayerWhenTheMarketValueIsInvalidThrowAnException() {
        assertAll(
                () -> assertThrows(InvalidPlayerException.class, () -> service.addPlayer( "Player #1",
                        LocalDate.of(1999, 5, 13), Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85,
                        BigDecimal.valueOf(-1), "Brazilian")),
                () -> assertThrows(InvalidPlayerException.class, () -> service.addPlayer("", LocalDate.of(1999, 5, 13),
                        Position.ATTACKINGMIDFIELD, Foot.LEFT, 1.85, null, "Brazilian"))
        );
    }




}
