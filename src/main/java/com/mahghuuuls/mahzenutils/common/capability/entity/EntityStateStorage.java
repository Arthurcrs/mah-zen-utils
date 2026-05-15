package com.mahghuuuls.mahzenutils.common.capability.entity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class EntityStateStorage implements Capability.IStorage<IEntityState> {

    @Override
    public NBTBase writeNBT(Capability<IEntityState> capability, IEntityState instance, EnumFacing side) {
        if (instance instanceof EntityState) {
            return ((EntityState) instance).serializeNBT();
        }

        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<IEntityState> capability, IEntityState instance, EnumFacing side, NBTBase nbt) {
        if (instance instanceof EntityState && nbt instanceof NBTTagCompound) {
            ((EntityState) instance).deserializeNBT((NBTTagCompound) nbt);
        }
    }
}