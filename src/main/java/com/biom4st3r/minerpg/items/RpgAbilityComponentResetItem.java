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
        // TODO Auto-generated constructor stub
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity pe, Hand hand) {
        // if(!world_1.isClient)
        // {
        RPGPlayer rpgPe = (RPGPlayer)pe;
        rpgPe.getRPGAbilityComponent().clone(new RPGAbilityComponent());
        
        //rpgPe.getNetworkHandlerS().sendPacket(Packets.SERVER.sendRPGClassComponent(rpgPe));
        //}StatusEffect
        //StatusEffects
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS,pe.getStackInHand(hand));
    }
    
}