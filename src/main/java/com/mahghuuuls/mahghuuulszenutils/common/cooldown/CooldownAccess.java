package com.mahghuuuls.mahghuuulszenutils.common.cooldown;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class CooldownAccess {
    private CooldownAccess() {
    }

    public static CooldownState getCooldownState(EntityLivingBase entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof EntityPlayer) {
            IPlayerState playerState = entity.getCapability(PlayerStateProvider.CAPABILITY, null);
            return playerState != null ? playerState.getCooldownState() : null;
        }

        IEntityState entityState = entity.getCapability(EntityStateProvider.CAPABILITY, null);
        return entityState != null ? entityState.getCooldownState() : null;
    }
}