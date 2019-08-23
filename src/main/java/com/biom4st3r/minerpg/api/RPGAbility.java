package com.biom4st3r.minerpg.api;

import java.util.List;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

public abstract class RPGAbility
{
    protected RPGAbility(Identifier name,int coolDownDuration)
    {
        this.name = name;
        this.coolDownDuration = coolDownDuration;
    }

    protected int coolDownDuration;

    public int getCoolDown()
    {
        return coolDownDuration;
    }


    public final Identifier name;

    public Identifier getIcon()
    {
        return new Identifier(name.getNamespace(),"abilities/icons/" + name.getPath() + ".png");
        //return this.name; // TODO: Should return translation key line Item
        //Item
    }

    public boolean isCooledDown(RPGPlayer player)
    {
        return player.getRPGAbilityComponent().timeouts.containsKey(name);
    }

    public abstract void doAbility(RPGPlayer player);

    //public abstract boolean applyCost(RPGPlayer player);

    //public abstract boolean checkRequirements(RPGPlayer player);

    public abstract Type getType();

    public enum Type{
        PASSIVE,
        RIGHT_CLICK,
        LEFT_CLICK,
        USE
        
    }
    public abstract List<String> getToolTips();

    @Override
    public String toString() {
        return this.name.toString();
    }

}