package com.web.gameserver.api.model;

public enum PieceColor {
    RED {
        @Override
        public String toString() {
            return "RED";
        }
    }, BLACK {
        @Override
        public String toString() {
            return "BLACK";
        }
    }, WHITE {
        @Override
        public String toString() {
            return "WHITE";
        }
    }
}
