package com.mahghuuuls.mahghuuulszenutils.common.cooldown;

public class CooldownService {
    public boolean hasCooldown(ICooldownHolder holder, String id) {
        return holder != null && holder.getCooldownState().has(id);
    }

    public int getRemainingTicks(ICooldownHolder holder, String id) {
        return holder != null ? holder.getCooldownState().getRemaining(id) : 0;
    }

    public void setCooldown(ICooldownHolder holder, String id, int ticks) {
        if (holder == null) {
            return;
        }
        holder.getCooldownState().set(id, ticks);
    }

    public void clearCooldown(ICooldownHolder holder, String id) {
        if (holder == null) {
            return;
        }
        holder.getCooldownState().clear(id);
    }

    public void tick(ICooldownHolder holder) {
        if (holder == null) {
            return;
        }
        holder.getCooldownState().tick();
    }
}