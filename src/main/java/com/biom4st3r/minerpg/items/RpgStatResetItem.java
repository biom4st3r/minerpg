package com.biom4st3r.minerpg.items;

import com.biom4st3r.minerpg.components.StatsComponent;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RpgStatResetItem extends Item
{

    public RpgStatResetItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity pe, Hand hand) {
        // if(!world.isClient)
        // {
        RPGPlayer rpgPe = (RPGPlayer)pe;
        rpgPe.getStatsComponent().clone(new StatsComponent());
        //rpgPe.getNetworkHandlerS().sendPacket(Packets.SERVER.sendStats(rpgPe));
        //}
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, pe.getStackInHand(hand));
    }
}