package com.mahghuuuls.mahghuuulszenutils.common.marker;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class MarkerState {

    private final Map<String, Map<UUID, MarkerEntry>> marksById = new HashMap<>();

    public boolean hasMark(String markId) {
        Map<UUID, MarkerEntry> targets = marksById.get(markId);
        return targets != null && !targets.isEmpty();
    }

    public boolean isMarked(String markId, UUID targetId) {
        return getEntry(markId, targetId) != null;
    }

    public int getRemainingTicks(String markId, UUID targetId) {
        MarkerEntry entry = getEntry(markId, targetId);
        return entry != null ? entry.getRemainingTicks() : 0;
    }

    public MarkerEntry getEntry(String markId, UUID targetId) {
        if (markId == null || markId.isEmpty() || targetId == null) {
            return null;
        }

        Map<UUID, MarkerEntry> targets = marksById.get(markId);
        return targets != null ? targets.get(targetId) : null;
    }

    public MarkerEntry getOrCreateEntry(String markId, UUID targetId) {
        if (markId == null || markId.isEmpty() || targetId == null) {
            return null;
        }

        Map<UUID, MarkerEntry> targets = marksById.get(markId);
        if (targets == null) {
            targets = new HashMap<>();
            marksById.put(markId, targets);
        }

        MarkerEntry entry = targets.get(targetId);
        if (entry == null) {
            entry = new MarkerEntry();
            targets.put(targetId, entry);
        }

        return entry;
    }

    public void remove(String markId, UUID targetId) {
        if (markId == null || markId.isEmpty() || targetId == null) {
            return;
        }

        Map<UUID, MarkerEntry> targets = marksById.get(markId);
        if (targets == null) {
            return;
        }

        targets.remove(targetId);

        if (targets.isEmpty()) {
            marksById.remove(markId);
        }
    }

    public void clearMarkId(String markId) {
        if (markId == null || markId.isEmpty()) {
            return;
        }

        marksById.remove(markId);
    }

    public void clearAll() {
        marksById.clear();
    }

    public boolean isEmpty() {
        return marksById.isEmpty();
    }

    public Map<UUID, MarkerEntry> viewTargets(String markId) {
        Map<UUID, MarkerEntry> targets = marksById.get(markId);
        return targets != null ? Collections.unmodifiableMap(targets) : Collections.emptyMap();
    }

    public Map<String, Map<UUID, MarkerEntry>> viewAll() {
        return Collections.unmodifiableMap(marksById);
    }

    public void pruneExpired() {
        Iterator<Map.Entry<String, Map<UUID, MarkerEntry>>> outerIt = marksById.entrySet().iterator();
        while (outerIt.hasNext()) {
            Map.Entry<String, Map<UUID, MarkerEntry>> outerEntry = outerIt.next();
            Map<UUID, MarkerEntry> targets = outerEntry.getValue();

            if (targets == null || targets.isEmpty()) {
                outerIt.remove();
                continue;
            }

            Iterator<Map.Entry<UUID, MarkerEntry>> innerIt = targets.entrySet().iterator();
            while (innerIt.hasNext()) {
                Map.Entry<UUID, MarkerEntry> innerEntry = innerIt.next();
                MarkerEntry markerEntry = innerEntry.getValue();

                if (markerEntry == null || markerEntry.isExpired()) {
                    innerIt.remove();
                }
            }

            if (targets.isEmpty()) {
                outerIt.remove();
            }
        }
    }

    public NBTTagCompound serializeNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList markList = new NBTTagList();

        for (Map.Entry<String, Map<UUID, MarkerEntry>> markEntry : marksById.entrySet()) {
            String markId = markEntry.getKey();
            Map<UUID, MarkerEntry> targets = markEntry.getValue();

            if (markId == null || markId.isEmpty() || targets == null || targets.isEmpty()) {
                continue;
            }

            NBTTagCompound markTag = new NBTTagCompound();
            markTag.setString("MarkId", markId);

            NBTTagList targetList = new NBTTagList();
            for (Map.Entry<UUID, MarkerEntry> targetEntry : targets.entrySet()) {
                UUID targetId = targetEntry.getKey();
                MarkerEntry entry = targetEntry.getValue();

                if (targetId == null || entry == null || entry.isExpired()) {
                    continue;
                }

                NBTTagCompound targetTag = new NBTTagCompound();
                targetTag.setString("TargetUuid", targetId.toString());
                targetTag.setTag("Entry", entry.serializeNBT());
                targetList.appendTag(targetTag);
            }

            if (targetList.tagCount() > 0) {
                markTag.setTag("Targets", targetList);
                markList.appendTag(markTag);
            }
        }

        tag.setTag("Marks", markList);
        return tag;
    }

    public void deserializeNBT(NBTTagCompound tag) {
        marksById.clear();

        if (tag == null || !tag.hasKey("Marks", Constants.NBT.TAG_LIST)) {
            return;
        }

        NBTTagList markList = tag.getTagList("Marks", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < markList.tagCount(); i++) {
            NBTTagCompound markTag = markList.getCompoundTagAt(i);
            String markId = markTag.getString("MarkId");

            if (markId == null || markId.isEmpty()) {
                continue;
            }

            if (!markTag.hasKey("Targets", Constants.NBT.TAG_LIST)) {
                continue;
            }

            NBTTagList targetList = markTag.getTagList("Targets", Constants.NBT.TAG_COMPOUND);
            Map<UUID, MarkerEntry> targets = new HashMap<>();

            for (int j = 0; j < targetList.tagCount(); j++) {
                NBTTagCompound targetTag = targetList.getCompoundTagAt(j);
                String uuidString = targetTag.getString("TargetUuid");

                if (uuidString == null || uuidString.isEmpty()) {
                    continue;
                }

                UUID targetId;
                try {
                    targetId = UUID.fromString(uuidString);
                } catch (IllegalArgumentException ex) {
                    continue;
                }

                MarkerEntry entry = new MarkerEntry();
                if (targetTag.hasKey("Entry", Constants.NBT.TAG_COMPOUND)) {
                    entry.deserializeNBT(targetTag.getCompoundTag("Entry"));
                }

                if (!entry.isExpired()) {
                    targets.put(targetId, entry);
                }
            }

            if (!targets.isEmpty()) {
                marksById.put(markId, targets);
            }
        }
    }

    public void copyFrom(MarkerState other) {
        marksById.clear();

        if (other == null) {
            return;
        }

        for (Map.Entry<String, Map<UUID, MarkerEntry>> outerEntry : other.marksById.entrySet()) {
            String markId = outerEntry.getKey();
            Map<UUID, MarkerEntry> sourceTargets = outerEntry.getValue();

            if (markId == null || markId.isEmpty() || sourceTargets == null || sourceTargets.isEmpty()) {
                continue;
            }

            Map<UUID, MarkerEntry> copiedTargets = new HashMap<>();
            for (Map.Entry<UUID, MarkerEntry> innerEntry : sourceTargets.entrySet()) {
                UUID targetId = innerEntry.getKey();
                MarkerEntry sourceEntry = innerEntry.getValue();

                if (targetId == null || sourceEntry == null || sourceEntry.isExpired()) {
                    continue;
                }

                MarkerEntry copiedEntry = new MarkerEntry();
                copiedEntry.copyFrom(sourceEntry);
                copiedTargets.put(targetId, copiedEntry);
            }

            if (!copiedTargets.isEmpty()) {
                marksById.put(markId, copiedTargets);
            }
        }
    }
}