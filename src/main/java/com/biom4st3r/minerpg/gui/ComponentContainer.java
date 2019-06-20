package com.biom4st3r.minerpg.gui;

import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ComponentContainer extends Container {

    public Inventory bag;
    //ShulkerBoxContainer
    public ComponentContainer(final int int_1, final PlayerInventory playerInventory_1) {
        this(int_1, playerInventory_1, (Inventory)new BasicInventory(4*3));
    }

    public ComponentContainer(int int1, PlayerInventory playerInv, Inventory components) {
        super(null, int1);
        bag = components;
        int xPos = 0;
        int yPos = 0;
        for(int row = 0; row < 3; row++)
        {
            for(int colum = 0; colum < 4; colum++)
            {
                int index = 4 * row + colum;
                this.addSlot(new ComponentSlot(bag, index, xPos+(colum*18), yPos+(row*18)));
            }
        }
        for(int int_8 = 0; int_8 < 3; ++int_8) {
            for(int int_7 = 0; int_7 < 9; ++int_7) {
               this.addSlot(new Slot(playerInv, int_7 + int_8 * 9 + 9, 8 + int_7 * 18, 84 + int_8 * 18));
            }
         }
   
         for(int int_8 = 0; int_8 < 9; ++int_8) {
            this.addSlot(new Slot(playerInv, int_8, 8 + int_8 * 18, 142));
         }
   

    }

    @Override
    public ItemStack transferSlot(final PlayerEntity playerEntity_1, final int int_1) {
        ItemStack itemStack_1 = ItemStack.EMPTY;
        final Slot slot_1 = this.slotList.get(int_1);
        if (slot_1 != null && slot_1.hasStack()) {
            final ItemStack itemStack_2 = slot_1.getStack();
            itemStack_1 = itemStack_2.copy();
            if (int_1 < this.bag.getInvSize()) {
                if (!this.insertItem(itemStack_2, this.bag.getInvSize(), this.slotList.size(), true)) {
                    return ItemStack.EMPTY;
                }
            }
            else if (!this.insertItem(itemStack_2, 0, this.bag.getInvSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemStack_2.isEmpty()) {
                slot_1.setStack(ItemStack.EMPTY);
            }
            else {
                slot_1.markDirty();
            }
        }
        return itemStack_1;
    }

    @Override
    public boolean canUse(PlayerEntity arg0) {
        return true;
    }
    
}