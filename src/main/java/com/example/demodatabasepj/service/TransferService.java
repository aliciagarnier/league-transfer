package com.example.demodatabasepj.service;

import com.example.demodatabasepj.dtos.TransferRecordDTO;
import com.example.demodatabasepj.exception.club.ClubDoesNotExistsException;
import com.example.demodatabasepj.exception.player.PlayerNotFoundException;
import com.example.demodatabasepj.exception.transfer.DuplicatedTransferException;
import com.example.demodatabasepj.exception.transfer.InvalidTransferDateException;
import com.example.demodatabasepj.exception.transfer.SameClubTransferException;
import com.example.demodatabasepj.models.*;


import com.example.demodatabasepj.models.pk.PlayerClubPK;
import com.example.demodatabasepj.repository.ClubRepository;
import com.example.demodatabasepj.repository.PlayerClubRepository;
import com.example.demodatabasepj.repository.PlayerRepository;
import com.example.demodatabasepj.repository.TransferRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@AllArgsConstructor

@Service
public class TransferService {


    private final TransferRepository transferRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;
    private final PlayerClubRepository playerClubRepository;

    // Reviewing
    public Transfer addTransfer (TransferRecordDTO transferRecordDTO) {

        if(Objects.isNull(transferRecordDTO.club_join_id()) && Objects.isNull(transferRecordDTO.club_left_id())){
            throw new SameClubTransferException("Cannot transfer to both null clubs");
        }


        UUID player_id = transferRecordDTO.player_id(); //UUID.fromString(transferRecordDTO.player_id());
        UUID club_join_id = transferRecordDTO.club_join_id(); //UUID.fromString(transferRecordDTO.club_join_id());
        UUID club_left_id = transferRecordDTO.club_left_id(); //UUID.fromString(transferRecordDTO.club_left_id());


        //validar dados
        Optional<Player> player = playerRepository.findById(player_id);
        Club club_in = null;
        Club club_out = null;
        Optional<Club> club_in_opt;
        Optional<Club> club_out_opt;

        if (player.isEmpty()){
            throw new PlayerNotFoundException("Player not found");
        }

        //se existir club de entrada
        if(!Objects.isNull(club_join_id)){
            club_in_opt = clubRepository.findById(club_join_id);
            if(club_in_opt.isPresent()){
                club_in = club_in_opt.get();
            }
        }


        //se existir club de saida
        if(!Objects.isNull(club_left_id)) {
            club_out_opt = clubRepository.findById(club_left_id);
            // se existir clube de saida > garantir que nao sejam iguais
            if (club_out_opt.isPresent()) {
                if (!Objects.isNull(club_in) && club_out_opt.get().getID_club().equals(club_in.getID_club())) {
                    throw new SameClubTransferException("Cannot transfer to the same club");
                }
                club_out = club_out_opt.get();
            }
        }




            LocalDate received_date = transferRecordDTO.date();
            Optional<LocalDate> last_transfer_date = transferRepository.findLastPlayerTransfer(player_id); //buscar data da ultima transf do jogador
            if (last_transfer_date.isEmpty()) { // se nao existir, atribuir data minima
                last_transfer_date = Optional.of(LocalDate.MIN);
            }

            if (received_date.isAfter(LocalDate.now()) || received_date.isBefore(last_transfer_date.get())) {
                throw new InvalidTransferDateException("Invalid date of transfer!");
            }

        if(received_date.isEqual(last_transfer_date.get())){
            throw new InvalidTransferDateException("Esse jogador ja fez uma transferencia neste dia!");
        }



            // SETAR EM PLAYER_CLUB A DATA DA TRANSF E DATE OUT NULL -- DONE
            // NA PROXIMA TRANSF ATUALIZAR DATE OUT
            //OU SEJA, BUSCAR A ULTIMA TUPLA PELA DATA MAIS RECENTE EM PLAYER CLUB E ATUALIZAR DATE OUT
            // ALEM DISSO, CRIAR UMA NOVA TUPLA EM PLAYER CLUB COM DATE IN FORNECIDO PELO DTO.

            //Buscando tupla em PlayerClub relation > clube de entrada

            if (!Objects.isNull(club_in)) {
                PlayerClubPK playerClubPK_club_in = new PlayerClubPK(
                        club_join_id,
                        player_id,
                        transferRecordDTO.date());
                Optional<PlayerClub> player_club_in = playerClubRepository.findById(playerClubPK_club_in); //procurar tupla
                //Criar tupla em player_club se ela nao existie
                if (player_club_in.isEmpty()) {
                    PlayerClub new_playerClubTuple = new PlayerClub(playerClubPK_club_in, club_in, player.get(), null);
                    playerClubRepository.save(new_playerClubTuple);

                } else {
                    // se existir tupla com mesmo jogador , mesmo clube e mesma data. lancar excecao
                    throw new DuplicatedTransferException("Transfer already exists!");
                }
            }


                //Se existir clube de saida > atualizar date_out para data da transferencia
                //Buscando tupla em PlayerClub relation > clube de saida >
                //Buscar a tupla que possui clube_out == dto.club_out, jogador == dto.player e date_out == null

                Optional<PlayerClub> player_club_out = playerClubRepository
                        .findPlayerClubByClubAndPlayerAndDate_outNull(transferRecordDTO.player_id(), transferRecordDTO.club_join_id());
                if (player_club_out.isPresent()) {
                    playerClubRepository.updatePlayerClubByDate_out(
                            transferRecordDTO.player_id(), transferRecordDTO.club_left_id(), transferRecordDTO.date());

                }
                //Realizar transferencia se tudo ok
                Transfer transfer = new Transfer();
                transfer.setDate(transferRecordDTO.date());
                transfer.setFee(transferRecordDTO.fee());
                transfer.setPlayer(player.get());
                transfer.setJoin(club_in);
                transfer.setLeft(club_out);

                return transferRepository.save(transfer);
            }

     public Transfer updateTransfer (TransferRecordDTO transferRecordDTO, Transfer transfer) { // Quando vamos usar esse método?

         if (transferRepository.findTransferByPlayerDateJoinAndLeftClub(transferRecordDTO.player_id(),
                 transferRecordDTO.club_join_id(), transferRecordDTO.club_left_id(),
                 transferRecordDTO.date()).isPresent())
         {
             throw new DuplicatedTransferException("This transfer already exists."); // Vale para o caso que nenhuma
             // alteração é realizada.
         }

         BeanUtils.copyProperties(transferRecordDTO, transfer);
         return transferRepository.save(transfer);
    }

    public List<Transfer> findAllTransfers () {
        return transferRepository.findAll();
    }

    public Optional<Transfer> findTransferById(UUID id) {
        if(Objects.isNull(id)) {
            throw new IllegalArgumentException("ID null when fetching for a transfer.");
        }
        return transferRepository.findById(id);
    }

    public void deleteTransfer(Transfer transfer) {
        transferRepository.delete(transfer);
    }

    public Page<Transfer> getAllTransfersWithFilter(String keyword, int pageNumber, String sortField, String sortDir, int pageSize)
    {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1 , pageSize, sort);

        if(Objects.isNull(keyword)){
            return transferRepository.findAll(pageable);
        }

        return transferRepository.searchAllByPlayerNameOrJoinNameOrLeftName(keyword, pageable);
    }

    public Long countAllTransfersByPlayerNameOrJoinNameOrLeftName(String keyword){
        return transferRepository.countAllByPlayerNameOrJoinNameOrLeftName(keyword);
    }

    public List<Transfer> findAllTransfersByPlayerId(UUID id){
        return transferRepository.findAllByPlayerId(id);
    }

}
