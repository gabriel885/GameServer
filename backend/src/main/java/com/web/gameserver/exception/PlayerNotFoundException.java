package com.web.gameserver.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }

    public String toString() {
        return String.format("PlayerNotFoundException: %s", this.getMessage());
    }
}
