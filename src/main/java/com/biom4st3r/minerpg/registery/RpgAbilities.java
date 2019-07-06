package com.biom4st3r.minerpg.registery;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.impl.abilities.barbarian.RageAbility;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public class RpgAbilities
{
    public static final RPGAbility RAGE_ABILITY = new RageAbility(new Identifier(MineRPG.MODID, "rage"));

    public static RPGAbility register(RPGAbility rpgability)
    {
        return (RPGAbility)((MutableRegistry)RPG_Registry.ABILITY_REGISTRY).add(rpgability.name, rpgability);
    }

    public static void init()
    {
        // Registry.register(
        //     RPG_Registry.ABILITY_REGISTRY, 
        //     RAGE.name,
        //     RAGE
        //     );
        register(RAGE_ABILITY);
    }
}