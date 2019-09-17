package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.mixin_interfaces.BasicInventoryHelper;

import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ComponentSlot extends Slot {

    int invSlot;
    
    //ShulkerBoxSlot

    public ComponentSlot(Inventory inventory_1, int invSlot, int xPos, int yPos) {
        super(inventory_1, invSlot, xPos, yPos);
        this.invSlot = invSlot;
    }
    
    @Override
    public void setStack(ItemStack itemStack_1) {
        ((BasicInventoryHelper) this.inventory)._setInvStack(this.invSlot, itemStack_1);

        this.markDirty();
    }

    @Override
    public boolean canInsert(ItemStack itemStack_1) {
        return itemStack_1.isEnchantable() || itemStack_1.getItem() == Items.BOOK;
    }

    @Override
    public ItemStack getStack() {
        return this.inventory.getInvStack(this.invSlot);
    }
    @Override
    public int getMaxStackAmount() {
        return maxStackSize;
    }
    @Override
    public int getMaxStackAmount(ItemStack itemStack_1) {
        return maxStackSize;
    }
    int maxStackSize = 512;
    
}