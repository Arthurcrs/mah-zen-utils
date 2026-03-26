package com.mahghuuuls.mahghuuulszenutils.compat.crafttweaker.marker;

import com.mahghuuuls.mahghuuulszenutils.common.log.RuntimeLog;
import com.mahghuuuls.mahghuuulszenutils.common.marker.MarkerRule;
import com.mahghuuuls.mahghuuulszenutils.common.marker.MarkerService;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenRegister
@ZenExpansion("crafttweaker.player.IPlayer")
public class IPlayerMarkerExpansion {

    private static EntityPlayer asPlayer(IPlayer iPlayer) {
        if (iPlayer == null) {
            return null;
        }

        Object internal = iPlayer.getInternal();
        return internal instanceof EntityPlayer ? (EntityPlayer) internal : null;
    }

    private static Entity asEntity(IEntity iEntity) {
        if (iEntity == null) {
            return null;
        }

        Object internal = iEntity.getInternal();
        return internal instanceof Entity ? (Entity) internal : null;
    }

    @ZenMethod
    public static void markEntity(IPlayer iPlayer, String markId, IEntity target, int duration, String rule) {
        EntityPlayer owner = asPlayer(iPlayer);
        Entity resolvedTarget = asEntity(target);
        MarkerRule resolvedRule = MarkerRule.fromZen(rule);

        if (resolvedRule == null) {
            RuntimeLog.invalidMarkerRule(rule);
            return;
        }

        MarkerService.markEntity(owner, markId, resolvedTarget, duration, resolvedRule);
    }

    @ZenMethod
    public static IEntity[] getMarkedEntities(IPlayer iPlayer, String markId) {
        EntityPlayer owner = asPlayer(iPlayer);
        List<Entity> marked = MarkerService.getMarkedEntities(owner, markId);
        List<IEntity> converted = new ArrayList<>();

        for (Entity entity : marked) {
            IEntity wrapped = CraftTweakerMC.getIEntity(entity);
            if (wrapped != null) {
                converted.add(wrapped);
            }
        }

        return converted.toArray(new IEntity[0]);
    }

    @ZenMethod
    public static void clearMarks(IPlayer iPlayer, String markId) {
        MarkerService.clearMarks(asPlayer(iPlayer), markId);
    }

    @ZenMethod
    public static void unmarkEntity(IPlayer iPlayer, String markId, IEntity target) {
        MarkerService.unmarkEntity(asPlayer(iPlayer), markId, asEntity(target));
    }

    @ZenMethod
    public static boolean isMarked(IPlayer iPlayer, String markId, IEntity target) {
        return MarkerService.isMarked(asPlayer(iPlayer), markId, asEntity(target));
    }

    @ZenMethod
    public static int getRemainingMarkTime(IPlayer iPlayer, String markId, IEntity target) {
        return MarkerService.getRemainingMarkTime(asPlayer(iPlayer), markId, asEntity(target));
    }
}
