package com.web.gameserver.api.dto;

import com.web.gameserver.api.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PlayerDTO {
    private String username;
    private String password;
    private String email;
    private int rating;

    public PlayerDTO(Player player) {
        this(); // run empty constructor if player is null
        if (player == null) {
            return;
        }
        setUsername(player.getUsername());
        setPassword(player.getPassword());
        setEmail(player.getEmail());
        setRating(player.getRating());
    }

    public PlayerDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public String toString() {
        return String.format("Username: %s password: %s email: %s rating: %d", getUsername(), getPassword(), getEmail(), getRating());
    }

    // compare players by username
    @Override
    public boolean equals(Object o) {
        PlayerDTO player = (PlayerDTO) o;
        if (player == null || player.getUsername() == null || getUsername() == null) {
            return false;
        }
        // compare by username
        return this.getUsername().trim().equals(player.getUsername().trim());
    }
}
