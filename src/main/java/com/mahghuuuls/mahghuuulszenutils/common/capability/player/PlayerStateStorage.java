package com.mahghuuuls.mahghuuulszenutils.common.capability.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.Capability;

public class PlayerStateStorage implements Capability.IStorage<IPlayerState> {

    @Override
    public NBTBase writeNBT(Capability<IPlayerState> capability, IPlayerState instance, net.minecraft.util.EnumFacing side) {
        if (instance instanceof PlayerState) {
            return ((PlayerState) instance).serializeNBT();
        }
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<IPlayerState> capability, IPlayerState instance, net.minecraft.util.EnumFacing side, NBTBase nbt) {
        if (instance instanceof PlayerState && nbt instanceof NBTTagCompound) {
            ((PlayerState) instance).deserializeNBT((NBTTagCompound) nbt);
        }
    }
}