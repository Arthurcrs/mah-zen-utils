package com.mahghuuuls.mahghuuulszenutils.common.capability.entity;

import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownState;
import net.minecraft.nbt.NBTTagCompound;

public class EntityState implements IEntityState {

    private final CooldownState cooldownState = new CooldownState();

    @Override
    public CooldownState getCooldownState() {
        return cooldownState;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("CooldownState", cooldownState.serializeNBT());
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        if (tag == null) {
            return;
        }

        if (tag.hasKey("CooldownState")) {
            cooldownState.deserializeNBT(tag.getCompoundTag("CooldownState"));
        }
    }

    public void copyFrom(EntityState other) {
        if (other == null) {
            return;
        }

        this.cooldownState.copyFrom(other.cooldownState);
    }
}