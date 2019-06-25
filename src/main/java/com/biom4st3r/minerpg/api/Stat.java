package com.biom4st3r.minerpg.api;

import java.util.Arrays;
import java.util.List;
import com.biom4st3r.minerpg.MineRPG;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.Identifier;

public class Stat
{
    private static String modid = MineRPG.MODID;
    // private static final List<Identifier> stats = Arrays.asList(
    //     stat(modid,"strength"),
    //     stat(modid,"dexterity"),
    //     stat(modid,"intelligence"),
    //     stat(modid,"wisdow"),
    //     stat(modid,"constitution"),
    //     stat(modid,"charisma")
    // );

    public enum Stats
    {
        STRENGTH(modid + ":strength"),
        DEXTERITY(modid + ":dexterity"),
        INTELLIGENCE(modid + ":intelligence"),
        WISDOW(modid + ":wisdow"),
        CONSTITUTION(modid + ":constitution"),
        CHARISMA(modid + ":charisma");

        private final String text;
        Stats(final String text)
        {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    // public Stat(Identifier name, int defaultValue)
    // {
    //     this.name = name;
    // }

    // protected int value;

    // public final Identifier name;

    // public static boolean addStat(Stat s)
    // {
    //     if(!stats.contains(s.name))
    //     {
    //         stats.add(s.name);
    //         System.out.println(String.format("%s: %s stat registed!", MineRPG.MODID,s.name));
    //         return true;
    //     }
    //     System.out.println(String.format("%s as already been registered", s.name));
    //     return false;
    // }

    // public static boolean addStat(Identifier i)
    // {
    //     return addStat(new Stat(i,0));
    // }

    // public static boolean addStat(String modid, String name)
    // {
    //     return addStat(new Identifier(modid,name));
    // }

    private static Identifier stat(String modid, String name)
    {
        return new Identifier(modid, name);
    }

    public static TranslatableComponent getComponent(Identifier i)
    {
        return new TranslatableComponent("stat" + i.toString().replace(":", "."), new Object[0]);
    }

    public static String getDisplayName(Identifier i)
    {
        return getComponent(i).getFormattedText();
    }

    // public static List<Identifier> getStats()
    // {
    //     return Stat.stats;
    // }


}