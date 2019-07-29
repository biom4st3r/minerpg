package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RpgClasses;

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

    public RPGAbility[] getAbilities()
    {
        return this.rpgclass.abilitysAvalibleAtLevel(this.Lvl);
    }
}