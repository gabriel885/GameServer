package com.web.gameserver.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }

    public String toString() {
        return String.format("GameNotFoundException: %s", this.getMessage());
    }
}
