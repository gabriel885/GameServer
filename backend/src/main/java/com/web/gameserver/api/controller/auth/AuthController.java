package com.web.gameserver.api.controller.auth;

import com.web.gameserver.GameserverApplication;
import com.web.gameserver.api.dto.PlayerDTO;
import com.web.gameserver.service.PlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @Autowired
    private PlayerService playerService;

    /**
     * return 200 status code for authenticated users
     * otherwise return UNAUTHORIZED status code 401
     *
     * @return
     */
    @GetMapping("/status")
    public ResponseEntity authorized() {
        // check that user is logged in
        if (playerService.loggedIn()) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    /**
     * return user data for authorized users
     * otherwise return 401 status code
     *
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<PlayerDTO> getPlayer() {
        if (playerService.loggedIn()) {
            return new ResponseEntity<>(playerService.getLoggedUser(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
