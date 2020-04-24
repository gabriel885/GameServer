package com.web.gameserver.service;

import com.web.gameserver.api.dto.GameDTO;
import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.Game;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.api.model.GameType;
import com.web.gameserver.eventsource.events.MessageEvent;
import com.web.gameserver.exception.GameNotFoundException;
import com.web.gameserver.repository.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GameService {

    private static final Logger logger = LoggerFactory.getLogger(GameService.class);
    @Autowired
    private final GameRepository gameRepository;

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    /**
     * create a new game
     *
     * @param firstPlayer - first player that initialize the game
     * @param gameType
     * @return
     */
    public GameDTO createNewGame(PlayerDTO firstPlayer, GameType gameType) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setGameType(gameType);
        gameDTO.setFirstPlayer(firstPlayer);
        gameDTO.setCreated(new Date());
        // initial game is waiting for an opponent
        gameDTO.setGameStatus(GameStatus.WAITING_FOR_OPPONENT);
        logger.info(String.format("Created Game: \n%s \n", gameDTO.toString()));
        Game saved = gameRepository.save(new Game(gameDTO));
        gameDTO.setId(saved.getId()); // set ID to gameDTO
        return gameDTO;
    }

    /**
     * joining second player to game
     *
     * @param gameId
     * @param playerToJoin
     */
    public void joinGame(Long gameId, PlayerDTO playerToJoin) {
        if (this.gameExists(gameId)) { // check that game exists
            GameDTO gameToJoin = this.getGameById(gameId);
            // check that game is waiting for an opponent
            // check that player to join is not already joined the game
            if (gameToJoin.getGameStatus() == GameStatus.WAITING_FOR_OPPONENT && !gameToJoin.hasPlayer(playerToJoin)) {
                gameToJoin.setSecondPlayer(playerToJoin);
                if ((int) Math.floor(Math.random()) == 1) {
                    // update last played user to random
                    gameToJoin.setLastPlayedPlayer(playerToJoin.getUsername());
                } else {
                    gameToJoin.setLastPlayedPlayer(gameToJoin.getFirstPlayer().getUsername());
                }
                gameToJoin.setGameStatus(GameStatus.IN_PROGRESS);
                // commit database
                Game saved = gameRepository.save(new Game(gameToJoin));
                logger.info("Game saved: %s", new GameDTO(saved).toString());
                logger.info(String.format("Player %s joined game %d", playerToJoin.getUsername(), gameToJoin.getId()));

                EventSourceObserverService.send(gameId, new MessageEvent(String.format("Player %s joined game %d", playerToJoin.getUsername(), gameToJoin.getId())));
            }
        }
    }

    /**
     * resume game
     *
     * @param gameId
     * @param playerToJoin
     */
    public void resumeGame(Long gameId, PlayerDTO playerToJoin) {
        if (this.gameExists(gameId)) {
            GameDTO gameToResume = this.getGameById(gameId);
            // check that game is waiting for an opponent
            if (gameToResume.getGameStatus() == GameStatus.PAUSED) {
                logger.info(String.format("Game %d resumed", gameToResume.getId()));
                gameToResume.setGameStatus(GameStatus.IN_PROGRESS);
                // commit database
                gameRepository.save(new Game(gameToResume));
            }
        }
    }

    /**
     * pause game
     *
     * @param gameId
     */
    public void pauseGame(Long gameId) {
        if (this.gameExists(gameId)) {
            GameDTO gameToPause = this.getGameById(gameId);
            // check that game is in progress
            if (gameToPause.getGameStatus() == GameStatus.IN_PROGRESS) {
                gameToPause.setGameStatus(GameStatus.PAUSED);
                // commit database
                gameRepository.save(new Game(gameToPause));
                logger.info(String.format("Game %d paused", gameToPause.getId()));
            }
        }
    }

    /**
     * resign from game, receives player who resigns and game Id
     *
     * @param gameId
     */
    public void resignGame(Long gameId, PlayerDTO looser) {
        if (this.gameExists(gameId)) {
            GameDTO gameToFinish = this.getGameById(gameId);
            if (gameToFinish.getGameStatus()==GameStatus.FIRST_PLAYER_WON || gameToFinish.getGameStatus()==GameStatus.SECOND_PLAYER_WON ){
                return;
            }
            EventSourceObserverService.send(gameId, new MessageEvent(String.format("Player %s resigned from game %d !", looser.getUsername(), gameToFinish.getId())));
            // opponent wins game
            if (gameToFinish.getFirstPlayer().getUsername().equals(looser.getUsername())) {
                gameToFinish.setGameStatus(GameStatus.SECOND_PLAYER_WON);
            } else {
                gameToFinish.setGameStatus(GameStatus.FIRST_PLAYER_WON);
            }
            // commit database
            gameRepository.save(new Game(gameToFinish));
            logger.info(String.format("Game %d finished", gameToFinish.getId()));
        }
    }

    /**
     * finish game
     *
     * @param gameId
     */
    public void finishGame(Long gameId) {
        if (this.gameExists(gameId)) {
            GameDTO gameToFinish = this.getGameById(gameId);
            EventSourceObserverService.send(gameId, new MessageEvent(String.format("Player %s won game %d !", gameToFinish.getLastPlayedPlayer(), gameToFinish.getId())));
            // last played player is the Winner!
            if (gameToFinish.getLastPlayedPlayer().equals(gameToFinish.getFirstPlayer().getUsername())) {
                gameToFinish.setGameStatus(GameStatus.FIRST_PLAYER_WON);
            } else {
                gameToFinish.setGameStatus(GameStatus.SECOND_PLAYER_WON);
            }
            // commit database
            gameRepository.save(new Game(gameToFinish));
            logger.info(String.format("Game %d finished", gameToFinish.getId()));
        }
    }

    /**
     * get game by ID
     *
     * @param gameId
     * @return
     */
    public GameDTO getGameById(Long gameId) {
        GameDTO gameDTO = new GameDTO();
        if (!this.gameExists(gameId)) {
            logger.warn(new GameNotFoundException(String.format("Couldn't find game with id %d", gameId)).toString());
            return gameDTO;
        } else {
            Game game = gameRepository.findById(gameId).get();
            return new GameDTO(game);
        }
    }

    /**
     * get all games
     *
     * @return
     */
    public List<GameDTO> getGames() {
        return gameRepository.findAll().stream().map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * get all active games -> games with status IN_PROGRESS
     *
     * @return
     */
    public List<GameDTO> getActiveGames() {
        return gameRepository.findAll().stream().filter(game -> game.getGameStatus() == GameStatus.IN_PROGRESS).map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * get all finished games
     *
     * @return
     */
    public List<GameDTO> getFinishedGames() {
        List<GameDTO> firstPlayerWon = this.getGamesByGameStatus(GameStatus.FIRST_PLAYER_WON);
        List<GameDTO> secondsPlayerWon = this.getGamesByGameStatus(GameStatus.SECOND_PLAYER_WON);
        return Stream.concat(firstPlayerWon.stream(), secondsPlayerWon.stream())
                .collect(Collectors.toList());
    }

    /**
     * get games by game type
     *
     * @param gameType
     * @return
     */
    public List<GameDTO> getGamesByGameType(GameType gameType) {
        return gameRepository.findAll().stream().filter(game -> game.getGameType() == gameType).map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * get game by game status
     *
     * @param gameStatus
     * @return
     */
    public List<GameDTO> getGamesByGameStatus(GameStatus gameStatus) {
        return gameRepository.findAll().stream().filter(game -> game.getGameStatus() == gameStatus).map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * get games by game type and game status
     *
     * @param gameType
     * @param gameStatus
     * @return
     */
    public List<GameDTO> getGamesByGameTypeAndGameStatus(GameType gameType, GameStatus gameStatus) {
        return gameRepository.findByGameTypeAndGameStatus(gameType, gameStatus).stream().map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * get games by username
     *
     * @param username
     * @return
     */
    public List<GameDTO> getGamesByUsername(String username) {
        return gameRepository.findAll().stream().map(game -> new GameDTO(game)).filter(game -> game.hasPlayer(new PlayerDTO().setUsername(username))).collect(Collectors.toList());
    }

    /**
     * get games where username1 played against username2
     *
     * @param username1
     * @param username2
     * @return
     */
    public List<GameDTO> getGamesByBothUsernames(String username1, String username2) {
        return gameRepository.findAll().stream().filter(game -> (game.getFirst_player().getUsername().equals(username1) && game.getSecond_player().getUsername().equals(username2)) || (game.getFirst_player().getUsername().equals(username2) && game.getSecond_player().getUsername().equals(username1))).map(game -> new GameDTO(game)).collect(Collectors.toList());
    }

    /**
     * check if game Id is an active game
     *
     * @param gameId
     * @return
     */
    public boolean gameExists(Long gameId) {
        return gameRepository.existsById(gameId);
    }

    /**
     * delete game by game id
     *
     * @param gameId
     */
    public void deleteGameById(Long gameId) {
        logger.info(String.format("Deleting game %s", gameId));
        gameRepository.deleteById(gameId);
    }

    /**
     * update last player played in Game (choose opponent)
     *
     * @param gameId
     */
    public void updateLastPlayed(Long gameId) {
        GameDTO game = this.getGameById(gameId);
        if (game.getLastPlayedPlayer().equals(game.getFirstPlayer().getUsername())) {
            game.setLastPlayedPlayer(game.getSecondPlayer().getUsername());
        } else {
            game.setLastPlayedPlayer(game.getFirstPlayer().getUsername());
        }
        gameRepository.save(new Game(game)); // persist
        logger.info(String.format("Game [%d] updated current turn %s", game.getId(), game.getLastPlayedPlayer()));
    }

    /**
     * get all chess games
     *
     * @return
     */
    public List<GameDTO> getChessGames() {
        return this.getGamesByGameType(GameType.CHESS);
    }

    /**
     * return true if it's player's turn, otherwise return false
     *
     * @param gameId
     * @param playersTurn
     * @return
     */
    public boolean turn(Long gameId, PlayerDTO playersTurn) {
        String lastPlayed = this.getGameById(gameId).getLastPlayedPlayer().trim();
        if (lastPlayed != null) {
            return lastPlayed.equals(playersTurn.getUsername().trim());
        }
        return false;
    }
}
