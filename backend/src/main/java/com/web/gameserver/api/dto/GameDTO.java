package com.web.gameserver.api.dto;

import com.web.gameserver.api.model.Game;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.api.model.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GameDTO {
    private long id;
    private PlayerDTO firstPlayer;
    private PlayerDTO secondPlayer;
    private Date created;
    private GameType gameType;
    private GameStatus gameStatus;
    private String lastPlayedPlayer;

    public GameDTO(Game game) {
        setId(game.getId());
        setFirstPlayer(new PlayerDTO(game.getFirst_player()));
        setSecondPlayer(new PlayerDTO(game.getSecond_player()));
        setCreated(game.getCreated());
        setGameType(game.getGameType());
        setGameStatus(game.getGameStatus());
        setLastPlayedPlayer(game.getLastPlayedPlayer());
    }

    /**
     * return true if player is one of the players in game
     *
     * @param player
     * @return
     */
    public boolean hasPlayer(PlayerDTO player) {
        if (player == null || player.getUsername() == null || this.firstPlayer == null) {
            return false;
        }
        if (getSecondPlayer() == null || getSecondPlayer().getUsername() == null) {
            return this.firstPlayer.equals(player);
        }
        return (this.firstPlayer.equals(player) || this.secondPlayer.equals(player));
    }

    @Override
    public String toString() {
        if (getSecondPlayer() == null) {
            return String.format("\tGAME - %d \n\t Player 1 %s \n\t Player 2 UNDEFINED" +
                    " \n\t Date %s \n\t Type %s \n\t Status %s \n", getId(), getFirstPlayer().toString(), getCreated().toString(), getGameType(), getGameStatus());
        }
        return String.format("\tGAME - %d \n\t Player 1 %s \n\t Player 2 %s" +
                        " \n\t Date %s \n\t Type %s \n\t Status %s \n", getId(), getFirstPlayer().toString(),
                getSecondPlayer().toString(), getCreated().toString(), getGameType(), getGameStatus());
    }

    /**
     * compare objects by id
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        GameDTO game = (GameDTO) o;
        return Long.compare(this.getId(), game.getId()) == 0;
    }
}
