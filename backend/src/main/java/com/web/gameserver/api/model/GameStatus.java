package com.web.gameserver.api.model;


public enum GameStatus {
    WAITING_FOR_OPPONENT {
        @Override
        public String toString() {
            return "WAITING_FOR_OPPONENT";
        }
    }, IN_PROGRESS {
        @Override
        public String toString() {
            return "IN_PROGRESS";
        }
    }, PAUSED {
        @Override
        public String toString() {
            return "PAUSED";
        }
    }, FIRST_PLAYER_WON {
        @Override
        public String toString() {
            return "FIRST_PLAYER_WON";
        }
    }, SECOND_PLAYER_WON {
        @Override
        public String toString() {
            return "SECOND_PLAYER_WON";
        }
    }
}
