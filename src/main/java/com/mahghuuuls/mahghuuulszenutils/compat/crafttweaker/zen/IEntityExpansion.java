package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.zen;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntity")
public class IEntityExpansion {
    @ZenMethod
    public static IPlayer asIPlayer(IEntity iEntity) {
        Entity entity = CraftTweakerMC.getEntity(iEntity);

        if (entity instanceof EntityPlayerMP) {
            return CraftTweakerMC.getIPlayer((EntityPlayerMP) entity);
        }

        return null;
    }
}