package com.mahghuuuls.mahghuuulszenutils.common.marker;

import net.minecraft.nbt.NBTTagCompound;

public class MarkerEntry {

    private int remainingTicks;

    public MarkerEntry() {
        this(0);
    }

    public MarkerEntry(int remainingTicks) {
        this.remainingTicks = Math.max(0, remainingTicks);
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = Math.max(0, remainingTicks);
    }

    public boolean isExpired() {
        return remainingTicks <= 0;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("RemainingTicks", remainingTicks);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        if (tag == null) {
            remainingTicks = 0;
            return;
        }

        remainingTicks = Math.max(0, tag.getInteger("RemainingTicks"));
    }

    public void copyFrom(MarkerEntry other) {
        if (other == null) {
            this.remainingTicks = 0;
            return;
        }

        this.remainingTicks = other.remainingTicks;
    }
}