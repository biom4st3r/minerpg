package com.biom4st3r.minerpg.api.abilities;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.util.Identifier;

public abstract class ArmorOverrideAbility extends RPGAbility {

    protected ArmorOverrideAbility(Identifier id, int coolDownDuration) {
        super(id, coolDownDuration);
		// TODO Auto-generated constructor stub
    }

    @Override
    public void applyCost(RPGPlayer player) {

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        return false;
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        return false;
    }

    @Override
    public Type getType() {
        return Type.PASSIVE;
    }
    
    public abstract int getArmor(RPGPlayer player);
    
}