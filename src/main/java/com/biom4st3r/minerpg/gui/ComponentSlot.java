package com.biom4st3r.minerpg.gui;

import net.minecraft.container.Slot;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class ComponentSlot extends Slot {

    public ComponentSlot(Inventory inventory_1, int int_1, int int_2, int int_3) {
        super(inventory_1, int_1, int_2, int_3);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean canInsert(ItemStack itemStack_1) {
        return itemStack_1.isEnchantable();
    }
    
}