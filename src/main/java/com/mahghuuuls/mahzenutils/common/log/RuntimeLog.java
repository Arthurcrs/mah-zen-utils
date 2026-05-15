package com.mahghuuuls.mahzenutils.common.log;

import com.mahghuuuls.mahzenutils.MahZenUtilsMod;

public final class RuntimeLog {

    private RuntimeLog() {
    }

    public static void invalidMarkerRule(String rule) {
        MahZenUtilsMod.LOGGER.warn("[Runtime] Invalid marker rule: {}", rule);
    }

    public static void invalidStackRule(String kind, String value) {
        MahZenUtilsMod.LOGGER.warn("[Runtime] Invalid stack {} rule: {}", kind, value);
    }

    public static void invalidStackRegistration(String stackId,
                                                int maxStacks,
                                                int defaultDuration,
                                                Object expirationRule,
                                                Object refreshRule) {
        MahZenUtilsMod.LOGGER.warn(
                "[Runtime] Invalid stack registration: id='{}', maxStacks={}, defaultDuration={}, expirationRule={}, refreshRule={}",
                stackId,
                maxStacks,
                defaultDuration,
                expirationRule,
                refreshRule
        );
    }

    public static void duplicateStackRegistration(String stackId) {
        MahZenUtilsMod.LOGGER.warn("[Runtime] Duplicate stack registration for id='{}'", stackId);
    }

    public static void unregisteredStack(String operation, String stackId) {
        MahZenUtilsMod.LOGGER.warn("[Runtime] Unregistered stack id used in {}: '{}'", operation, stackId);
    }
}