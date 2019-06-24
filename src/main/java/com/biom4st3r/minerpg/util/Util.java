package com.biom4st3r.minerpg.util;


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
        System.out.println(count);
        return count > 0 ? new ItemStack(i,count) : ItemStack.EMPTY;
    }

}