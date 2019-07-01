package com.biom4st3r.minerpg.impl.abilities.barbarian;

import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.util.Identifier;

public class RageAbility extends Ability 
{

    public RageAbility(Identifier name) {
        super(name);
    }

    @Override
    public void doAbility() {
        
    }

    @Override
    public boolean applyCost(RPGPlayer player) {
        return true;
    }

    @Override
    public boolean checkRequirements(RPGPlayer player) {
        return false;
    }



}