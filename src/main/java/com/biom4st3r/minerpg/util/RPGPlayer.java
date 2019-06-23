package com.biom4st3r.minerpg.util;

import java.util.List;

import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.gui.ComponentContainer;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.ListTag;

public interface RPGPlayer
{
    public ComponentContainer getComponentContainer();

    public void setComponentContainer(ComponentContainer cc);

    public List<Stat> getStats();

    public BasicInventory deserialize(ListTag lt, int size);

    public ListTag serialize(Inventory bi);

    
}