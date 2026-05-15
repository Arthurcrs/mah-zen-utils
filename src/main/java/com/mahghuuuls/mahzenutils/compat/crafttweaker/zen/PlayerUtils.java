package com.mahghuuuls.mahzenutils.compat.crafttweaker.zen;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.mahzenutils.PlayerUtils") // Notice we are making a NEW class, not expanding an old one!
public class PlayerUtils {

    @ZenMethod
    public static boolean isSneaking(IPlayer player) {
        if (player == null || player.getInternal() == null) return false;

        Entity mcEntity = (Entity) player.getInternal();
        return mcEntity.isSneaking();
    }
}