package com.biom4st3r.minerpg.registery;

import java.util.List;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.impl.rpgclass.BarbarianClass;
import com.google.common.collect.Lists;

import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;

public final class RpgClasses {
    public static final RPGClass BARBARIAN_CLASS = new BarbarianClass(new Identifier(MineRPG.MODID, "barbarian"));
    public static final RPGClass NONE = new NoClass();

    public static RPGClass register(RPGClass rpgclass) {
        return (RPGClass) ((MutableRegistry<RPGClass>) RPG_Registry.CLASS_REGISTRY).add(rpgclass.id, rpgclass);
    }

    public static void init() {
        // Registry.register(
        // RPG_Registry.CLASS_REGISTRY,
        // barbarianClass.name,
        // barbarianClass
        // );
        register(NONE);
        register(BARBARIAN_CLASS);
    }
}

class NoClass extends RPGClass {

    public NoClass() {
        super(new Identifier(MineRPG.MODID, "noclass"));
    }

    @Override
    public RPGAbility[] abilitysAvalibleAtLevel(int Lvl) {
        return new RPGAbility[] { RpgAbilities.NONE };
    }

    @Override
    public List<String> getToolTips() {
        return Lists.newArrayList("ERROR");
    }

    @Override
    public void provideStatAtLvl(Stat<?> stat, int Lvl) {
        // TODO Auto-generated method stub

    }

}