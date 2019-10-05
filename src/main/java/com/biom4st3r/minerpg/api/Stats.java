package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.MineRPG;

public enum Stats {

    

    STRENGTH(MineRPG.MODID + ":strength"), 
    DEXTERITY(MineRPG.MODID + ":dexterity"), 
    INTELLIGENCE(MineRPG.MODID + ":intelligence"),
    WISDOW(MineRPG.MODID + ":wisdow"), 
    CONSTITUTION(MineRPG.MODID + ":constitution"), 
    CHARISMA(MineRPG.MODID + ":charisma");

    
    public final String text;

    Stats(final String text) {
        this.text = text;
    }
}