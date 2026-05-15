package com.mahghuuuls.mahzenutils.common.cooldown;

import com.mahghuuuls.mahzenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahzenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahzenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahzenutils.common.capability.player.PlayerStateProvider;
import com.mahghuuuls.mahzenutils.common.debug.DebugNotifier;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class CooldownService {

    private CooldownService() {
    }

    public static void set(EntityLivingBase entity, String cooldownId, int ticks) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null) {
            return;
        }

        state.set(cooldownId, ticks);
        DebugNotifier.cooldown(entity, "set cooldown '" + cooldownId + "' to " + ticks + " ticks");
    }

    public static boolean has(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null) {
            return false;
        }

        boolean result = state.has(cooldownId);
        DebugNotifier.cooldown(entity, "query has cooldown '" + cooldownId + "' -> " + result);
        return result;
    }

    public static int getRemaining(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null) {
            return 0;
        }

        int remaining = state.getRemaining(cooldownId);
        DebugNotifier.cooldown(entity, "query remaining cooldown '" + cooldownId + "' -> " + remaining + " ticks");
        return remaining;
    }

    public static void clear(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null) {
            return;
        }

        boolean existed = state.has(cooldownId);
        state.clear(cooldownId);

        if (existed) {
            DebugNotifier.cooldown(entity, "cleared cooldown '" + cooldownId + "'");
        }
    }

    public static void tick(EntityLivingBase entity) {
        CooldownState state = getState(entity);
        if (state == null || state.isEmpty()) {
            return;
        }

        for (String expiredId : state.tickAndGetExpired()) {
            DebugNotifier.cooldown(entity, "cooldown expired '" + expiredId + "'");
        }
    }

    private static CooldownState getState(EntityLivingBase entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof EntityPlayer) {
            if (PlayerStateProvider.CAPABILITY == null) {
                return null;
            }

            IPlayerState playerState = entity.getCapability(PlayerStateProvider.CAPABILITY, null);
            return playerState != null ? playerState.getCooldownState() : null;
        }

        if (EntityStateProvider.CAPABILITY == null) {
            return null;
        }

        IEntityState entityState = entity.getCapability(EntityStateProvider.CAPABILITY, null);
        return entityState != null ? entityState.getCooldownState() : null;
    }
}