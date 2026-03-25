package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.cooldown;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownState;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class IEntityLivingBaseCooldownExpansion {

    private static CooldownState getCooldownState(IEntityLivingBase iEntity) {
        if (iEntity == null) {
            return null;
        }

        Object internal = iEntity.getInternal();
        if (!(internal instanceof EntityLivingBase)) {
            return null;
        }

        EntityLivingBase living = (EntityLivingBase) internal;

        if (living instanceof EntityPlayer) {
            return null;
        }

        IEntityState state = living.getCapability(EntityStateProvider.CAPABILITY, null);
        return state != null ? state.getCooldownState() : null;
    }

    @ZenMethod
    public static void startCooldown(IEntityLivingBase iEntity, String cooldownId, int duration) {
        CooldownState state = getCooldownState(iEntity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.set(cooldownId, duration);
    }

    @ZenMethod
    public static boolean onCooldown(IEntityLivingBase iEntity, String cooldownId) {
        CooldownState state = getCooldownState(iEntity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return false;
        }

        return state.has(cooldownId);
    }

    @ZenMethod
    public static int getCooldownTicks(IEntityLivingBase iEntity, String cooldownId) {
        CooldownState state = getCooldownState(iEntity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return 0;
        }

        return state.getRemaining(cooldownId);
    }

    @ZenMethod
    public static void clearCooldown(IEntityLivingBase iEntity, String cooldownId) {
        CooldownState state = getCooldownState(iEntity);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.clear(cooldownId);
    }
}