package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.util.Identifier;

public abstract class Ability
{
    public Ability(Identifier name)
    {
        this.name = name;
    }

    public final Identifier name;

    public abstract void doAbility();

    public abstract void applyCost(RPGPlayer player);

    public float cooldown;

}