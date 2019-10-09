package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.MineRPG;

import net.minecraft.util.Identifier;

public class TraitCatagory {
    public static final TraitCatagory 
        ARMOR_OVERRIDE  = TraitCatagory.create(new Identifier(MineRPG.MODID, "armoroverride")),
        DAMAGE_OVERRIDE = TraitCatagory.create(new Identifier(MineRPG.MODID, "damageoverride")),
        JUMP_OVERRIDE   = TraitCatagory.create(new Identifier(MineRPG.MODID, "jumpoverride")),
        SPEED_OVERRIDE  = TraitCatagory.create(new Identifier(MineRPG.MODID, "speedoverride")),
        NONE            = TraitCatagory.create(new Identifier(MineRPG.MODID, "NONE"))
        
        
        ;

    public TraitCatagory(Identifier name) {
        this.name = name;
    }

    public Identifier name;

    public static TraitCatagory create(Identifier name) {
        return new TraitCatagory(name);
    }

}