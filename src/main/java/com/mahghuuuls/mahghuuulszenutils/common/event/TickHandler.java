package com.mahghuuuls.mahghuuulszenutils.common.event;

import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownService;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        EntityPlayer player = event.player;
        if (player.world.isRemote) {
            return;
        }

        CooldownService.tick(player);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        EntityLivingBase entity = event.getEntityLiving();

        if (entity.world.isRemote) {
            return;
        }

        if (entity instanceof EntityPlayer) {
            return;
        }

        CooldownService.tick(entity);
    }
}
