package com.biom4st3r.minerpg.registery;

import java.util.List;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.impl.abilities.PotionAbility;
import com.biom4st3r.minerpg.impl.abilities.barbarian.RageAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public class RpgAbilities {
    public static final RPGAbility RAGE_ABILITY = new PotionAbility(new Identifier(MineRPG.MODID, "rage"), 600, StatusEffects.RESISTANCE, 1000, 1);//new RageAbility(new Identifier(MineRPG.MODID, "rage"));
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
    public void doAbility(RPGPlayer player) {

    }

    @Override
    public Type getType() {
        return Type.PASSIVE;
    }

    @Override
    public List<String> getToolTips() {
        return Lists.newArrayList();
    }
    
}