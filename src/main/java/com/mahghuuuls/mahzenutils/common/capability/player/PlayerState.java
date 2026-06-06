package com.mahghuuuls.mahzenutils.common.capability.player;

import com.mahghuuuls.mahzenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahzenutils.common.marker.MarkerState;
import com.mahghuuuls.mahzenutils.common.stack.StackState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class PlayerState implements IPlayerState {

    private final CooldownState cooldownState = new CooldownState();
    private final StackState stackState = new StackState();
    private final MarkerState markerState = new MarkerState();

    @Override
    public CooldownState getCooldownState() {
        return cooldownState;
    }

    @Override
    public StackState getStackState() {
        return stackState;
    }

    @Override
    public MarkerState getMarkerState() {
        return markerState;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("CooldownState", cooldownState.serializeNBT());
        tag.setTag("StackState", stackState.serializeNBT());
        tag.setTag("MarkerState", markerState.serializeNBT());
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

        if (tag.hasKey("MarkerState", Constants.NBT.TAG_COMPOUND)) {
            markerState.deserializeNBT(tag.getCompoundTag("MarkerState"));
        }
    }

    public void copyCooldownsFrom(PlayerState other) {
        if (other == null) {
            return;
        }

        this.cooldownState.copyFrom(other.cooldownState);
    }

    public void copyStacksFrom(PlayerState other) {
        if (other == null) {
            return;
        }

        this.stackState.copyFrom(other.stackState);
    }

    public void copyMarkersFrom(PlayerState other) {
        if (other == null) {
            return;
        }

        this.markerState.copyFrom(other.markerState);
    }
}