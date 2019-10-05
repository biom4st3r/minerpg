package com.biom4st3r.minerpg.interfaces;

import java.util.List;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface Reward
{
    public void give(RPGPlayer player, List<ItemStack> list);
}