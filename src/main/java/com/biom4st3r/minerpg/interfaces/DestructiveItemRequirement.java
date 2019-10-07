package com.biom4st3r.minerpg.interfaces;

import java.util.List;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.google.common.collect.Sets;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface DestructiveItemRequirement extends ItemRequirement,DestructiveRequirement<BasicInventory>
{

    @Override
    default void destory(BasicInventory bi) {
        for(ItemStack iS : suppy())
        {
            int taken = 0;
            for(int i = 0; i < bi.getInvSize(); i++)
            {
                ItemStack target = bi.getInvStack(i);
                if(target.isItemEqualIgnoreDamage(iS))
                {
                    int sumRemaining = iS.getCount()-taken;
                    if(target.getCount() >= sumRemaining)
                    {
                        target.decrement(iS.getCount());
                        break;
                    }
                    int temp = target.getCount();
                    target.decrement(Math.min(target.getCount(),sumRemaining));
                    taken+=temp;
                }
            }
        }
    }

    @Override
    default boolean meetsRequirements(RPGPlayer player,BasicInventory inv, Object... args) {
        BasicInventory bi = (BasicInventory) args[0];
        List<ItemStack> items = suppy();
        for(ItemStack iS : items)
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