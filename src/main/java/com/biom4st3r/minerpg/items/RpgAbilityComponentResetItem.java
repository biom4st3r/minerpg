package com.biom4st3r.minerpg.items;

import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RpgAbilityComponentResetItem extends Item
{

    public RpgAbilityComponentResetItem(Settings item$Settings_1) {
        super(item$Settings_1);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity pe, Hand hand) {
        RPGPlayer rpgPe = (RPGPlayer)pe;
        rpgPe.getRPGAbilityComponent().clone(new RPGAbilityComponent((RPGPlayer)pe));
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS,pe.getStackInHand(hand));
    }
    
}