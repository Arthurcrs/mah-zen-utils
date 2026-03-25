package com.mahghuuuls.mahghuuulszenutils.common.init;

import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.EntityStateStorage;
import com.mahghuuuls.mahghuuulszenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerState;
import com.mahghuuuls.mahghuuulszenutils.common.capability.player.PlayerStateStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityInit {
    private CapabilityInit() {
    }

    public static void register() {
        CapabilityManager.INSTANCE.register(
                IPlayerState.class,
                new PlayerStateStorage(),
                PlayerState::new
        );

        CapabilityManager.INSTANCE.register(
                IEntityState.class,
                new EntityStateStorage(),
                EntityState::new
        );
    }
}
