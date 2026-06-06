package com.mahghuuuls.mahzenutils.common.stack;

public enum StackRefreshRule {
    RESET,
    ADD,
    PRESERVE;

    public static StackRefreshRule fromZen(String value) {
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