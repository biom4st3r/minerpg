package com.biom4st3r.minerpg.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.util.Identifier;

public abstract class RPGClass
{
    

    //protected HashMap<Integer,Ability[]> abilities;

    public RPGClass(Identifier name)
    {
        this.name = name;
    }

    public final Identifier name;

    public int maxLvl;

    
    public abstract Ability[] abilitysAvalibleAtLevel(int Lvl);

    @Override
    public int hashCode() {
        return name.hashCode();
    }



}