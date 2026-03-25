package com.mahghuuuls.mahghuuulszenutils.common.event;

import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateProvider;
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

        ((PlayerState) clone).copyCooldownsFrom((PlayerState) original);
    }
}
