package com.mahghuuuls.mahghuuulszenutils.common.cooldown;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class CooldownService {

    private CooldownService() {
    }

    public static void set(EntityLivingBase entity, String cooldownId, int ticks) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.set(cooldownId, ticks);
    }

    public static boolean has(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return false;
        }

        return state.has(cooldownId);
    }

    public static int getRemaining(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return 0;
        }

        return state.getRemaining(cooldownId);
    }

    public static void clear(EntityLivingBase entity, String cooldownId) {
        CooldownState state = getState(entity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.clear(cooldownId);
    }

    public static void tick(EntityLivingBase entity) {
        CooldownState state = getState(entity);
        if (state == null || state.isEmpty()) {
            return;
        }

        state.tick();
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