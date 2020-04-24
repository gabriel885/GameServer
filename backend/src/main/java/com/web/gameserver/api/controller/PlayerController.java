package com.web.gameserver.api.controller;

import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);

    @Autowired
    private PlayerService playerService;

    @GetMapping()
    public PlayerDTO getLoggedInUser() {
        return playerService.getLoggedUser();
    }

    @GetMapping("/email/{email}")
    public PlayerDTO getPlayerByEmail(@PathVariable String email) {
        return playerService.getPlayerByEmail(email);
    }

}
