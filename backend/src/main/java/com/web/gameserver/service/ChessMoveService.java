package com.web.gameserver.service;

import com.web.gameserver.api.dto.ChessMoveDTO;
import com.web.gameserver.api.model.ChessMove;
import com.web.gameserver.api.model.Game;
import com.web.gameserver.repository.ChessMoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChessMoveService {

    private final ChessMoveRepository chessMoveRepository;

    @Autowired
    private GameService gameService;

    public ChessMoveService(ChessMoveRepository chessMoveRepository) {
        this.chessMoveRepository = chessMoveRepository;
    }

    /**
     * add chess move to game
     *
     * @param chessMoveDTO - chess move to add
     * @return
     */
    public void addMove(ChessMoveDTO chessMoveDTO) {
        this.chessMoveRepository.save(new ChessMove(chessMoveDTO));
    }

    /**
     * get all moves in a game
     *
     * @param gameId
     * @return
     */
    public List<ChessMoveDTO> getGameMoves(Long gameId) {
        List<ChessMove> moves = chessMoveRepository.findByGame(new Game(gameService.getGameById(gameId)));
        return moves.stream().map(move -> new ChessMoveDTO(move)).collect(Collectors.toList());
    }
}
