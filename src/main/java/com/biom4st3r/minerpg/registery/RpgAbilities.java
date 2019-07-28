package com.biom4st3r.minerpg.registery;

import java.util.List;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.impl.abilities.barbarian.RageAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public class RpgAbilities {
    public static final RPGAbility RAGE_ABILITY = new RageAbility(new Identifier(MineRPG.MODID, "rage"));
    public static final RPGAbility NONE = new No_Ability();
    

    public static RPGAbility register(RPGAbility rpgability)
    {
        return (RPGAbility)((MutableRegistry<RPGAbility>)RPG_Registry.ABILITY_REGISTRY).add(rpgability.name, rpgability);
    }

    public static void init()
    {
        // Registry.register(
        //     RPG_Registry.ABILITY_REGISTRY, 
        //     RAGE.name,
        //     RAGE
        //     );
        register(NONE);
        register(RAGE_ABILITY);
    }
}
class No_Ability extends RPGAbility {

    public No_Ability() {
        super(new Identifier(MineRPG.MODID,"noability"));
        // TODO Auto-generated constructor stub
    }

    @Override
    public void doAbility() {

    }

    @Override
    public boolean applyCost(RPGPlayer player) {
        return false;
    }

    @Override
    public boolean checkRequirements(RPGPlayer player) {
        return false;
    }

    @Override
    public Type getType() {
        return Type.ACTIVED;
    }

    @Override
    public List<String> getToolTips() {
        return Lists.newArrayList();
    }
    
}