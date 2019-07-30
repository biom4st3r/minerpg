package com.biom4st3r.minerpg.util;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;

public interface BasicInventoryHelper
{
    public void _setInvStack(int index, ItemStack iS);

    public void copy(BasicInventory original);

    // public void serializeBuffer(PacketByteBuf pbb);
    // public void deserializeBuffer(PacketByteBuf pbb);
}