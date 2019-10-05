package com.biom4st3r.minerpg.components;

import java.util.HashMap;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.api.BufferSerializable;
import com.biom4st3r.minerpg.api.IComponent;
import com.biom4st3r.minerpg.api.NbtSerializable;
import com.biom4st3r.minerpg.api.Stat;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class RPGStatsComponent implements IComponent, BufferSerializable, NbtSerializable
{
    HashMap<Stat, Integer> stats;

    private final String STATS = "rpgstatscomp";
    private final String SPAREPOINTS = "points";

    public int remainingPoints;

    public RPGStatsComponent()
    {
        stats = Maps.newHashMap();
        this.initStats();
    }

    public void increaseStatProtected(Stat s)
    {
        int old = this.stats.get(s);
        if(old < 20 && this.remainingPoints > 0)
        {
            this.stats.put(s, old+1);
            this.remainingPoints-=1;
        }
    }

    public void decreaseStatUnProtected(Stat s)
    {
        int old = this.stats.get(s);
        this.stats.put(s, old-1);
        this.remainingPoints++;
    }

    public RPGStatsComponent copyOfStats()
    {
        RPGStatsComponent rpgc = new RPGStatsComponent();
        for(Stat stat : Stat.values())
        {
            rpgc.stats.put(stat, this.getStat(stat));
        }
        rpgc.remainingPoints = this.remainingPoints;
        return rpgc;
    }

    public boolean clientRequestChanges(RPGStatsComponent client)
    {
        int delta = 0;

        for(Stat stat : Stat.values())
        {
            int serverStat = this.stats.get(stat);
            int clientStat = client.stats.get(stat);
            if(clientStat < serverStat)
            {
                Biow0rks.error("Client stat lower than Server.");
                return false;
            }
            delta+= clientStat-serverStat;
        }
        if(delta > this.remainingPoints)
        {
            Biow0rks.error("Client delta from server greater than remainingPoints");
            return false;
        }
        if(delta <= this.remainingPoints)
        {
            Biow0rks.debug("remainingPoints %s",this.remainingPoints);
            Biow0rks.debug("point delta: %s",delta);
            this.remainingPoints -= delta;
            for(Stat stat : Stat.values())
            {
                this.stats.put(stat, client.stats.get(stat));
                Biow0rks.debug(stat + " " + client.getStat(stat));
            }
            Biow0rks.debug("remaining points after transaction: %s", this.remainingPoints);
            Biow0rks.debug("Success");
            return true;
        }
        Biow0rks.error("unknown issue in stat compare");
        return false;
    }

    public void serializeNBT(CompoundTag tag)
    {
        CompoundTag statTag = new CompoundTag();
        if(this.stats.size() == 6)
        {
            for(Stat s : Stat.values())
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
            for(Stat i : Stat.values())
            {
                this.stats.put(i, 8);
            }
            return;
        }
        CompoundTag statsTag = tag.getCompound(STATS);
        for(Stat i : Stat.values())
        {
            this.stats.put(i, (int)statsTag.getByte(i.text));
        }
        this.remainingPoints = ((int)statsTag.getByte(SPAREPOINTS));
    }

    public int getStatPoints()
    {
        return this.remainingPoints;
    }

    public int getModifier(Stat s)
    {
        return (int)Math.floor((this.getStat(s)-10)/2);
    }

    public void deserializeBuffer(PacketByteBuf pbb)
    {
        for(Stat s :Stat.values())
        {
            this.stats.put(s, (int)pbb.readByte());
        }
        int i = (int)pbb.readByte();
        this.setRemainingPoints(i);
    }

    public void initStats()
    {
        for(Stat stat : Stat.values())
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
        for(Stat s :Stat.values())
        {
            pbb.writeByte(this.stats.get(s));
        }
        pbb.writeByte(this.getStatPoints());
    }

    public <T extends IComponent> void clone(T origin)
    {
        RPGStatsComponent original = (RPGStatsComponent)origin;
        for(Stat s : Stat.values())
        {
            this.stats.put(s, original.getStats().get(s).intValue());
        }
        this.remainingPoints = original.getStatPoints();
    }

    public int getStat(Stat name)
    {
        return this.stats.get(name) != null ? this.stats.get(name) : -1;
    }

    public void setRemainingPoints(int i)
    {
        this.remainingPoints = i;
    }

    public HashMap<Stat, Integer> getStats()
    {
        return this.stats;
    }

    @Override
    public <T extends IComponent> T getCopy() {
        return null;
    }
}