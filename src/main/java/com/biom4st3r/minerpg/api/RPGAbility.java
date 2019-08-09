package com.biom4st3r.minerpg.api;

import java.util.List;

import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.util.Identifier;

public abstract class RPGAbility
{
    protected RPGAbility(Identifier name)
    {
        this.name = name;
    }



    public final Identifier name;

    public Identifier getIcon()
    {
        return this.name;
    }

    public abstract void doAbility(RPGPlayer player);

    public abstract boolean applyCost(RPGPlayer player);

    public float cooldown;
    public abstract boolean checkRequirements(RPGPlayer player);

    public abstract Type getType();

    public enum Type{
        PASSIVE,
        REACTIVE,
        ACTIVED
    }
    public abstract List<String> getToolTips();

    @Override
    public String toString() {
        return this.name.toString();
    }

}