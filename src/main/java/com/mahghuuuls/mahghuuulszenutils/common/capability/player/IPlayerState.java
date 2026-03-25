package com.mahghuuuls.mahghuuulszenutils.common.capability.player;

import com.mahghuuuls.mahghuuulszenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahghuuulszenutils.common.cooldown.ICooldownHolder;
import com.mahghuuuls.mahghuuulszenutils.common.stack.StackState;

public interface IPlayerState extends ICooldownHolder {

    @Override
    CooldownState getCooldownState();

    StackState getStackState();
}