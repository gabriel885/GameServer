package com.web.gameserver.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.web.gameserver.api.dto.GameDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "games")
public class Game {

    @NotNull
    @Column(name = "created")
    private Date created;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "first_player_username")
    @JsonBackReference
    private Player first_player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "second_player_username")
    @JsonBackReference
    private Player second_player;

    @Column(name = "game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus = GameStatus.WAITING_FOR_OPPONENT; // default value is finished game

    @Column(name = "game_type")
    @Enumerated(EnumType.STRING)
    private GameType gameType = GameType.CHESS; // default game is checkers

    @Column(name = "last_played")
    private String lastPlayedPlayer; // username of last played player

    public Game(GameDTO gameDTO) {
        setId(gameDTO.getId());
        setFirst_player(new Player(gameDTO.getFirstPlayer()));
        if (gameDTO.getSecondPlayer() != null && gameDTO.getSecondPlayer().getUsername() != null) {
            setSecond_player(new Player(gameDTO.getSecondPlayer()));
        }
        setCreated(gameDTO.getCreated());
        setGameStatus(gameDTO.getGameStatus());
        setGameType(gameDTO.getGameType());
        setLastPlayedPlayer(gameDTO.getLastPlayedPlayer());
    }
}
