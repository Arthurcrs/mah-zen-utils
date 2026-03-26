package com.mahghuuuls.mahghuuulszenutils.common.stack;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.debug.DebugNotifier;
import com.mahghuuuls.mahghuuulszenutils.common.log.RuntimeLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class StackService {

    private StackService() {
    }

    public static boolean register(String stackId,
                                   int maxStacks,
                                   int defaultDuration,
                                   StackExpirationRule expirationRule,
                                   StackRefreshRule refreshRule) {
        StackDefinition definition = new StackDefinition(
                stackId,
                maxStacks,
                defaultDuration,
                expirationRule,
                refreshRule
        );

        if (!definition.isValid()) {
            RuntimeLog.invalidStackRegistration(stackId, maxStacks, defaultDuration, expirationRule, refreshRule);
            return false;
        }

        boolean registered = StackRegistry.register(definition);
        if (!registered) {
            RuntimeLog.duplicateStackRegistration(stackId);
            return false;
        }

        DebugNotifier.stackGlobal("registered stack '" + stackId + "'"
                + " max=" + maxStacks
                + " duration=" + defaultDuration
                + " expiration=" + expirationRule
                + " refresh=" + refreshRule);
        return true;
    }

    public static boolean isRegistered(String stackId) {
        return StackRegistry.isRegistered(stackId);
    }

    public static int getStacks(EntityLivingBase entity, String stackId) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, "getStacks");
        if (state == null || definition == null) {
            return 0;
        }

        return state.getCount(stackId);
    }

    public static int getRemainingStackTime(EntityLivingBase entity, String stackId) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, "getRemainingStackTime");
        if (state == null || definition == null) {
            return 0;
        }

        return state.getRemainingTicks(stackId);
    }

    public static void clearStacks(EntityLivingBase entity, String stackId) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, "clearStacks");
        if (state == null || definition == null) {
            return;
        }

        boolean existed = state.has(stackId);
        state.remove(stackId);

        if (existed) {
            DebugNotifier.stack(entity, "cleared stack '" + stackId + "'");
        }
    }

    public static void setStackCount(EntityLivingBase entity, String stackId, int count) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, "setStackCount");
        if (state == null || definition == null) {
            return;
        }

        if (count <= 0) {
            state.remove(stackId);
            DebugNotifier.stack(entity, "set stack count '" + stackId + "' -> 0 (removed)");
            return;
        }

        int clamped = Math.min(count, definition.getMaxStacks());
        StackInstance instance = state.getOrCreate(stackId);
        instance.setCount(clamped);

        if (definition.isTimed()) {
            if (instance.getRemainingTicks() <= 0) {
                instance.setRemainingTicks(definition.getDefaultDuration());
            }
        } else {
            instance.setRemainingTicks(0);
        }

        DebugNotifier.stack(entity, "set stack count '" + stackId + "' -> " + clamped);
    }

    public static void setStackTime(EntityLivingBase entity, String stackId, int ticks) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, "setStackTime");
        if (state == null || definition == null) {
            return;
        }

        if (!definition.isTimed()) {
            return;
        }

        StackInstance instance = state.get(stackId);
        if (instance == null || instance.isEmpty()) {
            return;
        }

        if (ticks <= 0) {
            handleExpiration(state, definition, stackId, instance, entity);
            state.pruneEmpty();
            return;
        }

        instance.setRemainingTicks(ticks);
        DebugNotifier.stack(entity, "set stack time '" + stackId + "' -> " + ticks);
    }

    public static void addStacks(EntityLivingBase entity, String stackId, int amount) {
        applySignedDelta(entity, stackId, amount, "addStacks");
    }

    public static void removeStacks(EntityLivingBase entity, String stackId, int amount) {
        applySignedDelta(entity, stackId, -amount, "removeStacks");
    }

    public static void tick(EntityLivingBase entity) {
        StackState state = getState(entity);
        if (state == null || state.isEmpty()) {
            return;
        }

        for (String stackId : state.view().keySet().toArray(new String[0])) {
            StackDefinition definition = StackRegistry.get(stackId);
            if (definition == null || !definition.isTimed()) {
                continue;
            }

            StackInstance instance = state.get(stackId);
            if (instance == null || instance.isEmpty()) {
                continue;
            }

            int next = instance.getRemainingTicks() - 1;
            instance.setRemainingTicks(next);

            if (next <= 0) {
                handleExpiration(state, definition, stackId, instance, entity);
            }
        }

        state.pruneEmpty();
    }

    private static void applySignedDelta(EntityLivingBase entity, String stackId, int delta, String source) {
        StackState state = getState(entity);
        StackDefinition definition = requireDefinition(stackId, source);
        if (state == null || definition == null || delta == 0) {
            return;
        }

        if (delta > 0) {
            applyAddition(state, definition, stackId, delta, entity, source);
        } else {
            applyRemoval(state, definition, stackId, -delta, entity, source);
        }

        state.pruneEmpty();
    }

    private static void applyAddition(StackState state,
                                      StackDefinition definition,
                                      String stackId,
                                      int amount,
                                      EntityLivingBase entity,
                                      String source) {
        StackInstance instance = state.getOrCreate(stackId);
        int before = instance.getCount();
        int after = Math.min(definition.getMaxStacks(), before + amount);

        if (after <= 0) {
            state.remove(stackId);
            return;
        }

        instance.setCount(after);
        applyRefreshRuleOnAdd(instance, definition);

        DebugNotifier.stack(entity,
                source + " effective add '" + stackId + "' "
                        + before + " -> " + after
                        + " time=" + instance.getRemainingTicks());
    }

    private static void applyRemoval(StackState state,
                                     StackDefinition definition,
                                     String stackId,
                                     int amount,
                                     EntityLivingBase entity,
                                     String source) {
        StackInstance instance = state.get(stackId);
        if (instance == null || instance.isEmpty()) {
            return;
        }

        int before = instance.getCount();
        int after = Math.max(0, before - amount);

        if (after <= 0) {
            state.remove(stackId);
            DebugNotifier.stack(entity,
                    source + " effective remove '" + stackId + "' "
                            + before + " -> 0 (removed)");
            return;
        }

        instance.setCount(after);

        DebugNotifier.stack(entity,
                source + " effective remove '" + stackId + "' "
                        + before + " -> " + after
                        + " time=" + instance.getRemainingTicks());
    }

    private static void applyRefreshRuleOnAdd(StackInstance instance, StackDefinition definition) {
        if (!definition.isTimed()) {
            instance.setRemainingTicks(0);
            return;
        }

        int defaultDuration = definition.getDefaultDuration();
        int current = instance.getRemainingTicks();

        switch (definition.getRefreshRule()) {
            case RESET:
                instance.setRemainingTicks(defaultDuration);
                break;
            case ADD:
                instance.setRemainingTicks(current + defaultDuration);
                break;
            case PRESERVE:
                if (current <= 0) {
                    instance.setRemainingTicks(defaultDuration);
                }
                break;
        }
    }

    private static void handleExpiration(StackState state,
                                         StackDefinition definition,
                                         String stackId,
                                         StackInstance instance,
                                         EntityLivingBase entity) {
        switch (definition.getExpirationRule()) {
            case CLEAR_ALL:
                state.remove(stackId);
                DebugNotifier.stack(entity, "stack expired '" + stackId + "' -> clear_all");
                break;
            case DECAY_ONE:
                int nextCount = instance.getCount() - 1;
                if (nextCount <= 0) {
                    state.remove(stackId);
                    DebugNotifier.stack(entity, "stack expired '" + stackId + "' -> decay_one to 0 (removed)");
                } else {
                    instance.setCount(nextCount);
                    instance.setRemainingTicks(definition.getDefaultDuration());
                    DebugNotifier.stack(entity,
                            "stack expired '" + stackId + "' -> decay_one, count=" + nextCount
                                    + ", reset time=" + definition.getDefaultDuration());
                }
                break;
            case PERMANENT:
                instance.setRemainingTicks(0);
                break;
        }
    }

    private static StackDefinition requireDefinition(String stackId, String operation) {
        if (stackId == null) {
            return null;
        }

        StackDefinition definition = StackRegistry.get(stackId);
        if (definition == null) {
            RuntimeLog.unregisteredStack(operation, stackId);
        }
        return definition;
    }

    private static StackState getState(EntityLivingBase entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof EntityPlayer) {
            if (PlayerStateProvider.CAPABILITY == null) {
                return null;
            }

            IPlayerState playerState = entity.getCapability(PlayerStateProvider.CAPABILITY, null);
            return playerState != null ? playerState.getStackState() : null;
        }

        if (EntityStateProvider.CAPABILITY == null) {
            return null;
        }

        IEntityState entityState = entity.getCapability(EntityStateProvider.CAPABILITY, null);
        return entityState != null ? entityState.getStackState() : null;
    }
}
