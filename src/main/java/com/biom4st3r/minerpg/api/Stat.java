package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.MineRPG;

public enum Stat {

    STRENGTH(MineRPG.MODID + ":strength"), 
    DEXTERITY(MineRPG.MODID + ":dexterity"), 
    INTELLIGENCE(MineRPG.MODID + ":intelligence"),
    WISDOW(MineRPG.MODID + ":wisdow"), 
    CONSTITUTION(MineRPG.MODID + ":constitution"), 
    CHARISMA(MineRPG.MODID + ":charisma");
    
    /** identifier equivilant string */
    public final String text;

    Stat(final String text) {
        this.text = text;
    }
}