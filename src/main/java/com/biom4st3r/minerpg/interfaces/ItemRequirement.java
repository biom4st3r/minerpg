package com.biom4st3r.minerpg.interfaces;

import java.util.List;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.google.common.collect.Sets;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
@FunctionalInterface
public interface ItemRequirement extends Requirement<BasicInventory>
{
    public List<ItemStack> suppy();

    @Override
    default boolean meetsRequirements(RPGPlayer player,BasicInventory inv, Object... args) {
        BasicInventory bi = (BasicInventory) args[0];
        for(ItemStack iS : suppy())
        {
            Item item = iS.getItem();
            int quantity = iS.getCount();
            if(!bi.containsAnyInInv(Sets.newHashSet(item)))
            {
                return false;
            }
            if(bi.countInInv(item) < quantity)
            {
                return false;
            }
        }
        return true;
    }
}