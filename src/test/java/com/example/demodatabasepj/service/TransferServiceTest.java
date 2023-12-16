package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.TransferRecordDTO;
import com.example.demodatabasepj.enumerator.Foot;
import com.example.demodatabasepj.exception.player.PlayerNotFoundException;
import com.example.demodatabasepj.exception.transfer.DuplicatedTransferException;
import com.example.demodatabasepj.exception.transfer.InvalidTransferDateException;
import com.example.demodatabasepj.exception.transfer.SameClubTransferException;
import com.example.demodatabasepj.models.Club;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.models.PlayerClub;
import com.example.demodatabasepj.models.Transfer;
import com.example.demodatabasepj.models.pk.PlayerClubPK;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.repository.PlayerClubRepository;
import com.example.demodatabasepj.repository.PlayerRepository;
import com.example.demodatabasepj.repository.TransferRepository;
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

public class TransferServiceTest {

    @InjectMocks
    private TransferService service;

    @Mock
    private TransferRepository transferRepository;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private PlayerRepository playerRepository;
    @Mock
    PlayerClubRepository playerClubRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("#countAllTransfersByPlayerName > return transfer number")
    public void countAllTransfersByPlayerName(){
        Mockito.when(transferRepository.countAllByPlayerNameOrJoinNameOrLeftName("test")).thenReturn(3L);
        Assertions.assertEquals(3L, service.countAllTransfersByPlayerNameOrJoinNameOrLeftName("test"));
    }

    @Test
    @DisplayName("#findAllTransfersByPlayerId > when player id is not valid > throw exception")
    public void findAllTransfersByPlayerIdWhenIdNotValidThrowExcception(){
        Mockito.when(transferRepository.findAllByPlayerId(null)).thenThrow(IllegalArgumentException.class);
        Assertions.assertThrows(IllegalArgumentException.class, ()-> service.findAllTransfersByPlayerId(null));
    }

    @Test
    @DisplayName("#findAllTransfersByPlayerId > when there is no transfer > return empty list")
    public void findAllTransfersByPlayerIdWhenThereIsNoTransferReturnEmptyList(){
        UUID player_id = UUID.randomUUID();
        Mockito.when(transferRepository.findAllByPlayerId(player_id)).thenReturn(new ArrayList<>());
        List<Transfer> transferList = service.findAllTransfersByPlayerId(player_id);
        Assertions.assertEquals(0, transferList.size());
    }


    @Test
    @DisplayName("#findAllTransfersByPlayerId > when id is valid > return list of transfers")
    public void findAllTransfersByPlayerIdWhenIdValidReturnListOfTransfer(){
        UUID player_id = UUID.randomUUID();
        Mockito.when(transferRepository.findAllByPlayerId(player_id)).thenReturn(new ArrayList<>(){
            {
                add(new Transfer());
            }
        });
        List<Transfer> transferList = service.findAllTransfersByPlayerId(player_id);
        Assertions.assertEquals(1, transferList.size());
    }


    @Test
    @DisplayName("#addTransfer > when both ids are null > throw an exception")
    public void addTransferBothNullIDThrowException(){
        UUID player_id = UUID.randomUUID();


        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, null, null, LocalDate.now(), new BigDecimal(1000));

