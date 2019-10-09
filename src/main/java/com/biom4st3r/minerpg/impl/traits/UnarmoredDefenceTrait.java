package com.biom4st3r.minerpg.impl.traits;

import com.biom4st3r.minerpg.api.RPGTrait;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.api.TraitCatagory;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.util.Identifier;

public class UnarmoredDefenceTrait extends RPGTrait<Integer> {
    // PlayerEntity
    protected UnarmoredDefenceTrait(Identifier id, TraitCatagory tc) {
        super(id, tc);
    }
    
    // LivingEntity
    @Override
    public Integer output(RPGPlayer player) {
        int armor = 10 + player.getStatsComponent().getModifier(Stat.STRENGTH) + player.getStatsComponent().getModifier(Stat.CONSTITUTION);
        return armor;
    }
}