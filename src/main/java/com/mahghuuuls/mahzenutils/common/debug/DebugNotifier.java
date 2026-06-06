package com.mahghuuuls.mahzenutils.common.debug;

import com.mahghuuuls.mahzenutils.MahZenUtilsMod;
import com.mahghuuuls.mahzenutils.common.config.ModConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

public final class DebugNotifier {

    private DebugNotifier() {
    }

    public static void cooldown(EntityLivingBase entity, String message) {
        if (!ModConfig.debugCooldowns || entity == null || message == null) {
            return;
        }

        MahZenUtilsMod.LOGGER.info("[CooldownDebug] {} {}", describe(entity), message);
    }

    public static void stack(EntityLivingBase entity, String message) {
        if (!ModConfig.debugStacks || entity == null || message == null) {
            return;
        }

        MahZenUtilsMod.LOGGER.info("[StackDebug] {} {}", describe(entity), message);
    }

    public static void stackGlobal(String message) {
        if (!ModConfig.debugStacks || message == null) {
            return;
        }

        MahZenUtilsMod.LOGGER.info("[StackDebug] {}", message);
    }

    public static void marker(EntityPlayer owner, String message) {
        if (!ModConfig.debugMarkers || owner == null || message == null) {
            return;
        }

        owner.sendMessage(new TextComponentString("[MarkerDebug] " + message));
        MahZenUtilsMod.LOGGER.info("[MarkerDebug] [Player:{}] {}", owner.getName(), message);
    }

    private static String describe(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            return "[Player:" + player.getName() + "]";
        }

        return "[Entity:" + entity.getName() + "/" + entity.getUniqueID() + "]";
    }
}