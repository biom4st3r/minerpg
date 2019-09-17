package com.biom4st3r.minerpg.registery;

import java.util.List;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.impl.abilities.EvokerFangsAOEAbility;
import com.biom4st3r.minerpg.impl.abilities.EvokerFangsAbility;
import com.biom4st3r.minerpg.impl.abilities.FireballAbility;
import com.biom4st3r.minerpg.impl.abilities.MultiPotionAbility;
import com.biom4st3r.minerpg.impl.abilities.barbarian.UnarmoredDefenceAbility;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public final class RpgAbilities {
    public static final RPGAbility RAGE_ABILITY = new MultiPotionAbility(new Identifier(MineRPG.MODID, "rage"), 600, new StatusEffect[]{StatusEffects.RESISTANCE,StatusEffects.STRENGTH}, new int[]{1000,1000}, new int[]{2,0});//new RageAbility(new Identifier(MineRPG.MODID, "rage"));
    public static final RPGAbility NONE = new No_Ability();
    public static final RPGAbility FIREBALL_ABILITY = new FireballAbility(new Identifier(MineRPG.MODID, "fireball"), 20);
    public static final RPGAbility UNARMORED_DEFENCE = new UnarmoredDefenceAbility(new Identifier(MineRPG.MODID,"unarmereddefencebarb"));
    public static final RPGAbility RECKLESS_ATK = new MultiPotionAbility(new Identifier(MineRPG.MODID,"recklessatk"), 600, new StatusEffect[]{MinerpgStatusEffect.PAPER_SKIN,StatusEffects.STRENGTH}, new int[]{20*15,20*15}, new int[]{1,2});
    public static final RPGAbility EVOKER_FANGS = new EvokerFangsAbility(new Identifier(MineRPG.MODID,"evokerfangs"), 1);
    public static final RPGAbility EVOKER_AOE = new EvokerFangsAOEAbility(new Identifier(MineRPG.MODID,"evokeraoe"), 1);
    
    public static RPGAbility register(RPGAbility rpgability)
    {
        return (RPGAbility)((MutableRegistry<RPGAbility>)RPG_Registry.ABILITY_REGISTRY).add(rpgability.id, rpgability);
    }

    public static void init()
    {
        register(NONE);
        register(RAGE_ABILITY);
        register(FIREBALL_ABILITY);
        register(UNARMORED_DEFENCE);
        register(RECKLESS_ATK);
        register(EVOKER_FANGS);
        register(EVOKER_AOE);
    }
}
class No_Ability extends RPGAbility {

    public No_Ability() {
        super(new Identifier(MineRPG.MODID,"noability"),0);
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        return false;
    }

    @Override
    public Type getType() {
        return Type.PASSIVE;
    }

    @Override
    public List<String> getToolTips() {
        
        return Lists.newArrayList("you shouldn't see this. Please report as issue");
        //return Lists.newArrayList();
    }

    @Override
    public void applyCost(RPGPlayer player) {

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        return true;
    }
    
}