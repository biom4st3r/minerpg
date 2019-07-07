package com.biom4st3r.minerpg.mixin;

import java.util.List;

import com.biom4st3r.minerpg.util.BasicInventoryHelper;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.InventoryListener;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

@Mixin(BasicInventory.class)
public abstract class BasicInventoryMaxSize implements BasicInventoryHelper {
    
    @Shadow
    @Final
    private DefaultedList<ItemStack> stackList;
    
    @Shadow
    private List<InventoryListener> listeners;

    @Shadow
    public void markDirty()
    {

    }

    @Override
    public void _setInvStack(int index, ItemStack iS) {
        this.stackList.set(index, iS);
  
        this.markDirty();
    }
}