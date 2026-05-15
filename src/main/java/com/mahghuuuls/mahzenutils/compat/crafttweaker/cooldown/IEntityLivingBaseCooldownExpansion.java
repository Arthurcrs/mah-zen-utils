package com.mahghuuuls.mahzenutils.compat.crafttweaker.cooldown;

import com.mahghuuuls.mahzenutils.common.cooldown.CooldownService;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class IEntityLivingBaseCooldownExpansion {

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
    public static void startCooldown(IEntityLivingBase iEntity, String cooldownId, int duration) {
        CooldownService.set(asLiving(iEntity), cooldownId, duration);
    }

    @ZenMethod
    public static boolean onCooldown(IEntityLivingBase iEntity, String cooldownId) {
        return CooldownService.has(asLiving(iEntity), cooldownId);
    }

    @ZenMethod
    public static int getCooldownTicks(IEntityLivingBase iEntity, String cooldownId) {
        return CooldownService.getRemaining(asLiving(iEntity), cooldownId);
    }

    @ZenMethod
    public static void clearCooldown(IEntityLivingBase iEntity, String cooldownId) {
        CooldownService.clear(asLiving(iEntity), cooldownId);
    }
}