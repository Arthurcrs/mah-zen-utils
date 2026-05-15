package com.mahghuuuls.mahzenutils.common.capability.entity;

import com.mahghuuuls.mahzenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahzenutils.common.cooldown.ICooldownHolder;
import com.mahghuuuls.mahzenutils.common.stack.StackState;

public interface IEntityState extends ICooldownHolder {

    @Override
    CooldownState getCooldownState();

    StackState getStackState();
}