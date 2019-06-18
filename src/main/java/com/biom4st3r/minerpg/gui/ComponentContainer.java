package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.util.ComponentSlot;

import net.minecraft.container.AnvilContainer;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class ComponentContainer extends Container {

    public Inventory bag;
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
    public boolean canUse(PlayerEntity arg0) {
        return true;
    }
    
}