package com.mahghuuuls.mahzenutils.compat.crafttweaker.stack;

import com.mahghuuuls.mahzenutils.common.stack.StackService;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class IPlayerStackExpansion {

    private static EntityPlayer asPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            return null;
        }

        Object internal = iPlayer.getInternal();
        return internal instanceof EntityPlayer ? (EntityPlayer) internal : null;
    }

    @ZenMethod
    public static void addStacks(IPlayer iPlayer, String stackId, int amount) {
        StackService.addStacks(asPlayer(iPlayer), stackId, amount);
    }

    @ZenMethod
    public static void removeStacks(IPlayer iPlayer, String stackId, int amount) {
        StackService.removeStacks(asPlayer(iPlayer), stackId, amount);
    }

    @ZenMethod
    public static void clearStacks(IPlayer iPlayer, String stackId) {
        StackService.clearStacks(asPlayer(iPlayer), stackId);
    }

    @ZenMethod
    public static int getStacks(IPlayer iPlayer, String stackId) {
        return StackService.getStacks(asPlayer(iPlayer), stackId);
    }

    @ZenMethod
    public static int getRemainingStackTime(IPlayer iPlayer, String stackId) {
        return StackService.getRemainingStackTime(asPlayer(iPlayer), stackId);
    }

    @ZenMethod
    public static void setStackCount(IPlayer iPlayer, String stackId, int count) {
        StackService.setStackCount(asPlayer(iPlayer), stackId, count);
    }

    @ZenMethod
    public static void setStackTime(IPlayer iPlayer, String stackId, int ticks) {
        StackService.setStackTime(asPlayer(iPlayer), stackId, ticks);
    }
}