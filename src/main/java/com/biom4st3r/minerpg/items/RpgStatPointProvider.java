package com.biom4st3r.minerpg.items;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RpgStatPointProvider extends Item
{

    public RpgStatPointProvider(Settings item$Settings_1) {
        super(item$Settings_1);
        // TODO Auto-generated constructor stub
    }

    @Override 
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity pe, Hand hand) 
    {
        ((RPGPlayer)pe).getStatsComponent().remainingPoints+=1;
        pe.getStackInHand(hand).decrement(1);
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, pe.getStackInHand(hand));
    }
}