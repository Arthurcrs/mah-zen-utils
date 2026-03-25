package com.mahghuuuls.mahghuuulszenutils.common.stack;

import net.minecraft.nbt.NBTTagCompound;

public class StackInstance {

    private int count;
    private int remainingTicks;

    public StackInstance() {
        this(0, 0);
    }

    public StackInstance(int count, int remainingTicks) {
        this.count = Math.max(0, count);
        this.remainingTicks = Math.max(0, remainingTicks);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = Math.max(0, count);
    }

    public int getRemainingTicks() {
        return remainingTicks;
    }

    public void setRemainingTicks(int remainingTicks) {
        this.remainingTicks = Math.max(0, remainingTicks);
    }

    public boolean isEmpty() {
        return count <= 0;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("Count", count);
        tag.setInteger("RemainingTicks", remainingTicks);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        if (tag == null) {
            this.count = 0;
            this.remainingTicks = 0;
            return;
        }

        this.count = Math.max(0, tag.getInteger("Count"));
        this.remainingTicks = Math.max(0, tag.getInteger("RemainingTicks"));
    }

    public void copyFrom(StackInstance other) {
        if (other == null) {
            this.count = 0;
            this.remainingTicks = 0;
            return;
        }

        this.count = other.count;
        this.remainingTicks = other.remainingTicks;
    }
}