package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RpgClasses;

import net.minecraft.util.Identifier;

public class RpgClassContext
{
    public static final RpgClassContext EMPTY = new RpgClassContext(RpgClasses.NONE,-1);

    public RpgClassContext(RPGClass rpgClass, int Lvl)
    {
        this.rpgclass = rpgClass;
        this.Lvl = Lvl;
    }

    public RPGClass rpgclass;
    public int Lvl = -1;


}