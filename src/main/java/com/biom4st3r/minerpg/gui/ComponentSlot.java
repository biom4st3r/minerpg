package com.biom4st3r.minerpg.gui;

import net.minecraft.container.ShulkerBoxSlot;
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
    public boolean canInsert(ItemStack itemStack_1) {
        return itemStack_1.isEnchantable() || itemStack_1.getItem() == Items.BOOK;
    }

    @Override
    public ItemStack getStack() {
        return this.inventory.getInvStack(this.invSlot);
    }
    @Override
    public int getMaxStackAmount() {
        return 512;
    }
    @Override
    public int getMaxStackAmount(ItemStack itemStack_1) {
        return 512;
    }
    
}