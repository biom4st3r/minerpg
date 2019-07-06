package com.biom4st3r.minerpg.registery;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.impl.rpgclass.BarbarianClass;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public class RpgClasses
{
    public static final RPGClass BARBARIAN_CLASS = new BarbarianClass(new Identifier(MineRPG.MODID, "barbarian"));

    public static RPGClass register(RPGClass rpgclass)
    {
        return (RPGClass)((MutableRegistry)RPG_Registry.CLASS_REGISTRY).add(rpgclass.name, rpgclass);
    }


    public static void init()
    {
        // Registry.register(
        //     RPG_Registry.CLASS_REGISTRY, 
        //     barbarianClass.name, 
        //     barbarianClass
        //     );
        register(BARBARIAN_CLASS);
    }
}