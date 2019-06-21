package com.biom4st3r.minerpg.gui;

import net.minecraft.container.ShulkerBoxSlot;
import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ComponentSlot extends Slot {

    int invSlot;
    //ShulkerBoxSlot

    public ComponentSlot(Inventory inventory_1, int invSlot, int xPos, int yPos) {
        super(inventory_1, invSlot, xPos, yPos);
        this.invSlot = invSlot;
    }

    @Override
    public boolean canInsert(ItemStack itemStack_1) {
        return itemStack_1.isEnchantable();
    }

    @Override
    public ItemStack getStack() {
        return this.inventory.getInvStack(this.invSlot);
    }
    
}