package com.mahghuuuls.mahghuuulszenutils.common.stack;

public enum StackExpirationRule {
    CLEAR_ALL,
    DECAY_ONE,
    PERMANENT;

    public static StackExpirationRule fromZen(String value) {
        if (value == null) {
            return null;
        }

        switch (value.trim().toLowerCase()) {
            case "clear_all":
                return CLEAR_ALL;
            case "decay_one":
                return DECAY_ONE;
            case "permanent":
                return PERMANENT;
            default:
                return null;
        }
    }
}
