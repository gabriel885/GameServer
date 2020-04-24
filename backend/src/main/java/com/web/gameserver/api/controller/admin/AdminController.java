package com.web.gameserver.api.controller.admin;

import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.api.model.Role;
import com.web.gameserver.service.GameService;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private PlayerService playerService;


    @Autowired
    private GameService gameService;

    /**
     * return status code 200 if logged in user is admin, otherwise return 401 (unauthorized)
     *
     * @return
     */
    @GetMapping(value = "/status")
    public ResponseEntity isAdmin() {
        if (playerService.getLoggedUserRole() == Role.ADMIN) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    /**
     * return all players
     *
     * @return
     */
    @GetMapping(value = "/players")
    public List<PlayerDTO> getAllPlayers() {
        List<PlayerDTO> allPlayers = playerService.getAllPlayers();
        return allPlayers;
    }
}
