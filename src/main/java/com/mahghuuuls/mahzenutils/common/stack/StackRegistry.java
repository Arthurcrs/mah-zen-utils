package com.mahghuuuls.mahzenutils.common.stack;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StackRegistry {

    private static final Map<String, StackDefinition> DEFINITIONS = new LinkedHashMap<>();

    private StackRegistry() {
    }

    public static boolean register(StackDefinition definition) {
        if (definition == null || !definition.isValid()) {
            return false;
        }

        String stackId = definition.getStackId();
        if (DEFINITIONS.containsKey(stackId)) {
            return false;
        }

        DEFINITIONS.put(stackId, definition);
        return true;
    }

    public static boolean isRegistered(String stackId) {
        return stackId != null && DEFINITIONS.containsKey(stackId);
    }

    public static StackDefinition get(String stackId) {
        if (stackId == null) {
            return null;
        }

        return DEFINITIONS.get(stackId);
    }

    public static Collection<StackDefinition> getAll() {
        return Collections.unmodifiableCollection(DEFINITIONS.values());
    }

    public static void clear() {
        DEFINITIONS.clear();
    }
}