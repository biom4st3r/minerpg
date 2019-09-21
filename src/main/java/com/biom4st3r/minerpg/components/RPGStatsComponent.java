package com.biom4st3r.minerpg.components;

import java.util.HashMap;

import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.BufferSerializable;
import com.biom4st3r.minerpg.util.NbtSerializable;
import com.biom4st3r.minerpg.util.Util;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class RPGStatsComponent implements AbstractComponent, BufferSerializable, NbtSerializable
{
    HashMap<Stats, Integer> stats;

    private final String STATS = "rpgstatscomp";
    private final String SPAREPOINTS = "points";

    public int remainingPoints;

    public RPGStatsComponent()
    {
        stats = Maps.newHashMap();
        this.initStats();
    }


    public void increaseStatProtected(Stats s)
    {
        int old = this.stats.get(s);
        if(old < 20 && this.remainingPoints > 0)
        {
            this.stats.put(s, old+1);
            this.remainingPoints-=1;
        }
    }

    public void decreaseStatUnProtected(Stats s)
    {
        int old = this.stats.get(s);
        this.stats.put(s, old-1);
        this.remainingPoints++;
    }

    public RPGStatsComponent copyOfStats()
    {
        RPGStatsComponent rpgc = new RPGStatsComponent();
        for(Stats stat : Stats.values())
        {
            rpgc.stats.put(stat, this.getStat(stat));
        }
        rpgc.remainingPoints = this.remainingPoints;
        return rpgc;
    }

    public boolean clientRequestChanges(RPGStatsComponent client)
    {
        int delta = 0;

        for(Stats stat : Stats.values())
        {
            int serverStat = this.stats.get(stat);
            int clientStat = client.stats.get(stat);
            if(clientStat < serverStat)
            {
                Util.errorMSG("Client stat lower than Server.");
                return false;
            }
            delta+= clientStat-serverStat;
        }
        if(delta > this.remainingPoints)
        {
            Util.errorMSG("Client delta from server > remainingPoints");
            return false;
        }
        if(delta <= this.remainingPoints)
        {
            Util.debug(this.remainingPoints);
            Util.debug(delta);
            this.remainingPoints -= delta;
            for(Stats stat : Stats.values())
            {
                this.stats.put(stat, client.stats.get(stat));
                Util.debug(stat + " " + client.getStat(stat));
            }
            Util.debug(this.remainingPoints);
            Util.debug("Success");
            return true;
        }
        Util.errorMSG("unknown issue in stat compare");
        return false;
    }

    public void serializeNBT(CompoundTag tag)
    {
        CompoundTag statTag = new CompoundTag();
        if(this.stats.size() == 6)
        {
            for(Stats s : Stats.values())
            {
                statTag.putByte(s.text, (byte)(int)this.stats.get(s));
            }
        //}
            statTag.putByte(SPAREPOINTS, (byte)remainingPoints);
            tag.put(STATS, statTag);
        }
    }

    public void deserializeNBT(CompoundTag tag)
    {
        if(tag.getCompound(STATS).isEmpty() )//|| true)
        {
            //this.serialize(tag);
            this.remainingPoints = 27;
            for(Stats i : Stats.values())
            {
                this.stats.put(i, 8);
            }
            return;
        }
        CompoundTag statsTag = tag.getCompound(STATS);
        for(Stats i : Stats.values())
        {
            this.stats.put(i, (int)statsTag.getByte(i.text));
        }
        this.remainingPoints = ((int)statsTag.getByte(SPAREPOINTS));
    }

    public int getStatPoints()
    {
        return this.remainingPoints;
    }

    public int getModifier(Stat.Stats s)
    {
        return (int)Math.floor((this.getStat(s)-10)/2);
    }

    public void deserializeBuffer(PacketByteBuf pbb)
    {
        for(Stats s :Stats.values())
        {
            this.stats.put(s, (int)pbb.readByte());
        }
        int i = (int)pbb.readByte();
        this.setRemainingPoints(i);
    }

    public void initStats()
    {
        for(Stats stat : Stats.values())
        {
            this.stats.put(stat, 8);
        }
        this.remainingPoints = 27;
    }

    public void serializeBuffer(PacketByteBuf pbb)
    {

        if(this.stats.size() != 6)
        {
            initStats();
        }
        for(Stats s :Stats.values())
        {
            pbb.writeByte(this.stats.get(s));
        }
        pbb.writeByte(this.getStatPoints());
    }

    public <T extends AbstractComponent> void clone(T origin)
    {
        RPGStatsComponent original = (RPGStatsComponent)origin;
        for(Stats s : Stats.values())
        {
            this.stats.put(s, original.getStats().get(s).intValue());
        }
        this.remainingPoints = original.getStatPoints();
    }

    public int getStat(Stats name)
    {
        return this.stats.get(name) != null ? this.stats.get(name) : -1;
    }

    public void setRemainingPoints(int i)
    {
        this.remainingPoints = i;
    }

    public HashMap<Stats, Integer> getStats()
    {
        return this.stats;
    }

    @Override
    public <T extends AbstractComponent> T getCopy() {
        return null;
    }
}