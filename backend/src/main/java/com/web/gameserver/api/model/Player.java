package com.web.gameserver.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.web.gameserver.api.dto.PlayerDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "player")
@Setter
@Getter
@NoArgsConstructor
public class Player {

    @Id
    @Length(min = 5, max = 25, message = "Username must have at least 5 characters")
    @Column(name = "username")
    private String username;

    @NotNull
    @Email(message = "Please provide valid email")
    @Column(name = "email")
    private String email;

    @NotNull
    @Length(min = 5, message = "Password must have at least 5 characters")
    @Column(name = "password_hash")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "created")
    private Date created;

    @NotNull
    @Column(name = "rating")
    private int rating = 0;

    // any change that is made here => cascades to all associated games
    // if player is deleted -> game is deleted
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "first_player")
    @JsonManagedReference
    private Set<Game> firstPlayerGames;


    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "second_player")
    @JsonManagedReference
    private Set<Game> secondPlayerGames;


    public Player(PlayerDTO playerDTO) {
        this();
        if (playerDTO == null) {
            return;
        }
        setUsername(playerDTO.getUsername());
        setEmail(playerDTO.getEmail());
        setPassword(playerDTO.getPassword());
        setRating(playerDTO.getRating());
    }

}
