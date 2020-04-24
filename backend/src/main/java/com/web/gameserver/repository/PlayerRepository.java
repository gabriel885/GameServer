package com.web.gameserver.repository;

import com.web.gameserver.api.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    Player findByUsername(String username);

    Player findByEmail(String email);
}
