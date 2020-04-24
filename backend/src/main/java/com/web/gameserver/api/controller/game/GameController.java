package com.web.gameserver.api.controller.game;

import com.web.gameserver.api.dto.GameDTO;
import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.api.model.GameType;
import com.web.gameserver.api.model.PieceColor;
import com.web.gameserver.eventsource.events.AliveEvent;
import com.web.gameserver.eventsource.events.GameStatusEvent;
import com.web.gameserver.service.EventSourceObserverService;
import com.web.gameserver.service.GameService;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * responsible for managing any game
 */
@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private EventSourceObserverService eventSourceService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;


    /**
     * return all logged in user's games to resume (paused or in progress)
     *
     * @return
     */
    @GetMapping("/resume")
    public List<GameDTO> getAllGamesToResume() {
        return gameService.getGamesByUsername(playerService.getLoggedUser().getUsername()).stream().filter(game -> ((game.getSecondPlayer() != null || game.getSecondPlayer().getUsername() != null) && (game.getGameStatus() == GameStatus.IN_PROGRESS || game.getGameStatus() == GameStatus.PAUSED))).collect(Collectors.toList());
    }

    /**
     * get all games
     *
     * @return
     */
    @GetMapping("/all")
    public List<GameDTO> getAllGames() {
        return gameService.getGames();
    }

    /**
     * return games by status
     *
     * @param gameStatus
     * @return
     */
    @GetMapping("/status/{gameStatus}")
    public List<GameDTO> getAllGamesByStatusType(@PathVariable GameStatus gameStatus) {
        return gameService.getGamesByGameStatus(gameStatus);
    }

    /**
     * return games by type
     *
     * @param gameType
     * @return
     */
    @GetMapping("/type/{gameType}")
    public List<GameDTO> getAllGamesByGameType(@PathVariable GameType gameType) {
        return gameService.getGamesByGameType(gameType);
    }

    /**
     * return games by type and status
     *
     * @param gameType
     * @return
     */
    @GetMapping("/{gameType}/{gameStatus}")
    public List<GameDTO> getGamesByGameTypeAndGameStatus(@PathVariable GameType gameType, @PathVariable GameStatus gameStatus) {
        return gameService.getGamesByGameType(gameType).stream().filter(game -> game.getGameStatus() == gameStatus).collect(Collectors.toList());
    }

    /**
     * get finished games by type
     *
     * @param gameType
     * @return
     */
    @GetMapping("/{gameType}/finished")
    public List<GameDTO> getFinishedGamesByType(@PathVariable GameType gameType) {
        return gameService.getFinishedGames().stream().filter(game -> game.getGameType() == gameType).collect(Collectors.toList());
    }

    /**
     * get all finished games
     *
     * @return
     */
    @GetMapping("/finished")
    public List<GameDTO> getFinishedGames() {
        return gameService.getFinishedGames();
    }

    /**
     * return games by player
     *
     * @param username
     * @return
     */
    @GetMapping("/player/{username}")
    public List<GameDTO> getAllGamesByUsername(@PathVariable String username) {
        return gameService.getGamesByUsername(username);
    }

    /**
     * return games by both players
     *
     * @param username1
     * @param username2
     * @return
     */
    @GetMapping("/players/{username1}/{username2}")
    public List<GameDTO> getAllGamesByBothUsernames(@PathVariable String username1, @PathVariable String username2) {
        return gameService.getGamesByBothUsernames(username1, username2);
    }

    /**
     * get game by id
     */
    @GetMapping("/{gameId}")
    public GameDTO getGame(@PathVariable Long gameId) {
        return gameService.getGameById(gameId);
    }

    /**
     * delete game by id
     */
    @DeleteMapping("/{gameId}")
    public void deleteGame(@PathVariable Long gameId) {
        // only players who belongs the game can delete the game
        if (gameService.getGameById(gameId).hasPlayer(playerService.getLoggedUser())) {
            gameService.deleteGameById(gameId);
        }
    }

    /**
     * join to specific game
     *
     * @param gameId
     * @return
     */
    @PostMapping("/{gameId}/join")
    public GameDTO joinGame(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        gameService.joinGame(gameId, loggedPlayer);
        return gameService.getGameById(gameId);
    }

    /**
     * receive game notifications
     *
     * @param gameId
     * @return game event emitter
     */
    @GetMapping("/{gameId}/notification")
    public SseEmitter gameNotifications(@PathVariable Long gameId) {
        logger.info("returning emitter for game " + gameId);
        PlayerDTO loggedUser = playerService.getLoggedUser();
        // check if game is active
        // only registered players can access notifications
        if (gameService.gameExists(gameId) && gameService.getGameById(gameId).hasPlayer(loggedUser)) {
            return eventSourceService.register(gameId); // return sse emitter
        }
        return null;
    }

    /**
     * user pings as sign of connectivity -> on each request an alive sse is sent to opponent
     * listening to route /{gameId}/notification
     *
     * @param gameId
     */
    @GetMapping("/{gameId}/alive")
    @EventListener
    public void alive(@PathVariable Long gameId) {
        PlayerDTO loggedUser = playerService.getLoggedUser();
        // System.out.println(String.format("%s user is alive in game %s", loggedUser.getUsername(), gameId.toString()));
        // send alive event to opponent
        EventSourceObserverService.send(gameId, new AliveEvent(loggedUser.getUsername()));
    }

    /**
     * resume specific game - only user in game can resume it
     *
     * @param gameId
     * @return
     */
    @PostMapping("/{gameId}/resume")
    public void resumeGame(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO gameToResume = gameService.getGameById(gameId);
        // only player in game can resume game!
        if (gameToResume.hasPlayer(loggedPlayer)) {
            gameService.resumeGame(gameId, loggedPlayer);
            // notify opponent
            EventSourceObserverService.send(gameId, new GameStatusEvent(GameStatus.IN_PROGRESS));
        }
    }

    /**
     * pause specific game - only user in game can pause it
     *
     * @param gameId
     */
    @PostMapping("/{gameId}/pause")
    public void pauseGame(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO gameToPause = gameService.getGameById(gameId);
        // only player in game can pause game!
        if (gameToPause.hasPlayer(loggedPlayer)) {
            gameService.pauseGame(gameId);
            EventSourceObserverService.send(gameId, new GameStatusEvent(GameStatus.PAUSED));
            return;
        }
        logger.info(String.format("Player %s tried to pause game %d he does not belong", loggedPlayer.getUsername(), gameToPause.getId()));
    }

    /**
     * resign from game
     *
     * @param gameId
     */
    @PostMapping("/{gameId}/resign")
    public void resignGame(@PathVariable Long gameId) {
        PlayerDTO looser = playerService.getLoggedUser();
        GameDTO gameToResign = gameService.getGameById(gameId);
        if (gameToResign.hasPlayer(looser)) {
            gameService.resignGame(gameId, looser);
            EventSourceObserverService.send(gameId, new GameStatusEvent(gameService.getGameById(gameId).getGameStatus()));
            EventSourceObserverService.deleteEmitters(gameId);
        }
    }

    /**
     * finish specific game - last played player wins
     *
     * @param gameId
     */
    @PostMapping("/{gameId}/finish")
    public void finishGame(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO gameToFinish = gameService.getGameById(gameId);
        // only player in game can pause game!
        if (gameToFinish.hasPlayer(loggedPlayer)) {
            gameService.finishGame(gameId);
            EventSourceObserverService.send(gameId, new GameStatusEvent(gameService.getGameById(gameId).getGameStatus()));
            EventSourceObserverService.deleteEmitters(gameId);
        }
    }

    /**
     * return true if it's logged in player's turn, otherwise return false
     */
    @GetMapping("/{gameId}/turn")
    public boolean isTurn(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        return gameService.turn(gameId, loggedPlayer);
    }

    /**
     * return opponent player of game
     *
     * @param gameId
     * @return
     */
    @GetMapping("/{gameId}/opponent")
    public PlayerDTO getOpponent(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO game = gameService.getGameById(gameId);
        if (game == null) {
            return null;
        }
        if (!game.getFirstPlayer().equals(loggedPlayer)) {
            return game.getFirstPlayer();
        } else {
            return game.getSecondPlayer();
        }
    }

    /**
     * get my color
     */
    @GetMapping("/{gameId}/color")
    public String getColor(@PathVariable Long gameId) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO game = gameService.getGameById(gameId);
        if (game == null) {
            return null;
        }
        if (game.getFirstPlayer().equals(loggedPlayer)) {
            return PieceColor.WHITE.toString();
        } else {
            return PieceColor.BLACK.toString();
        }
    }

}
