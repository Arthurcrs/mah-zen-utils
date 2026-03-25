package com.mahghuuuls.mahghuuulszenutils.common.debug;

import com.mahghuuuls.mahghuuulszenutils.MahZenUtils;
import com.mahghuuuls.mahghuuulszenutils.common.config.ModConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public final class DebugNotifier {

    private DebugNotifier() {
    }

    public static void cooldown(EntityLivingBase entity, String message) {
        if (!ModConfig.debugCooldowns || entity == null || message == null) {
            return;
        }

        MahZenUtils.LOGGER.info("[CooldownDebug] {} {}", describe(entity), message);
    }

    public static void stack(EntityLivingBase entity, String message) {
        if (!ModConfig.debugStacks || entity == null || message == null) {
            return;
        }

        MahZenUtils.LOGGER.info("[StackDebug] {} {}", describe(entity), message);
    }

    public static void stackGlobal(String message) {
        if (!ModConfig.debugStacks || message == null) {
            return;
        }

        MahZenUtils.LOGGER.info("[StackDebug] {}", message);
    }

    private static String describe(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entity;
            return "[Player:" + player.getName() + "]";
        }

        return "[Entity:" + entity.getName() + "/" + entity.getUniqueID() + "]";
    }
}
