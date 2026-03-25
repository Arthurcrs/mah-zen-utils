package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.cooldown;

import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownState;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;


@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class IPlayerCooldownExpansion {

    private static CooldownState getCooldownState(IPlayer iPlayer) {
        if (iPlayer == null) {
            return null;
        }

        Object internal = iPlayer.getInternal();
        if (!(internal instanceof EntityPlayer)) {
            return null;
        }

        EntityPlayer player = (EntityPlayer) internal;
        IPlayerState state = player.getCapability(PlayerStateProvider.CAPABILITY, null);
        return state != null ? state.getCooldownState() : null;
    }

    @ZenMethod
    public static void startCooldown(IPlayer iPlayer, String cooldownId, int duration) {
        CooldownState state = getCooldownState(iPlayer);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.set(cooldownId, duration);
    }

    @ZenMethod
    public static boolean onCooldown(IPlayer iPlayer, String cooldownId) {
        CooldownState state = getCooldownState(iPlayer);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return false;
        }

        return state.has(cooldownId);
    }

    @ZenMethod
    public static int getCooldownTicks(IPlayer iPlayer, String cooldownId) {
        CooldownState state = getCooldownState(iPlayer);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return 0;
        }

        return state.getRemaining(cooldownId);
    }

    @ZenMethod
    public static void clearCooldown(IPlayer iPlayer, String cooldownId) {
        CooldownState state = getCooldownState(iPlayer);
        if (state == null || cooldownId == null || cooldownId.isEmpty()) {
            return;
        }

        state.clear(cooldownId);
    }
}