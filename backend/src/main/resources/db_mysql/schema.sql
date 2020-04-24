CREATE TABLE iF NOT EXISTS `player` (
                          `username` varchar(30) NOT NULL,
                          `password_hash` varchar(255) NOT NULL,
                          `email` varchar(255) DEFAULT NULL,
                          `role` enum('USER','ADMIN') DEFAULT 'USER',
                          `created` date DEFAULT NULL,
                          `rating` int unsigned NOT NULL DEFAULT '0',
                          PRIMARY KEY (`username`)
);

CREATE TABLE IF NOT EXISTS `games` (
                         `game_id` int NOT NULL AUTO_INCREMENT,
                         `first_player_username` varchar(30) NOT NULL,
                         `second_player_username` varchar(30) DEFAULT NULL,
                         `created` date NOT NULL,
                         `game_status` enum('WAITING_FOR_OPPONENT','IN_PROGRESS','PAUSED','FIRST_PLAYER_WON','SECOND_PLAYER_WON') NOT NULL,
                         `game_type` enum('CHESS','CHECKERS') NOT NULL,
                         `last_played` varchar(30) DEFAULT NULL,
                         `player_won` varchar(30) DEFAULT NULL,
                         PRIMARY KEY (`game_id`),
                         KEY `first_player_username_fk` (`first_player_username`),
                         KEY `second_player_username_fk` (`second_player_username`),
                         CONSTRAINT `first_player_username_fk` FOREIGN KEY (`first_player_username`) REFERENCES `player` (`username`),
                         CONSTRAINT `second_player_username_fk` FOREIGN KEY (`second_player_username`) REFERENCES `player` (`username`)
);

CREATE TABLE IF NOT EXISTS `chessmove` (
                             `move_id` int NOT NULL AUTO_INCREMENT,
                             `player` varchar(30) NOT NULL,
                             `game_id` int NOT NULL,
                             `from_board_row` int NOT NULL,
                             `from_board_column` int NOT NULL,
                             `to_board_row` int NOT NULL,
                             `to_board_column` int NOT NULL,
                             `piece_type` enum('KING','QUEEN','ROOK','BISHOP','KNIGHT','PAWN') NOT NULL,
                             `piece_color` enum('RED','BLACK','WHITE') NOT NULL,
                             `move_count` INT NOT NULL,
                             `piece_id` INT NOT NULL,
                             `created` date NOT NULL,
                             PRIMARY KEY (`move_id`),
                             KEY `chessmove_games_game_id_fk` (`game_id`),
                             KEY `chessmove_player_username_fk` (`player`),
                             CONSTRAINT `chessmove_games_game_id_fk` FOREIGN KEY (`game_id`) REFERENCES `games` (`game_id`) ON DELETE CASCADE ON UPDATE CASCADE,
                             CONSTRAINT `chessmove_player_username_fk` FOREIGN KEY (`player`) REFERENCES `player` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
);
