package com.web.gameserver.service;

import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.Player;
import com.web.gameserver.api.model.Role;
import com.web.gameserver.exception.PlayerNotFoundException;
import com.web.gameserver.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * checks if a player exists by a username
     *
     * @param username
     * @return
     */
    public boolean existsByUsername(String username) {
        logger.debug(String.format("Checking if player %s exists by username", username));
        if (this.playerRepository.existsById(username)) {
            logger.debug(String.format("Player %s exists by username", username));
            return true;
        }
        logger.debug(String.format("Player %s does not exist by username", username));
        return false;
    }

    /**
     * checks if a player exists by an email
     *
     * @param email
     * @return
     */
    public boolean existsByEmail(String email) {
        logger.debug(String.format("Checking if player %s exists by email", email));
        if (this.playerRepository.findByEmail(email) != null) {
            logger.debug(String.format("Player %s exists by email", email));
            return true;
        }
        logger.debug(String.format("Player %s does not exist by email", email));
        return false;
    }

    /**
     * get player by username
     *
     * @param username
     * @return
     */
    public PlayerDTO getPlayerByUsername(String username) {
        logger.debug(String.format("Getting user by username %s", username));
        if (!existsByUsername(username)) {
            logger.warn(new PlayerNotFoundException(String.format("User %s does not exist", username)).toString());
            return new PlayerDTO();
        }
        return new PlayerDTO(playerRepository.findByUsername(username));

    }

    /**
     * get player by email
     *
     * @param email
     * @return
     */
    public PlayerDTO getPlayerByEmail(String email) {
        logger.debug(String.format("Getting user by email %s", email));
        if (!existsByEmail(email)) {
            logger.warn(new PlayerNotFoundException(String.format("User with email %s does not exist", email)).toString());
            return new PlayerDTO();
        }
        return new PlayerDTO(playerRepository.findByEmail(email));
    }

    /**
     * get all players
     *
     * @return
     */
    public List<PlayerDTO> getAllPlayers() {
        logger.debug(String.format("Getting all players"));
        return playerRepository.findAll(Sort.by("role")).stream().map(player -> new PlayerDTO(player)).collect(Collectors.toList());
    }

    /**
     * delete player by username
     *
     * @param username
     */
    public void deletePlayerByUsername(String username) {
        logger.info(String.format("Deleting player %s", username));
        playerRepository.deleteById(username);
    }

    /**
     * create new player with role "USER"
     *
     * @param playerDTO
     * @return
     */
    public PlayerDTO createUser(PlayerDTO playerDTO) {
        // encrypt password
        playerDTO.setPassword(new BCryptPasswordEncoder().encode(playerDTO.getPassword()));
        playerRepository.save(new Player(playerDTO));
        logger.info(String.format("Created user %s", playerDTO.toString()));
        return playerDTO;
    }


    /**
     * get logged-in player
     *
     * @return
     */
    public PlayerDTO getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Player principal = playerRepository.findByUsername(auth.getName());
        return new PlayerDTO(principal);
    }

    /**
     * return logged-in player's role
     *
     * @return
     */
    public Role getLoggedUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Player principal = playerRepository.findByUsername(auth.getName());
        if (principal == null) {
            return Role.UNKNOWN;
        }
        return principal.getRole();
    }

    /**
     * return true if user is logged in - otherwise return false
     *
     * @return
     */
    public boolean loggedIn() {
        // SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
        return this.getLoggedUserRole() != Role.UNKNOWN;
    }


}
