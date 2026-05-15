package com.mahghuuuls.mahzenutils.common.event;

import com.mahghuuuls.mahzenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahzenutils.common.capability.player.PlayerState;
import com.mahghuuuls.mahzenutils.common.capability.player.PlayerStateProvider;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerLifecycleHandler {

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        IPlayerState original = event.getOriginal().getCapability(PlayerStateProvider.CAPABILITY, null);
        IPlayerState clone = event.getEntityPlayer().getCapability(PlayerStateProvider.CAPABILITY, null);

        if (!(original instanceof PlayerState) || !(clone instanceof PlayerState)) {
            return;
        }

        // Cooldowns always persist through both death and dimension changes
        ((PlayerState) clone).copyCooldownsFrom((PlayerState) original);

        // Stacks and Markers clear on death, but persist through dimension changes
        if (!event.isWasDeath()) {
            ((PlayerState) clone).copyStacksFrom((PlayerState) original);
            ((PlayerState) clone).copyMarkersFrom((PlayerState) original);
        }
    }
}