package com.biom4st3r.minerpg.components;

import java.util.HashMap;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class StatsComponent
{
    HashMap<Stats, Integer> stats;

    private final String STATS = "rpgstats";
    private final String SPAREPOINTS = "points";

    public int remainingPoints;

    public StatsComponent()
    {
        stats = Maps.newHashMap();
        // for(Stats stat : Stats.values())
        // {
        //     this.stats.put(stat,0);
        // }
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

    public StatsComponent copyOfStats()
    {
        StatsComponent rpgc = new StatsComponent();
        for(Stats stat : Stats.values())
        {
            rpgc.stats.put(stat, this.getStat(stat));
        }
        rpgc.remainingPoints = this.remainingPoints;
        return rpgc;
    }

    public boolean clientRequestChanges(StatsComponent client)
    {
        int delta = 0;

        for(Stats stat : Stats.values())
        {
            int serverStat = this.stats.get(stat);
            int clientStat = client.stats.get(stat);
            if(clientStat < serverStat)
            {
                System.out.println("wtf: Client stat lower than Server.");
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
                System.out.println(stat + " " + client.getStat(stat));
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

    public void deserialize(CompoundTag tag)
    {
        if(tag.getCompound(STATS).isEmpty() )//|| true)
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

    public void initStats()
    {
        System.out.println("Init Stats");
        for(Stats stat : Stats.values())
        {
            this.stats.put(stat, 8);
        }
        this.remainingPoints = 27;
    }

    public void toBuffer(PacketByteBuf pbb)
    {
        //PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());

        if(this.stats.size() != 6)
        {
            initStats();
        }
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
            this.stats.put(s, rpgpe.getStatsComponent().getStats().get(s));
        }
        this.remainingPoints = rpgpe.getStatsComponent().getStatPoints();
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

    public static void doRandomDebugShit(RPGPlayer pe)
    {
        //((PlayerEntity)pe);
        //((PlayerEntity)pe).abilities.allowFlying = true;
        //((PlayerEntity)pe).abilities.invulnerable = true;
    }
}