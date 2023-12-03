package com.example.demodatabasepj.service;
import com.example.demodatabasepj.models.Player;
import com.example.demodatabasepj.models.Transfer;
import com.example.demodatabasepj.models.pk.PlayerClubPK;
import com.example.demodatabasepj.repository.PlayerClubRepository;
import com.example.demodatabasepj.models.PlayerClub;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
@Service
@AllArgsConstructor
public class PlayerClubService {

   private final PlayerClubRepository repository;
   public PlayerClub addPlayerClubRelationship(Transfer transfer) {

        if(Objects.isNull(transfer)) // Não é necessário se validei anteriormente, tendo certeza que o objeto vai ser criado corretamente.
        {
           throw new IllegalArgumentException("The transfer cannot be null");
        }

    // Se considerar que a validação de todos os atributos ocorreu, posso simplesmente fazer o objeto.

       PlayerClubPK primaryKey = new PlayerClubPK(transfer.getPlayer().getId(),
               transfer.getJoin().getID_club(), transfer.getDate());

       PlayerClub newPlayerClub = new PlayerClub(primaryKey, transfer.getJoin(), transfer.getPlayer(),
               primaryKey.getDate_in());

      return repository.save(newPlayerClub);
   }

   public Boolean existsPlayerClub(PlayerClubPK primaryKey) {

       if(repository.findById(primaryKey).isPresent())
       {
           return Boolean.TRUE;
       }
       return Boolean.FALSE;
   }

   public List<Player> getClubCurrentTeam(UUID club_id){
       return repository.findAllByClubAndDate_outNull(club_id);
   }

}
