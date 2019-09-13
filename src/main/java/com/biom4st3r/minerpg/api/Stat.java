package com.biom4st3r.minerpg.api;

import com.biom4st3r.minerpg.MineRPG;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class Stat {
    private static String modid = MineRPG.MODID;


    public enum Stats {
        STRENGTH(modid + ":strength"), DEXTERITY(modid + ":dexterity"), INTELLIGENCE(modid + ":intelligence"),
        WISDOW(modid + ":wisdow"), CONSTITUTION(modid + ":constitution"), CHARISMA(modid + ":charisma");

        public final String text;

        Stats(final String text) {
            this.text = text;
        }
    }

    public static TranslatableText getComponent(Identifier i)
    {
        return new TranslatableText("stat" + i.toString().replace(":", "."), new Object[0]);
    }

    public static String getDisplayName(Identifier i)
    {
        return getComponent(i).asFormattedString();
        //net.minecraft.stat.Stats;
    }

}