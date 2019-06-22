package com.biom4st3r.minerpg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.container.Container;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(Container.class)
public abstract class SlotMixin
{

    @Inject(at = @At("RETURN"),method = "onSlotClick")
    public void onSlotClick(int slotIndex, int invIndex, SlotActionType action, PlayerEntity pe,CallbackInfoReturnable<ItemStack> ci)
    {
        //System.out.println(String.format("SlotIndex: %s\nInvIndex: %s\n Action: %s", slotIndex,invIndex,action.toString()));
    }
}