package com.mahghuuuls.mahzenutils.common.cooldown;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CooldownState {

    private final Map<String, Integer> remainingTicksById = new HashMap<>();

    public boolean has(String id) {
        return getRemaining(id) > 0;
    }

    public int getRemaining(String id) {
        Integer value = remainingTicksById.get(id);
        return value != null && value > 0 ? value : 0;
    }

    public void set(String id, int ticks) {
        if (id == null || id.isEmpty()) {
            return;
        }

        if (ticks <= 0) {
            remainingTicksById.remove(id);
            return;
        }

        remainingTicksById.put(id, ticks);
    }

    public void clear(String id) {
        if (id == null || id.isEmpty()) {
            return;
        }

        remainingTicksById.remove(id);
    }

    public void clearAll() {
        remainingTicksById.clear();
    }

    public boolean isEmpty() {
        return remainingTicksById.isEmpty();
    }

    public List<String> tickAndGetExpired() {
        List<String> expired = new ArrayList<>();

        if (remainingTicksById.isEmpty()) {
            return expired;
        }

        Iterator<Map.Entry<String, Integer>> it = remainingTicksById.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Integer> entry = it.next();
            int next = entry.getValue() - 1;

            if (next <= 0) {
                expired.add(entry.getKey());
                it.remove();
            } else {
                entry.setValue(next);
            }
        }

        return expired;
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList list = new NBTTagList();

        for (Map.Entry<String, Integer> entry : remainingTicksById.entrySet()) {
            if (entry.getValue() <= 0) {
                continue;
            }

            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setString("Id", entry.getKey());
            entryTag.setInteger("Ticks", entry.getValue());
            list.appendTag(entryTag);
        }

        tag.setTag("Cooldowns", list);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        remainingTicksById.clear();

        if (tag == null || !tag.hasKey("Cooldowns", Constants.NBT.TAG_LIST)) {
            return;
        }

        NBTTagList list = tag.getTagList("Cooldowns", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound entryTag = list.getCompoundTagAt(i);
            String id = entryTag.getString("Id");
            int ticks = entryTag.getInteger("Ticks");

            if (!id.isEmpty() && ticks > 0) {
                remainingTicksById.put(id, ticks);
            }
        }
    }

    public void copyFrom(CooldownState other) {
        if (other == null) {
            return;
        }

        this.remainingTicksById.clear();
        this.remainingTicksById.putAll(other.remainingTicksById);
    }
}