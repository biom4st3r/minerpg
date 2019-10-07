package com.biom4st3r.minerpg.interfaces;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

public interface Requirement<T>
{

    public boolean meetsRequirements(RPGPlayer player, T obj, Object... args);
}