package com.web.gameserver.api.controller.admin;

import com.web.gameserver.api.dto.GameDTO;
import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.service.GameService;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/player")
public class AdminPlayerController {

    private static final Logger logger = LoggerFactory.getLogger(AdminPlayerController.class);

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    /**
     * get player by username
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}")
    public ResponseEntity<PlayerDTO> getPlayer(@PathVariable String username) {
        logger.debug(String.format("admin getting user %s", username));
        PlayerDTO player = playerService.getPlayerByUsername(username);
        if (player.getUsername() == null) {
            return new ResponseEntity(player, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(player, HttpStatus.OK);
    }

    /**
     * delete player by username
     *
     * @param username
     */
    @DeleteMapping("/{username}")
    public void deletePlayerByUsername(@PathVariable String username) {
        logger.debug(String.format("admin deleting user %s", username));
        playerService.deletePlayerByUsername(username);
    }

    /**
     * get all games for player
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}/game")
    public List<GameDTO> getUsernameGames(@PathVariable String username) {
        return gameService.getGamesByUsername(username);
    }

    /**
     * get all username's active games
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}/game/{gameStatus}")
    public List<GameDTO> getUsernameActiveGames(@PathVariable String username, @PathVariable GameStatus gameStatus) {
        return gameService.getGamesByUsername(username).stream().filter(game -> game.getGameStatus() == gameStatus).collect(Collectors.toList());
    }

}
