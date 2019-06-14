package com.biom4st3r.minerpg.api;

import net.minecraft.util.Identifier;

public class Stat 
{
    public Stat(Identifier name, int defaultValue)
    {
        this.name = name;
    }

    protected int value;

    public final Identifier name;

}