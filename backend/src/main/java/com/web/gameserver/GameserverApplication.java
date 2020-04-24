package com.web.gameserver;


import com.web.gameserver.api.model.Player;
import com.web.gameserver.api.model.Role;
import com.web.gameserver.repository.PlayerRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GameserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameserverApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(PlayerRepository repository) {
        // save admin credentials first
        return args -> {
            //repository.save(new Player("admin", "admin@example.com", new BCryptPasswordEncoder().encode("example"), Role.ADMIN, new Date(), 0));
            Player admin = new Player();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(new BCryptPasswordEncoder().encode("example"));
            admin.setRole(Role.ADMIN);
            admin.setRating(0);
            repository.save(admin);
        };
    }

}
