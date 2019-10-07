package com.biom4st3r.minerpg.items;

import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RpgStatResetItem extends Item
{

    public RpgStatResetItem(Settings item$Settings_1) 
    {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity pe, Hand hand) {
        RPGPlayer rpgPe = (RPGPlayer)pe;
        rpgPe.getStatsComponent().clone(new RPGStatsComponent());
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, pe.getStackInHand(hand));
    }
}