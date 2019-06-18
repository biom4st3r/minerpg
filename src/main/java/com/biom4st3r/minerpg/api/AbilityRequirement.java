package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.util.RPGPlayer;

public abstract class AbilityRequirement
{

    public Ability ability;

    public abstract boolean hasRequirement(RPGPlayer player);





}