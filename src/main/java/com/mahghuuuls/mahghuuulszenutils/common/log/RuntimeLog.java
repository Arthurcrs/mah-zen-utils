package com.mahghuuuls.mahghuuulszenutils.common.log;

import com.mahghuuuls.mahghuuulszenutils.MahZenUtils;

public final class RuntimeLog {

    private RuntimeLog() {
    }

    public static void invalidMarkerRule(String rule) {
        MahZenUtils.LOGGER.warn("[Runtime] Invalid marker rule: {}", rule);
    }

    public static void invalidStackRule(String kind, String value) {
        MahZenUtils.LOGGER.warn("[Runtime] Invalid stack {} rule: {}", kind, value);
    }

    public static void invalidStackRegistration(String stackId,
                                                int maxStacks,
                                                int defaultDuration,
                                                Object expirationRule,
                                                Object refreshRule) {
        MahZenUtils.LOGGER.warn(
                "[Runtime] Invalid stack registration: id='{}', maxStacks={}, defaultDuration={}, expirationRule={}, refreshRule={}",
                stackId,
                maxStacks,
                defaultDuration,
                expirationRule,
                refreshRule
        );
    }

    public static void duplicateStackRegistration(String stackId) {
        MahZenUtils.LOGGER.warn("[Runtime] Duplicate stack registration for id='{}'", stackId);
    }

    public static void unregisteredStack(String operation, String stackId) {
        MahZenUtils.LOGGER.warn("[Runtime] Unregistered stack id used in {}: '{}'", operation, stackId);
    }
}
