package com.mahghuuuls.mahzenutils.compat.crafttweaker.zen;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;

import static crafttweaker.api.minecraft.CraftTweakerMC.getIPlayer;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public class IEntityLivingBaseExpansions {

    @ZenMethod
    public static IPlayer asIPlayer(IEntityLivingBase iEntity) {
        Entity entity = CraftTweakerMC.getEntity(iEntity);

        if (entity instanceof EntityPlayerMP) {
            return getIPlayer((EntityPlayerMP) entity);
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

    @ZenMethod
    public static void dealCustomDamage(IEntityLivingBase target, IEntity attacker, float amount, String customDamageType) {
        if (target == null || attacker == null) return;

        EntityLivingBase mcTarget = (EntityLivingBase) target.getInternal();
        Entity mcAttacker = (Entity) attacker.getInternal();
        EntityDamageSource source = new EntityDamageSource(customDamageType, mcAttacker);

        mcTarget.attackEntityFrom(source, amount);
    }

    @ZenMethod
    public static boolean isBlocking(IEntityLivingBase entity) {
        if (entity == null || entity.getInternal() == null) return false;
        EntityLivingBase mcEntity = (EntityLivingBase) entity.getInternal();

        return mcEntity.isActiveItemStackBlocking();
    }

    @ZenMethod
    public static IPlayer[] getNearbyPlayers(IEntityLivingBase entity, double radius) {
        if (entity == null || entity.getInternal() == null) return new IPlayer[0];
        EntityLivingBase mcEntity = (EntityLivingBase) entity.getInternal();

        // Create a bounding box around the entity expanded by the radius
        AxisAlignedBB boundingBox = mcEntity.getEntityBoundingBox().grow(radius, radius, radius);
        List<EntityPlayer> mcPlayers = mcEntity.world.getEntitiesWithinAABB(EntityPlayer.class, boundingBox);

        IPlayer[] ctPlayers = new IPlayer[mcPlayers.size()];
        for (int i = 0; i < mcPlayers.size(); i++) {
            ctPlayers[i] = CraftTweakerMC.getIPlayer(mcPlayers.get(i));
        }

        return ctPlayers;
    }

    @ZenMethod
    public static int getPotionAmplifier(IEntityLivingBase entity, String potionId) {
        if (entity == null || entity.getInternal() == null) return -1;
        EntityLivingBase mcEntity = (EntityLivingBase) entity.getInternal();

        Potion potion = Potion.getPotionFromResourceLocation(potionId);
        if (potion != null) {
            PotionEffect effect = mcEntity.getActivePotionEffect(potion);
            if (effect != null) {
                return effect.getAmplifier();
            }
        }
        return -1;
    }

    @ZenMethod
    public static float getTrueAbsorptionAmount(IEntityLivingBase entity) {
        if (entity == null || entity.getInternal() == null) return 0.0f;
        EntityLivingBase mcEntity = (EntityLivingBase) entity.getInternal();

        return mcEntity.getAbsorptionAmount();
    }
}