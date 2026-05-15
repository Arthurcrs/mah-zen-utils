package com.mahghuuuls.mahzenutils.common.init;

import com.mahghuuuls.mahzenutils.common.capability.entity.EntityState;
import com.mahghuuuls.mahzenutils.common.capability.entity.EntityStateStorage;
import com.mahghuuuls.mahzenutils.common.capability.entity.IEntityState;
import com.mahghuuuls.mahzenutils.common.capability.player.IPlayerState;
import com.mahghuuuls.mahzenutils.common.capability.player.PlayerState;
import com.mahghuuuls.mahzenutils.common.capability.player.PlayerStateStorage;
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