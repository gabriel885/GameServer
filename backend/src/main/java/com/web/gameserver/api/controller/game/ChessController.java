package com.web.gameserver.api.controller.game;

import com.web.gameserver.api.dto.ChessMoveDTO;
import com.web.gameserver.api.dto.GameDTO;
import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.Game;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.api.model.GameType;
import com.web.gameserver.api.model.Player;
import com.web.gameserver.eventsource.events.ChessGameMoveEvent;
import com.web.gameserver.service.ChessMoveService;
import com.web.gameserver.service.EventSourceObserverService;
import com.web.gameserver.service.GameService;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * responsible for managing chess games
 */
@RestController
@RequestMapping("/api/game/chess")
public class ChessController {

    private static final Logger logger = LoggerFactory.getLogger(ChessController.class);

    @Autowired
    HttpSession httpSession;

    @Autowired
    private EventSourceObserverService eventSourceService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ChessMoveService chessMoveService;

    /**
     * get all chess games
     *
     * @return
     */
    @GetMapping
    public List<GameDTO> getChessGames() {
        return gameService.getChessGames();
    }

    /**
     * create new game
     *
     * @return
     */
    @PostMapping("/create")
    public GameDTO createChessGame() {
        PlayerDTO loggedInPlayer = playerService.getLoggedUser();
        // if user has already created game - > return this game
        List<GameDTO> gamesRequested = gameService.getGamesByGameTypeAndGameStatus(GameType.CHESS, GameStatus.WAITING_FOR_OPPONENT).stream().filter(game -> game.hasPlayer(loggedInPlayer)).collect(Collectors.toList());
        if (!gamesRequested.isEmpty()) {
            return gamesRequested.get(0);
        }
        return gameService.createNewGame(loggedInPlayer, GameType.CHESS);
    }

    /**
     * get all moves in chess game
     *
     * @return
     */
    @GetMapping("/{gameId}/moves")
    public List<ChessMoveDTO> getAllMoves(@PathVariable Long gameId) {
        return chessMoveService.getGameMoves(gameId);
    }

    /**
     * add move to game's
     *
     * @param chessMoveDTO
     * @return
     */
    @PostMapping("/{gameId}/move/add")
    public ResponseEntity<HttpStatus> addMove(@PathVariable Long gameId, @Valid @ModelAttribute ChessMoveDTO chessMoveDTO) {
        PlayerDTO loggedPlayer = playerService.getLoggedUser();
        GameDTO game = gameService.getGameById(gameId);

        if (chessMoveDTO == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // check that it's the turn of the logged in user
        if (game.getLastPlayedPlayer().equals(loggedPlayer.getUsername())) {
            logger.info(String.format("[GAME %d] %s adding move %s", gameId, loggedPlayer.getUsername(), chessMoveDTO.toString()));
            chessMoveDTO.setGame(new Game(game));
            chessMoveDTO.setPlayer(new Player(loggedPlayer));
            chessMoveDTO.setCreated(new Date());

            chessMoveService.addMove(chessMoveDTO);
            // update game's last played
            gameService.updateLastPlayed(gameId);
            // sse move event
            EventSourceObserverService.send(gameId, new ChessGameMoveEvent(chessMoveDTO));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        logger.warn(String.format("[GAME %d] %s is not allowed to add move ", gameId, loggedPlayer.getUsername()));
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
