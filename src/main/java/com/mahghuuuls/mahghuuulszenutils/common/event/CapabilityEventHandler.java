package com.mahghuuuls.mahghuuulszenutils.common.event;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateProvider;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityEventHandler {

    @SubscribeEvent
    public void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();

        if (entity instanceof EntityPlayer) {
            event.addCapability(PlayerStateProvider.NAME, new PlayerStateProvider());
            return;
        }

        if (entity instanceof EntityLivingBase) {
            event.addCapability(EntityStateProvider.NAME, new EntityStateProvider());
        }
    }
}
