package com.web.gameserver.repository;

import com.web.gameserver.api.model.Game;
import com.web.gameserver.api.model.GameStatus;
import com.web.gameserver.api.model.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByGameTypeAndGameStatus(GameType GameType, GameStatus GameStatus);
}
