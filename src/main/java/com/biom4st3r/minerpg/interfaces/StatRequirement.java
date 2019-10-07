package com.biom4st3r.minerpg.interfaces;

import java.util.Map;

import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

@FunctionalInterface
public interface StatRequirement extends Requirement<RPGStatsComponent>
{
    public Map<Stat,Integer> suppy(); 

    @Override
    default boolean meetsRequirements(RPGPlayer player, RPGStatsComponent obj,Object... args) {
        RPGStatsComponent sc = (RPGStatsComponent) args[0];
        Map<Stat,Integer> stats = suppy();
        for(Stat s : Stat.values())
        {
            if(sc.getStat(s) < stats.get(s))
            {
                return false;
            }
        }
        return true;
    }

    
}