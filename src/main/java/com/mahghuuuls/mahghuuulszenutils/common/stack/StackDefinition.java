package com.mahghuuuls.mahghuuulszenutils.common.stack;

public class StackDefinition {

    private final String stackId;
    private final int maxStacks;
    private final int defaultDuration;
    private final StackExpirationRule expirationRule;
    private final StackRefreshRule refreshRule;

    public StackDefinition(String stackId,
                           int maxStacks,
                           int defaultDuration,
                           StackExpirationRule expirationRule,
                           StackRefreshRule refreshRule) {
        this.stackId = stackId;
        this.maxStacks = maxStacks;
        this.defaultDuration = defaultDuration;
        this.expirationRule = expirationRule;
        this.refreshRule = refreshRule;
    }

    public String getStackId() {
        return stackId;
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }

    public StackExpirationRule getExpirationRule() {
        return expirationRule;
    }

    public StackRefreshRule getRefreshRule() {
        return refreshRule;
    }

    public boolean isTimed() {
        return expirationRule != StackExpirationRule.PERMANENT;
    }

    public boolean isValid() {
        if (stackId == null || stackId.trim().isEmpty()) {
            return false;
        }

        if (maxStacks <= 0) {
            return false;
        }

        if (expirationRule == null || refreshRule == null) {
            return false;
        }

        if (expirationRule == StackExpirationRule.PERMANENT) {
            return defaultDuration == 0;
        }

        return defaultDuration > 0;
    }
}