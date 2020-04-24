package com.web.gameserver.repository;

import com.web.gameserver.api.model.ChessMove;
import com.web.gameserver.api.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChessMoveRepository extends JpaRepository<ChessMove, Long> {
    List<ChessMove> findByGame(Game game);
}
