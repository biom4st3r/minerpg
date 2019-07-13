package com.biom4st3r.minerpg.util;


import java.lang.reflect.Array;

import com.biom4st3r.minerpg.MineRPG;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Util
{
    public static RPGPlayer toRPG(PlayerEntity pe)
    {
        return (RPGPlayer)(Object)pe;
    }
    
    public static CompoundTag ShortItemStackToTag(ItemStack iS, CompoundTag tag) {
        Identifier identifier_1 = Registry.ITEM.getId(iS.getItem());
        tag.putString("id", identifier_1 == null ? "minecraft:air" : identifier_1.toString());
        tag.putShort("Count", (short)iS.getCount());

        if (iS.getTag() != null) {
            tag.put("tag", iS.getTag());
        }

        return tag;
    }

    public static ItemStack TagToShortItemStack(CompoundTag tag)
    {
        Item i = Registry.ITEM.get(new Identifier( tag.getString("id")));
        int count = 0;
        count = tag.getShort("Count");
        return count > 0 ? new ItemStack(i,count) : ItemStack.EMPTY;
    }
    static boolean  isTestBuild = true;
    public static void debug(Object o)
    {
        if(isTestBuild)
        {
            System.out.println(o.toString());
        }
    }
    public static void debugV(Object o, int depth)
    {
        if(isTestBuild)
        {
            StackTraceElement[] ste = new Exception().getStackTrace();
            for(int i = 1; i <= depth; i++)
            {
                debug(ste[i].getClassName() + "." + ste[i].getMethodName());
            }
            debug(o);
        }
    }

    public static void errorMSG(Object o)
    {
        System.out.println(MineRPG.MODID + ": " + o.toString());
    }

    public static <T extends Object> T[] reverse(T[] array)
    {
        @SuppressWarnings("unchecked")
        T[] reversed = (T[])Array.newInstance(array[0].getClass(), array.length);
        for(int i = 0; i < array.length; i++)
        {
            reversed[i] = array[(array.length-1)-i];
        }
        return reversed;
    }
}