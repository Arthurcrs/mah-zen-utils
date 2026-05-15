package com.mahghuuuls.mahzenutils.common.marker;

public enum MarkerRule {
    RESET,
    ADD,
    PRESERVE;

    public static MarkerRule fromZen(String value) {
        if (value == null) {
            return null;
        }

        switch (value.trim().toLowerCase()) {
            case "reset":
                return RESET;
            case "add":
                return ADD;
            case "preserve":
                return PRESERVE;
            default:
                return null;
        }
    }
}