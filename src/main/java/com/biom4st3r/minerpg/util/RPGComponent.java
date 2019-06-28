package com.biom4st3r.minerpg.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.google.common.collect.Maps;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class RPGComponent
{
    HashMap<Stats, Integer> stats;

    private final String STATS = "rpgstats";
    private final String SPAREPOINTS = "points";

    protected ArrayList<Ability> abilities;

    protected ArrayList<RPGClass> classes;

    public int remainingPoints;

    public RPGComponent()
    {
        stats = Maps.newHashMap();
        //remainingPoints = 0;
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

    public RPGComponent copy()
    {
        RPGComponent rpgc = new RPGComponent();
        for(Stats stat : Stats.values())
        {
            rpgc.stats.put(stat, this.getStat(stat));
        }
        rpgc.remainingPoints = this.remainingPoints;
        return rpgc;
    }

    public boolean compareAndUpdate(RPGComponent client)
    {
        int delta = 0;

        for(Stats stat : Stats.values())
        {
            int serverStat = this.stats.get(stat);
            int clientStat = client.stats.get(stat);
            if(clientStat < serverStat)
            {
                System.out.println("wtf: Client stat lower than Server");
                return false;
            }
            delta+= clientStat-serverStat;
        }
        if(delta > this.remainingPoints)
        {
            System.out.println("wtf: Client delta from server > remainingPoints");
            return false;
        }
        if(delta <= this.remainingPoints)
        {
            System.out.println(this.remainingPoints);
            System.out.println(delta);
            this.remainingPoints -= delta;
            for(Stats stat : Stats.values())
            {
                this.stats.put(stat, client.stats.get(stat));
                System.out.println(stat);
            }
            System.out.println(this.remainingPoints);
            System.out.println("Success");
            return true;
        }
        System.out.println("WTF: no clue");
        return false;
    }

    public void serialize(CompoundTag tag)
    {
        
        CompoundTag statTag = new CompoundTag();
        System.out.println("hello0");
        System.out.println(!tag.containsKey(STATS, 10));
        if(!tag.containsKey(STATS, 10))
        {
            System.out.println("hello1");
            for(Stats i : Stats.values())
            {   
                System.out.println("hello2");
                statTag.putByte(i.text,(byte)8);
                this.stats.put(i,8);
            }
            this.setRemainingPoints(27);
            statTag.putByte(SPAREPOINTS, (byte)27);
        }
        else
        {
            for(Stats s : Stats.values())
            {
                statTag.putByte(s.text, (byte)(int)this.stats.get(s));
            }
        }
        statTag.putByte(SPAREPOINTS, (byte)remainingPoints);
        tag.put(STATS, statTag);
    }

    public void deserialize(CompoundTag tag)
    {
        if(!tag.containsKey(STATS, 10))
        {
            System.out.println("first init");
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
        //System.out.println(statsTag.getByte(SPAREPOINTS));
        this.remainingPoints = ((int)statsTag.getByte(SPAREPOINTS));
        //System.out.println(remainingPoints);
        //System.out.println(statsTag.getByte(SPAREPOINTS));
        
    }

    public int getStatPoints()
    {
        return this.remainingPoints;
    }

    public void fromBuffer(PacketByteBuf pbb)
    {
        for(Stats s :Stats.values())
        {
            this.stats.put(s, (int)pbb.readByte());
        }
        int i = (int)pbb.readByte();
        //System.out.println(i); 
        this.setRemainingPoints(i);
    }

    public void toBuffer(PacketByteBuf pbb)
    {
        //PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
        for(Stats s :Stats.values())
        {
            //System.out.println(this.stats.get(s) == null);
            pbb.writeByte(this.stats.get(s));
        }
        pbb.writeByte(this.getStatPoints());
    }

    public void updatStats(PlayerEntity pe)
    {
        if(pe.world.isClient)
        {
            return;
        }
        RPGPlayer rpgpe = (RPGPlayer)pe;
        for(Stats s : Stats.values())
        {
            this.stats.put(s, rpgpe.getRPGComponent().getStats().get(s));
        }
        this.remainingPoints = rpgpe.getRPGComponent().getStatPoints();
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
}