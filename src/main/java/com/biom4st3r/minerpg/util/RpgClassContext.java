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

    public RpgAbilityContext getAbilityContext(RPGAbility rpga)
    {
        RPGAbility[] raa =  this.getAbilities();
        DefaultedObj<RpgAbilityContext> rax = new DefaultedObj<RpgAbilityContext>(null, RpgAbilityContext.EMPTY);
        for(int i = 0; i < raa.length; i++)
        {
            if(rpga == raa[i])
            {
                rax.set(new RpgAbilityContext(this, i, rpga));
                break;
            }
        }
        return rax.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof RpgClassContext)
        {
            RpgClassContext rcc = ((RpgClassContext)obj);
            if(this.rpgclass == rcc.rpgclass && this.Lvl == rcc.Lvl)
            {
                return true;
            }
        }
        return false;
    }
}