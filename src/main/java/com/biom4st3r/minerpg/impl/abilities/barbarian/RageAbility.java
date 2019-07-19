package com.biom4st3r.minerpg.impl.abilities.barbarian;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class RageAbility extends RPGAbility 
{

    public RageAbility(Identifier name) {
        super(name);
        //MinecraftClient
        //ClientPlayerInteractionManager
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

    @Override
    public Type getType() {
        return Type.ACTIVED;
    }

    @Override
    public List<String> getToolTips() 
    {//Enchantment
        List<String> l = Lists.newArrayList();
        l.add(Formatting.GRAY+ this.name.getPath().toUpperCase() + Formatting.RESET);
        l.add(this.getType().toString());
        return l;
    }


}