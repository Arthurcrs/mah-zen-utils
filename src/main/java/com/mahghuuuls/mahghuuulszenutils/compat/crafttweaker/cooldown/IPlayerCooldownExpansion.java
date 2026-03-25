package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.cooldown;

import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownService;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class IPlayerCooldownExpansion {

    private static EntityPlayer asPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            return null;
        }

        Object internal = iPlayer.getInternal();
        return internal instanceof EntityPlayer ? (EntityPlayer) internal : null;
    }

    @ZenMethod
    public static void startCooldown(IPlayer iPlayer, String cooldownId, int duration) {
        CooldownService.set(asPlayer(iPlayer), cooldownId, duration);
    }

    @ZenMethod
    public static boolean onCooldown(IPlayer iPlayer, String cooldownId) {
        return CooldownService.has(asPlayer(iPlayer), cooldownId);
    }

    @ZenMethod
    public static int getCooldownTicks(IPlayer iPlayer, String cooldownId) {
        return CooldownService.getRemaining(asPlayer(iPlayer), cooldownId);
    }

    @ZenMethod
    public static void clearCooldown(IPlayer iPlayer, String cooldownId) {
        CooldownService.clear(asPlayer(iPlayer), cooldownId);
    }
}