package com.mahghuuuls.mahghuuulszenutils.common.stack;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StackState {

    private final Map<String, StackInstance> stacksById = new HashMap<>();

    public boolean has(String stackId) {
        return getCount(stackId) > 0;
    }

    public int getCount(String stackId) {
        StackInstance instance = stacksById.get(stackId);
        return instance != null ? instance.getCount() : 0;
    }

    public int getRemainingTicks(String stackId) {
        StackInstance instance = stacksById.get(stackId);
        return instance != null ? instance.getRemainingTicks() : 0;
    }

    public StackInstance get(String stackId) {
        return stacksById.get(stackId);
    }

    public StackInstance getOrCreate(String stackId) {
        StackInstance instance = stacksById.get(stackId);
        if (instance == null) {
            instance = new StackInstance();
            stacksById.put(stackId, instance);
        }
        return instance;
    }

    public void remove(String stackId) {
        if (stackId == null || stackId.isEmpty()) {
            return;
        }

        stacksById.remove(stackId);
    }

    public void clearAll() {
        stacksById.clear();
    }

    public boolean isEmpty() {
        return stacksById.isEmpty();
    }

    public Map<String, StackInstance> view() {
        return Collections.unmodifiableMap(stacksById);
    }

    public void pruneEmpty() {
        Iterator<Map.Entry<String, StackInstance>> it = stacksById.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, StackInstance> entry = it.next();
            StackInstance instance = entry.getValue();
            if (instance == null || instance.isEmpty()) {
                it.remove();
            }
        }
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (Map.Entry<String, StackInstance> entry : stacksById.entrySet()) {
            StackInstance instance = entry.getValue();
            if (instance == null || instance.isEmpty()) {
                continue;
            }

            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setString("StackId", entry.getKey());
            entryTag.setTag("Instance", instance.serializeNBT());
            list.appendTag(entryTag);
        }

        tag.setTag("Stacks", list);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        stacksById.clear();

        if (tag == null || !tag.hasKey("Stacks", Constants.NBT.TAG_LIST)) {
            return;
        }

        NBTTagList list = tag.getTagList("Stacks", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entryTag = list.getCompoundTagAt(i);
            String stackId = entryTag.getString("StackId");

            if (stackId == null || stackId.isEmpty()) {
                continue;
            }

            StackInstance instance = new StackInstance();
            if (entryTag.hasKey("Instance", Constants.NBT.TAG_COMPOUND)) {
                instance.deserializeNBT(entryTag.getCompoundTag("Instance"));
            }

            if (!instance.isEmpty()) {
                stacksById.put(stackId, instance);
            }
        }
    }

    public void copyFrom(StackState other) {
        stacksById.clear();

        if (other == null) {
            return;
        }

        for (Map.Entry<String, StackInstance> entry : other.stacksById.entrySet()) {
            StackInstance copy = new StackInstance();
            copy.copyFrom(entry.getValue());
            if (!copy.isEmpty()) {
                stacksById.put(entry.getKey(), copy);
            }
        }
    }
}