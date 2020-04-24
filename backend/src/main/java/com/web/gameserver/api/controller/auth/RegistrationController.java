package com.web.gameserver.api.controller.auth;


import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.exception.ResourceExistsException;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth/register")
public class RegistrationController {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    @Autowired
    private PlayerService playerService;

    @PostMapping
    public ResponseEntity createNewUser(@Valid @ModelAttribute PlayerDTO player) {
        logger.info("registering player " + player.toString());
        if (playerService.existsByUsername(player.getUsername())) {
            logger.warn(new ResourceExistsException("There is already a user registered with the user name provided").toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already a user registered with the user name provided"); // return 200 status code
        } else if (playerService.existsByEmail(player.getEmail())) {
            logger.warn(new ResourceExistsException("There is already a user registered with the email provided").toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("There is already a user registered with the email provided"); // return 200 status code
        } else {
            // creating a player
            playerService.createUser(player);
            return ResponseEntity.status(HttpStatus.OK).body(player); // return 200 status code
        }
    }
}
