package com.example.demodatabasepj.repository;

import com.example.demodatabasepj.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.demodatabasepj.models.Player;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface PlayerRepository extends JpaRepository<Player, UUID> {

    @Query("SELECT player FROM Player player WHERE player.name LIKE %?1%")
        public Page<Player> searchAllByName(String name, Pageable pageable);

    @Query("SELECT COUNT(player) FROM Player player WHERE player.name LIKE %?1%")
        public long countAllByName(String name);


    @Query("SELECT pc.club FROM PlayerClub pc WHERE pc.player.id = ?1 AND pc.date_out IS NULL")
        public Optional<Club> getCurrentPlayerClubById(UUID id);

    @Query("SELECT DISTINCT player FROM Player player ORDER BY player.marketValue DESC LIMIT 1")
       public Optional<Player> findPlayerWithMaxMarketValue();


    // Jogadores que nunca realizaram um gol contra
    @Query("SELECT player FROM Player player " +
            "WHERE NOT EXISTS " +
            "(SELECT matchgoals FROM MatchGoals matchgoals " +
            " WHERE matchgoals.player = player AND matchgoals.type = 'CONTRA')")
   public Optional<List<Player>> playersWhoNeverScoredAOwnGoal();


    // Obter jogadores pela idade (Se eu estou ordenando a data aqui, aqui serão os mais novos?)
    @Query("SELECT player FROM Player player ORDER BY player.birthdate DESC") //
     public List<Player> playerSortedByDescBirthdate();

    // Obter jogadores pela idade (mais velhos?)
    @Query("SELECT player FROM Player player ORDER BY player.birthdate ASC")
    public List<Player> playerSortedByAscBirthdate();


    // Listagem dos jogadores com base no número de gols.
    @Query("SELECT matchgoals.player FROM MatchGoals matchgoals JOIN Player player ON matchgoals.player.id = player.id " +
            "GROUP BY player.id ORDER BY COUNT(matchgoals.player.id) DESC")
    public List<Player> findPlayersWhoScoreMostGoals();


    // Jogadores que mais trocaram de clube(realizaram/participaram de mais transferências)
    @Query("SELECT transfer.player FROM Transfer transfer JOIN Player player ON player.id = transfer.player.id " +
            "GROUP BY player.id ORDER BY COUNT(transfer.player.id) DESC")
     public List<Player> findMostTransferredPlayers();


    // Jogadores que possuem alguma transferência com valor superior a alguma quantia especificada
    @Query("SELECT player FROM Player player JOIN Transfer transfer ON player.id = transfer.player.id WHERE transfer.fee > SOME " +
            "(SELECT transfer.fee FROM Transfer transfer WHERE transfer.fee > ?1)")
    public List<Player> playerWithTransfersGreaterThanAValue(BigDecimal value);



}
