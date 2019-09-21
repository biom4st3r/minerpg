package com.biom4st3r.minerpg.components;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.BufferSerializable;
import com.biom4st3r.minerpg.util.NbtSerializable;
import com.biom4st3r.minerpg.util.RpgClassContext;
import com.biom4st3r.minerpg.util.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGClassComponent implements AbstractComponent, BufferSerializable, NbtSerializable
{

    //private List<RPGClass> rpgclasses;
    public Hashtable<RPGClass,Integer> rpgClasses;

    private int maxClasses = 1;

    public static final String EXPERIENCE = "exp",
    RPG_COMPONENT = "rpgclasscomponent",
    CLASS_ID = "id",
    LEVEL = "lvl";

    private float[] experience;

    public RPGClassComponent()
    {
        init();
    }
    private void init()
    {
        rpgClasses = new Hashtable<RPGClass,Integer>(1);
        experience = new float[1];
        //abilities = new ArrayList<RPGAbility>(20);
        //abilityBar = new RPGAbility[9];
    }

    public float getExperiance(int index)
    {
        return experience[index];
    }

    public void setExperience(int index, float value)
    {
        this.experience[index] = value;
    }
    public void addExperience(int index, float value)
    {
        Util.debug(value);
        this.experience[index]+=value;
    }

    public RPGAbility[] getAvalibleAbilities()
    {
        RPGClass rpgc = getRpgClass(0);
        if(rpgc != null)
        {
            int lvl = getRpgClassContext(rpgc).Lvl;
            return rpgc.abilitysAvalibleAtLevel(lvl);
        }
        return new RPGAbility[] {RpgAbilities.NONE};
    }

    public RPGClass getRpgClass(int index)
    {
        if(hasRpgClass())
        {
            Object[] t = rpgClasses.keySet().toArray();
            return (RPGClass)t[index];
        }
        return null;
    }
    

    public RpgClassContext getRpgClassContext(RPGClass rpgclass)
    {   
        try
        {
            return new RpgClassContext(rpgclass, rpgClasses.get(rpgclass).intValue());
        }
        catch(NullPointerException e)
        {
            return RpgClassContext.EMPTY;
        }
        
    }

    public boolean hasRpgClass()
    {

        return rpgClasses.size() > 0;
    }

    public void addRpgClass(RPGClass rpgClass)
    {
        if(rpgClasses.size() >= maxClasses)
        {
            Util.errorMSG("class list already max size");
            return;
        }
        rpgClasses.put(rpgClass, 1);

    }

    public void replaceClass(RPGClass OldrpgClass, RPGClass NewrpgClass)
    {
        rpgClasses.remove(OldrpgClass);
        rpgClasses.put(NewrpgClass,1);
    }

    public void serializeBuffer(PacketByteBuf pbb)
    {
        Enumeration<RPGClass> classes = rpgClasses.keys();
        RPGClass rpgclass;
        pbb.writeInt(rpgClasses.size());
        int i = 0;
        while(classes.hasMoreElements())
        {
            rpgclass = classes.nextElement();
            pbb.writeIdentifier(rpgclass.id);
            pbb.writeInt(rpgClasses.get(rpgclass));
            pbb.writeFloat(experience[i]);
            i++;
        }

    }
    public void deserializeBuffer(PacketByteBuf pbb)
    {
        int size = pbb.readInt();
        experience = new float[size];
        for(int i = 0; i < size; i++)
        {
            rpgClasses.put(RPG_Registry.CLASS_REGISTRY.get(pbb.readIdentifier()), pbb.readInt());
            experience[i] = pbb.readFloat();
        }
    }

    public void serializeNBT(CompoundTag ct)
    {
        ListTag lt = new ListTag();
        Enumeration<RPGClass> e = this.rpgClasses.keys();
        RPGClass rpgclass;
        int i = 0;
        while(e.hasMoreElements())
        {
            rpgclass = e.nextElement();
            CompoundTag set = new CompoundTag();
            set.putString(CLASS_ID, rpgclass.id.toString());
            set.putInt(LEVEL, this.rpgClasses.get(rpgclass));
            set.putFloat(EXPERIENCE, experience[i]);
            lt.add(set);
            i++;
        }
        ct.put(RPG_COMPONENT, lt);
        
    }

    public void deserializeNBT(CompoundTag ct)
    {
        ListTag lt = ct.getList(RPG_COMPONENT, 10);
        RPGClass rpgclass;
        int lvl;
        experience = new float[lt.size()];
        for(int i = 0; i < lt.size(); i++)
        {
            rpgclass = RPG_Registry.CLASS_REGISTRY.get(new Identifier(lt.getCompoundTag(i).getString(CLASS_ID)));
            if(rpgclass == null)
            {
                continue;
            }
            experience[i] = lt.getCompoundTag(i).getFloat(EXPERIENCE);
            lvl = lt.getCompoundTag(i).getInt(LEVEL);
            this.rpgClasses.put(rpgclass, lvl);
        }
    }

    @Override
    public <T extends AbstractComponent> void clone(T origin) {
        this.init();
        RPGClassComponent original = (RPGClassComponent)origin;
        Iterator<RPGClass> iterator = original.rpgClasses.keySet().iterator();
        while(iterator.hasNext())
        {
            RPGClass rpgClass = iterator.next();
            this.rpgClasses.put(rpgClass, original.rpgClasses.get(rpgClass).intValue());
        }
    }

    @Override
    public <T extends AbstractComponent> T getCopy() {
        return null;
    }

    public void processStat(Stat<?> stat,int amount) 
    {    
        int i = 0;
        
        for(RPGClass rpgClass : this.rpgClasses.keySet())
        {
            float worth = rpgClass.getStatWorthAtLevel(stat, amount, this.rpgClasses.get(rpgClass).intValue());
            if(worth > 0.0)
                this.addExperience(i, worth);
            i++;
        }
    }
    
    public void processExperience(int amount)
    {
        int i = 0;
        for(RPGClass rpgClass : this.rpgClasses.keySet())
        {
            float worth = rpgClass.getExperienceWorthAtLevel(amount,this.rpgClasses.get(rpgClass).intValue());
            if(worth > 0.0f)
                this.addExperience(i, worth);
            i++;
        }
    }
}