package com.biom4st3r.minerpg.util;

import java.util.HashMap;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayerEntity;

public interface RPGPlayer
{
    public ComponentContainer getComponentContainer();

    public void setComponentContainer(ComponentContainer cc);

    public HashMap<Stat.Stats,Integer> getStats();

    public BasicInventory deserialize(ListTag lt, int size);

    public ListTag serialize(Inventory bi);

    public void respawn(ServerPlayerEntity spe);

    public int getStatPoints();
}