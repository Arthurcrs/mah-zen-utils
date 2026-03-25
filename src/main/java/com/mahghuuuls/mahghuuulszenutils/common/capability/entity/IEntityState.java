package com.mahghuuuls.mahghuuulszenutils.common.capability.entity;

import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahghuuulszenutils.common.cooldown.ICooldownHolder;
import com.mahghuuuls.mahghuuulszenutils.common.stack.StackState;

public interface IEntityState extends ICooldownHolder {

    @Override
    CooldownState getCooldownState();

    StackState getStackState();
}
