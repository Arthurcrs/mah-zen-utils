package com.mahghuuuls.mahzenutils.common.capability.entity;

import com.mahghuuuls.mahzenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahzenutils.common.stack.StackState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class EntityState implements IEntityState {

    private final CooldownState cooldownState = new CooldownState();
    private final StackState stackState = new StackState();

    @Override
    public CooldownState getCooldownState() {
        return cooldownState;
    }

    @Override
    public StackState getStackState() {
        return stackState;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("CooldownState", cooldownState.serializeNBT());
        tag.setTag("StackState", stackState.serializeNBT());
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        if (tag == null) {
            return;
        }

        if (tag.hasKey("CooldownState", Constants.NBT.TAG_COMPOUND)) {
            cooldownState.deserializeNBT(tag.getCompoundTag("CooldownState"));
        }

        if (tag.hasKey("StackState", Constants.NBT.TAG_COMPOUND)) {
            stackState.deserializeNBT(tag.getCompoundTag("StackState"));
        }
    }

    public void copyFrom(EntityState other) {
        if (other == null) {
            return;
        }

        this.cooldownState.copyFrom(other.cooldownState);
        this.stackState.copyFrom(other.stackState);
    }
}