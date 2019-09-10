package com.biom4st3r.minerpg.items;

import com.biom4st3r.minerpg.MineRPG;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

public final class ItemReg
{
    public static void init()
    {
        Item.Settings s = new Item.Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.EPIC);
        reg("classreset",new RpgClassResetItem(s));
        reg("statreset", new RpgStatResetItem(s));
        reg("abilitycompreset", new RpgAbilityComponentResetItem(s));
    }

    private static Item reg(String name, Item i)
    {
        return Registry.register(Registry.ITEM, new Identifier(MineRPG.MODID,name), i);
    }
}