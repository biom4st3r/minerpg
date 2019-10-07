package com.biom4st3r.minerpg.interfaces;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

@FunctionalInterface
public interface ClassRequirement extends Requirement<RPGClassComponent>
{
    public List<RPGClass> suppy();

    @Override
    default boolean meetsRequirements(RPGPlayer player, RPGClassComponent cc, Object... args) {
        for(RPGClass rClass : suppy())
        {
            if(!cc.rpgClasses.containsKey(rClass))
            {
                return false;
            }
        }
        if(args.length == 1)
        {
            int[] levels = (int[]) args[0];
            for(int i = 0; i < levels.length; i++)
            {
                if(cc.rpgClasses.get(suppy().get(i)) < levels[i])
                {
                    return false;
                }
            }
        }
        return true;
    }

}