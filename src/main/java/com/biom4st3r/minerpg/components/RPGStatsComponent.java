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

    public RPGStatsComponent(int str, int dex,int intel,int wis,int con,int cha)
    {
        this.stats = Maps.newHashMap();
        stats.put(Stat.STRENGTH, str);
        stats.put(Stat.DEXTERITY,dex);
        stats.put(Stat.INTELLIGENCE,intel);
        stats.put(Stat.WISDOW, wis);
        stats.put(Stat.CONSTITUTION,con);
        stats.put(Stat.CHARISMA,cha);
    }

    public RPGStatsComponent()
    {
        stats = Maps.newHashMap();
        this.initStats();
    }

    /**
     * increases a {@link Stat} by 1. Also prevents stats from exceeding 20 and makes sure their are points to spend.
     * 
     * Only used in {@link com.biom4st3r.minerpg.gui.screens.StatMenu}
     * @param stat affected stat
     */
    public void increaseStatProtected(Stat stat)
    {
        int old = this.stats.get(stat);
        if(old < 20 && this.remainingPoints > 0)
        {
            this.stats.put(stat, old+1);
            this.remainingPoints-=1;
        }
    }

    /**
     * Removes points from a {@link Stat}. also adds that point back to {@link RPGStatsComponent#remainingPoints}
     * 
     * Only used in {@link com.biom4st3r.minerpg.gui.screens.StatMenu}
     * @param stat affected stat
     */
    public void decreaseStatUnProtected(Stat stat)
    {
        int old = this.stats.get(stat);
        this.stats.put(stat, old-1);
        this.remainingPoints++;
    }

    /**
     * Only used in {@link com.biom4st3r.minerpg.gui.screens.StatMenu}. 
     * Makes sure the total check to the Clients RPGStatComponet is valid. 
     *  * Stat hasn't decreased since last requested check
     *  * Stats increased don't exceed {@link RPGStatsComponent#remainingPoints}
     * 
     * Called whenever the player finalizes changes to their stats.
     * @param client copy of the clients 
     * @return false if the changes were not valid
     */
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
            statTag.putByte(SPAREPOINTS, (byte)remainingPoints);
            tag.put(STATS, statTag);
        }
    }

    public void deserializeNBT(CompoundTag tag)
    {
        if(tag.getCompound(STATS).isEmpty())
        {
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

    /**
     * 
     * @param stat the stat in question
     * @return a typical tabletop style modifier derived from stat
     */
    public int getModifier(Stat stat)
    {
        return (int)Math.floor((this.getStat(stat)-10)/2);
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

    /**
     * Destructive
     * Resets all stats to 8
     */
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

    /**
     * 
     * @param stat stat in question
     * @return the current value of stat.
     */
    public int getStat(Stat stat)
    {
        return this.stats.get(stat) != null ? this.stats.get(stat) : -1;
    }

    /**
     * Destructive. sets the value of {@link RPGStatsComponent#remainingPoints}
     * @param i
     */
    public void setRemainingPoints(int i)
    {
        this.remainingPoints = i;
    }

    
    public HashMap<Stat, Integer> getStats()
    {
        return this.stats;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RPGStatsComponent getCopy() {
        RPGStatsComponent rpgc = new RPGStatsComponent();
        for(Stat stat : Stat.values())
        {
            rpgc.stats.put(stat, this.getStat(stat));
        }
        rpgc.remainingPoints = this.remainingPoints;
        return rpgc;
    }

    @Override
    public void tick() {
    }
}