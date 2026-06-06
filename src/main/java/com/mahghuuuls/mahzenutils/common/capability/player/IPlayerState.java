package com.mahghuuuls.mahzenutils.common.capability.player;

import com.mahghuuuls.mahzenutils.common.cooldown.CooldownState;
import com.mahghuuuls.mahzenutils.common.cooldown.ICooldownHolder;
import com.mahghuuuls.mahzenutils.common.marker.MarkerState;
import com.mahghuuuls.mahzenutils.common.stack.StackState;

public interface IPlayerState extends ICooldownHolder {

    @Override
    CooldownState getCooldownState();

    StackState getStackState();

    MarkerState getMarkerState();
}