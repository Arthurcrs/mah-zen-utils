package com.mahghuuuls.mahzenutils.compat.crafttweaker.stack;

import com.mahghuuuls.mahzenutils.common.stack.StackService;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class IEntityLivingBaseStackExpansion {

    private static EntityLivingBase asLiving(IEntityLivingBase iEntity) {
        if (iEntity == null) {
            return null;
        }

        Object internal = iEntity.getInternal();
        if (!(internal instanceof EntityLivingBase)) {
            return null;
        }

        EntityLivingBase living = (EntityLivingBase) internal;
        return living instanceof EntityPlayer ? null : living;
    }

    @ZenMethod
    public static void addStacks(IEntityLivingBase iEntity, String stackId, int amount) {
        StackService.addStacks(asLiving(iEntity), stackId, amount);
    }

    @ZenMethod
    public static void removeStacks(IEntityLivingBase iEntity, String stackId, int amount) {
        StackService.removeStacks(asLiving(iEntity), stackId, amount);
    }

    @ZenMethod
    public static void clearStacks(IEntityLivingBase iEntity, String stackId) {
        StackService.clearStacks(asLiving(iEntity), stackId);
    }

    @ZenMethod
    public static int getStacks(IEntityLivingBase iEntity, String stackId) {
        return StackService.getStacks(asLiving(iEntity), stackId);
    }

    @ZenMethod
    public static int getRemainingStackTime(IEntityLivingBase iEntity, String stackId) {
        return StackService.getRemainingStackTime(asLiving(iEntity), stackId);
    }

    @ZenMethod
    public static void setStackCount(IEntityLivingBase iEntity, String stackId, int count) {
        StackService.setStackCount(asLiving(iEntity), stackId, count);
    }

    @ZenMethod
    public static void setStackTime(IEntityLivingBase iEntity, String stackId, int ticks) {
        StackService.setStackTime(asLiving(iEntity), stackId, ticks);
    }
}