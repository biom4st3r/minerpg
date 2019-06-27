package com.biom4st3r.minerpg.util;

import java.util.HashMap;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;

public class RPGComponent
{
    HashMap<Stats, Integer> stats;

    public int remainingPoints;

    public void init()
    {
        stats = Maps.newHashMap();
        remainingPoints = 0;
    }

    public CompoundTag serialize()
    {
        return null;
    }

    public void deserialize(CompoundTag tag)
    {
        if(tag.containsKey(STATS))
        {
            CompoundTag statsTag = tag.getCompound(STATS);
            for(Stats i : Stat.Stats.values())
            {
                this.stats.put(i, (int)statsTag.getByte(i.text));
            }
            this.freeStatPoints = tag.getByte(SPAREPOINTS);
        }
    }
}