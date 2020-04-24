package com.web.gameserver.eventsource.events;

public enum EventType {
    ALIVE {
        @Override
        public String toString() {
            return "alive";
        }
    }, NOT_ALIVE {
        @Override
        public String toString() {
            return "inactive";
        }
    }, GameStatus {
        @Override
        public String toString() {
            return "gameStatus";
        }
    }, ChessGameMove {
        @Override
        public String toString() {
            return "chessGameMove";
        }
    }, CheckersGameMove {
        @Override
        public String toString() {
            return "checkersGameMove";
        }
    }, Message {
        @Override
        public String toString() {
            return "message";
        }
    }
}
