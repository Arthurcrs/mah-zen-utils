package com.mahghuuuls.mahghuuulszenutils.common.marker;

import com.mahghuuuls.mahghuuulszenutils.MahZenUtils;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.debug.DebugNotifier;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MarkerService {

    private MarkerService() {
    }

    public static void markEntity(EntityPlayer owner, String markId, Entity target, int duration, MarkerRule rule) {
        if (owner == null || markId == null || markId.isEmpty() || target == null || rule == null) {
            return;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return;
        }

        UUID targetId = target.getUniqueID();
        int normalizedDuration = Math.max(0, duration);

        MarkerEntry existing = state.getEntry(markId, targetId);

        switch (rule) {
            case RESET: {
                if (normalizedDuration <= 0) {
                    state.remove(markId, targetId);
                    DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " removed by reset(0)");
                    return;
                }

                MarkerEntry entry = state.getOrCreateEntry(markId, targetId);
                entry.setRemainingTicks(normalizedDuration);
                DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " set to " + normalizedDuration + " ticks [reset]");
                return;
            }

            case ADD: {
                if (existing == null) {
                    if (normalizedDuration <= 0) {
                        return;
                    }

                    MarkerEntry entry = state.getOrCreateEntry(markId, targetId);
                    entry.setRemainingTicks(normalizedDuration);
                    DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " created with " + normalizedDuration + " ticks [add]");
                    return;
                }

                int next = existing.getRemainingTicks() + normalizedDuration;
                if (next <= 0) {
                    state.remove(markId, targetId);
                    DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " removed by add <= 0");
                    return;
                }

                existing.setRemainingTicks(next);
                DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " adjusted to " + next + " ticks [add]");
                return;
            }

            case PRESERVE: {
                if (existing != null) {
                    DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " preserved at " + existing.getRemainingTicks() + " ticks");
                    return;
                }

                if (normalizedDuration <= 0) {
                    return;
                }

                MarkerEntry entry = state.getOrCreateEntry(markId, targetId);
                entry.setRemainingTicks(normalizedDuration);
                DebugNotifier.marker(owner, "mark '" + markId + "' on " + describeTarget(target) + " created with " + normalizedDuration + " ticks [preserve]");
            }
        }
    }

    public static List<Entity> getMarkedEntities(EntityPlayer owner, String markId) {
        List<Entity> result = new ArrayList<>();

        if (owner == null || markId == null || markId.isEmpty()) {
            return result;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return result;
        }

        List<UUID> toRemove = new ArrayList<>();

        for (Map.Entry<UUID, MarkerEntry> entry : state.viewTargets(markId).entrySet()) {
            UUID targetId = entry.getKey();
            MarkerEntry markerEntry = entry.getValue();

            if (targetId == null || markerEntry == null || markerEntry.isExpired()) {
                toRemove.add(targetId);
                continue;
            }

            Entity resolved = findLoadedEntity(owner, targetId);
            if (!isValidTarget(resolved)) {
                toRemove.add(targetId);
                continue;
            }

            result.add(resolved);
        }

        for (UUID targetId : toRemove) {
            state.remove(markId, targetId);
        }

        return result;
    }

    public static void clearMarks(EntityPlayer owner, String markId) {
        if (owner == null || markId == null || markId.isEmpty()) {
            return;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return;
        }

        if (state.hasMark(markId)) {
            state.clearMarkId(markId);
            DebugNotifier.marker(owner, "cleared all marks for '" + markId + "'");
        }
    }

    public static void unmarkEntity(EntityPlayer owner, String markId, Entity target) {
        if (owner == null || markId == null || markId.isEmpty() || target == null) {
            return;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return;
        }

        UUID targetId = target.getUniqueID();
        if (state.isMarked(markId, targetId)) {
            state.remove(markId, targetId);
            DebugNotifier.marker(owner, "unmarked '" + markId + "' from " + describeTarget(target));
        }
    }

    public static boolean isMarked(EntityPlayer owner, String markId, Entity target) {
        if (owner == null || markId == null || markId.isEmpty() || target == null) {
            return false;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return false;
        }

        UUID targetId = target.getUniqueID();
        MarkerEntry entry = state.getEntry(markId, targetId);
        if (entry == null || entry.isExpired()) {
            if (entry != null) {
                state.remove(markId, targetId);
            }
            return false;
        }

        if (!isValidTarget(findLoadedEntity(owner, targetId))) {
            state.remove(markId, targetId);
            return false;
        }

        return true;
    }

    public static int getRemainingMarkTime(EntityPlayer owner, String markId, Entity target) {
        if (owner == null || markId == null || markId.isEmpty() || target == null) {
            return 0;
        }

        MarkerState state = getState(owner);
        if (state == null) {
            return 0;
        }

        UUID targetId = target.getUniqueID();
        MarkerEntry entry = state.getEntry(markId, targetId);
        if (entry == null || entry.isExpired()) {
            if (entry != null) {
                state.remove(markId, targetId);
            }
            return 0;
        }

        if (!isValidTarget(findLoadedEntity(owner, targetId))) {
            state.remove(markId, targetId);
            return 0;
        }

        return entry.getRemainingTicks();
    }

    public static void tick(EntityPlayer owner) {
        if (owner == null || owner.world == null || owner.world.isRemote) {
            return;
        }

        MarkerState state = getState(owner);
        if (state == null || state.isEmpty()) {
            return;
        }

        List<String> emptyMarkIds = new ArrayList<>();

        for (Map.Entry<String, Map<UUID, MarkerEntry>> markEntry : state.viewAll().entrySet()) {
            String markId = markEntry.getKey();
            Map<UUID, MarkerEntry> targets = markEntry.getValue();

            if (targets == null || targets.isEmpty()) {
                emptyMarkIds.add(markId);
                continue;
            }

            List<UUID> toRemove = new ArrayList<>();

            for (Map.Entry<UUID, MarkerEntry> targetEntry : targets.entrySet()) {
                UUID targetId = targetEntry.getKey();
                MarkerEntry entry = targetEntry.getValue();

                if (targetId == null || entry == null) {
                    toRemove.add(targetId);
                    continue;
                }

                Entity resolved = findLoadedEntity(owner, targetId);
                if (!isValidTarget(resolved)) {
                    toRemove.add(targetId);
                    continue;
                }

                int next = entry.getRemainingTicks() - 1;
                entry.setRemainingTicks(next);

                if (next <= 0) {
                    toRemove.add(targetId);
                    DebugNotifier.marker(owner, "mark '" + markId + "' expired on " + describeTarget(resolved));
                }
            }

            for (UUID targetId : toRemove) {
                state.remove(markId, targetId);
            }

            if (!state.hasMark(markId)) {
                emptyMarkIds.add(markId);
            }
        }

        for (String markId : emptyMarkIds) {
            state.clearMarkId(markId);
        }

        state.pruneExpired();
    }

    private static MarkerState getState(EntityPlayer owner) {
        if (owner == null || PlayerStateProvider.CAPABILITY == null) {
            return null;
        }

        IPlayerState state = owner.getCapability(PlayerStateProvider.CAPABILITY, null);
        return state != null ? state.getMarkerState() : null;
    }

    private static Entity findLoadedEntity(EntityPlayer owner, UUID targetId) {
        if (owner == null || owner.world == null || targetId == null) {
            return null;
        }

        for (Entity entity : owner.world.loadedEntityList) {
            if (entity != null && targetId.equals(entity.getUniqueID())) {
                return entity;
            }
        }

        return null;
    }

    private static boolean isValidTarget(Entity entity) {
        return entity != null && !entity.isDead;
    }

    private static String describeTarget(Entity target) {
        if (target == null) {
            return "<null>";
        }

        return target.getName() + "/" + target.getUniqueID();
    }
}