package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.api.Stats;
import com.biom4st3r.minerpg.components.RPGStatsComponent;

public class StatRequirement
{
    private int str,dex,intel,wis,con,cha;
    public StatRequirement(int str,int dex,int intel,int wis,int con, int cha)
    {
        this.str = str; this.dex = dex; this.intel = intel; this.wis = wis; this.con = con; this.cha = cha;
    }
    public boolean hasRequirements(RPGStatsComponent rsc)
    {
        if( rsc.getStat(Stats.STRENGTH) >= this.str &&
            rsc.getStat(Stats.DEXTERITY) >= this.dex &&
            rsc.getStat(Stats.INTELLIGENCE) >= this.intel &&
            rsc.getStat(Stats.WISDOW) >= this.wis &&
            rsc.getStat(Stats.CONSTITUTION) >= this.con &&
            rsc.getStat(Stats.CHARISMA) >= this.cha)
        {
            return true;
        }

        return false;
    }
}