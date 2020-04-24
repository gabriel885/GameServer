package com.web.gameserver.service;

import com.web.gameserver.api.model.Player;
import com.web.gameserver.api.model.Role;
import com.web.gameserver.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

    @Autowired
    private PlayerRepository playerRepository;

    /**
     * user for login - loading user
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = playerRepository.findByUsername(username);
        if (player == null) {
            throw new UsernameNotFoundException(String.format("Couldn't find username %s", username));
        }
        logger.debug(String.format("loaded username %s", player.toString()));
        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (player.getRole() == Role.ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
            logger.debug("adding admin ROLE");
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        return buildUserForAuthentication(player, authorities);
    }

    private UserDetails buildUserForAuthentication(Player player, java.util.List<GrantedAuthority> authorities) {
        logger.debug(String.format("Building user %s %s for authentication", player.getUsername(), authorities.toString()));
        return new org.springframework.security.core.userdetails.User(player.getUsername(), player.getPassword(),
                true, true, true, true, authorities);
    }
}

