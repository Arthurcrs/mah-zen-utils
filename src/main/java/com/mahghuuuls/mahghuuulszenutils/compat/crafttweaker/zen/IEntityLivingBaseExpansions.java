package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.zen;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class IEntityLivingBaseExpansions {

    @ZenMethod
    public static IPlayer asIPlayer(IEntity iEntity) {
        Entity entity = CraftTweakerMC.getEntity(iEntity);

        if (entity instanceof EntityPlayerMP) {
            return CraftTweakerMC.getIPlayer((EntityPlayerMP) entity);
        }

        return null;
    }

    @ZenMethod
    public static float getAttributeValue(IEntityLivingBase entityBase, String attributeName, float defaultValue) {
        if (entityBase == null || attributeName == null) {
            return defaultValue;
        }

        EntityLivingBase mcEntity = CraftTweakerMC.getEntityLivingBase(entityBase);
        if (mcEntity == null) {
            return defaultValue;
        }

        IAttributeInstance attribute = mcEntity.getAttributeMap().getAttributeInstanceByName(attributeName);
        if (attribute != null) {
            return (float) attribute.getAttributeValue();
        }

        return defaultValue;
    }

    @ZenMethod
    public static void applyPotionEffect(IEntityLivingBase entityBase, String potionId, int durationTicks, int amplifier) {
        if (entityBase == null || potionId == null) {
            return;
        }

        EntityLivingBase mcEntity = CraftTweakerMC.getEntityLivingBase(entityBase);
        if (mcEntity == null || mcEntity.world.isRemote) {
            return;
        }

        Potion potion = Potion.getPotionFromResourceLocation(potionId);
        if (potion != null) {
            mcEntity.addPotionEffect(new PotionEffect(potion, durationTicks, amplifier, false, true));
        } else {
            CraftTweakerAPI.logError("applyPotionEffect: Unknown potion ID '" + potionId + "'");
        }
    }

    @ZenMethod
    public static float getHealthPercentage(IEntityLivingBase entityBase) {
        if (entityBase == null) {
            return 0.0f;
        }

        EntityLivingBase mcEntity = CraftTweakerMC.getEntityLivingBase(entityBase);
        if (mcEntity == null) {
            return 0.0f;
        }

        float maxHealth = mcEntity.getMaxHealth();
        if (maxHealth <= 0.0f) {
            return 0.0f;
        }

        return mcEntity.getHealth() / maxHealth;
    }
}