        Assertions.assertThrows(SameClubTransferException.class, ()-> service.addTransfer(dto));
    }

    @Test
    @DisplayName("#addTransfer > when player not found > throw an exception")
    public void addTransferWhenPlayerNotFoundThrowException(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id = UUID.randomUUID();

        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now(), new BigDecimal(1000));

        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(PlayerNotFoundException.class, ()-> service.addTransfer(dto));
    }


    @Test
    @DisplayName("#addTransfer > when there are 2 clubs > when they are the same > throw an exception")
    public void addTransferWhenTransferringToTheSameClubThrowException(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id = club_left_id;
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );
        Club club_left = new Club(club_left_id, "Club1", "Stadium1", new BigDecimal(0));
        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now(), new BigDecimal(1000));

        Mockito.when(clubRepository.findById(club_left_id)).thenReturn(Optional.of(club_left));

        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));
        Assertions.assertThrows(SameClubTransferException.class, ()-> service.addTransfer(dto));
    }

    @Test
    @DisplayName("#addTransfer > when invalid date of transfer > throw an exception")
    public void addTransferWhenInvalidDateThrowException(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id =  UUID.randomUUID();
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );
        Club club_left = new Club(club_left_id, "Club1", "Stadium1", new BigDecimal(0));
        Club club_join = new Club(club_join_id, "Club2", "Stadium2", new BigDecimal(0));
        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now().minusDays(1), new BigDecimal(1000));

        Mockito.when(clubRepository.findById(club_left_id)).thenReturn(Optional.of(club_left));
        Mockito.when(clubRepository.findById(club_join_id)).thenReturn(Optional.of(club_join));
        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));

        Mockito.when(transferRepository.findLastPlayerTransfer(player_id))
                .thenReturn(Optional.of(LocalDate.now()));

        Assertions.assertThrows(InvalidTransferDateException.class, ()-> service.addTransfer(dto));
    }

    @Test
    @DisplayName("#addTransfer > when transferring multiple times in the same day > throw an exception")
    public void addTransferWhenMultipleTransfersThrowException(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id =  UUID.randomUUID();
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );
        Club club_left = new Club(club_left_id, "Club1", "Stadium1", new BigDecimal(0));
        Club club_join = new Club(club_join_id, "Club2", "Stadium2", new BigDecimal(0));
        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now(), new BigDecimal(1000));

        Mockito.when(clubRepository.findById(club_left_id)).thenReturn(Optional.of(club_left));
        Mockito.when(clubRepository.findById(club_join_id)).thenReturn(Optional.of(club_join));
        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));

        Mockito.when(transferRepository.findLastPlayerTransfer(player_id))
                .thenReturn(Optional.of(LocalDate.now()));

        Assertions.assertThrows(InvalidTransferDateException.class, ()-> service.addTransfer(dto));
    }

    @Test
    @DisplayName("#addTransfer > when the transfer already exists > throw an exception")
    public void addTransferWhenTheTransferAlreadyExistsThrowException(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id =  UUID.randomUUID();
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );
        Club club_left = new Club(club_left_id, "Club1", "Stadium1", new BigDecimal(0));
        Club club_join = new Club(club_join_id, "Club2", "Stadium2", new BigDecimal(0));
        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now().minusDays(1), new BigDecimal(1000));

        Mockito.when(clubRepository.findById(club_left_id)).thenReturn(Optional.of(club_left));
        Mockito.when(clubRepository.findById(club_join_id)).thenReturn(Optional.of(club_join));
        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));

        Mockito.when(transferRepository.findLastPlayerTransfer(player_id))
                .thenReturn(Optional.of(LocalDate.now().minusDays(10)));

        PlayerClubPK pk = new PlayerClubPK(club_join_id, player_id, LocalDate.now().minusDays(1));
        Mockito.when(playerClubRepository.findById(pk)).thenReturn(Optional.of(new PlayerClub()));

        Assertions.assertThrows(DuplicatedTransferException.class, ()-> service.addTransfer(dto));
    }


    @Test
    @DisplayName("#addTransfer > when the transfer does not exists > return transfer")
    public void addTransferWhenTheTransferDoesNotExistsReturnTheTransfer(){
        UUID player_id = UUID.randomUUID();
        UUID club_left_id = UUID.randomUUID();
        UUID club_join_id =  UUID.randomUUID();
        Player player = new Player(
                player_id, "Player",
                LocalDate.of(1999, 1, 1),
                Foot.RIGHT,
                1.75,
                new BigDecimal(0),
                "Brazil"
        );
        Club club_left = new Club(club_left_id, "Club1", "Stadium1", new BigDecimal(0));
        Club club_join = new Club(club_join_id, "Club2", "Stadium2", new BigDecimal(0));
        TransferRecordDTO dto = new TransferRecordDTO(
                player_id, club_left_id, club_join_id, LocalDate.now().minusDays(1), new BigDecimal(1000));

        Mockito.when(clubRepository.findById(club_left_id)).thenReturn(Optional.of(club_left));
        Mockito.when(clubRepository.findById(club_join_id)).thenReturn(Optional.of(club_join));
        Mockito.when(playerRepository.findById(player_id)).thenReturn(Optional.of(player));

        Mockito.when(transferRepository.findLastPlayerTransfer(player_id))
                .thenReturn(Optional.of(LocalDate.now().minusDays(10)));

        PlayerClubPK pk = new PlayerClubPK(club_join_id, player_id, LocalDate.now().minusDays(1));
        Mockito.when(playerClubRepository.findById(pk)).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(()-> service.addTransfer(dto));
    }
}
