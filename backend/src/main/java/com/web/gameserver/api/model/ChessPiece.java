package com.web.gameserver.api.model;

public enum ChessPiece {
    KING {
        @Override
        public String toString() {
            return "KING";
        }
    }, QUEEN {
        @Override
        public String toString() {
            return "QUEEN";
        }
    }, ROOK {
        @Override
        public String toString() {
            return "ROOK";
        }
    }, BISHOP {
        @Override
        public String toString() {
            return "BISHOP";
        }
    }, KNIGHT {
        @Override
        public String toString() {
            return "KNIGHT";
        }
    }, PAWN {
        @Override
        public String toString() {
            return "PAWN";
        }
    }

}
