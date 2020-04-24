package com.web.gameserver.api.model;


public enum Role {
    USER {
        @Override
        public String toString() {
            return "USER";
        }
    }, ADMIN {
        @Override
        public String toString() {
            return "ADMIN";
        }
    }, UNKNOWN {
        @Override
        public String toString() {
            return "UNKNOWN";
        }
    }
}
