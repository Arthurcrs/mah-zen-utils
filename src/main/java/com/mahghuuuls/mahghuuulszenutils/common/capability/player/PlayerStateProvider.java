package com.mahghuuuls.mahghuuulszenutils.common.capability.player;

import com.mahghuuuls.mahghuuulszenutils.MahZenUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public class PlayerStateProvider implements ICapabilitySerializable<NBTTagCompound> {

    public static final ResourceLocation NAME =
            new ResourceLocation(MahZenUtils.MOD_ID, "player_state");

    @CapabilityInject(IPlayerState.class)
    public static final Capability<IPlayerState> CAPABILITY = null;

    private final IPlayerState instance = new PlayerState();

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return CAPABILITY != null && capability == CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (CAPABILITY == null) {
            return null;
        }

        return capability == CAPABILITY ? CAPABILITY.cast(instance) : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        if (CAPABILITY == null) {
            return new NBTTagCompound();
        }

        return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (CAPABILITY == null) {
            return;
        }

        CAPABILITY.getStorage().readNBT(CAPABILITY, instance, null, nbt);
    }
}