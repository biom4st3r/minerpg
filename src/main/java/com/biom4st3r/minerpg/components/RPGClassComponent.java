package com.biom4st3r.minerpg.components;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.api.BufferSerializable;
import com.biom4st3r.minerpg.api.IComponent;
import com.biom4st3r.minerpg.api.NbtSerializable;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.interfaces.Reward;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.stat.Stat;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGClassComponent implements IComponent, BufferSerializable, NbtSerializable
{
    RPGPlayer owner;

    public LinkedHashMap<RPGClass,Integer> rpgClasses;

    private static final int maxClasses = 1;

    public static final String EXPERIENCE = "exp",
        RPG_COMPONENT = "rpgclasscomponent",
        CLASS_ID = "id",
        LEVEL = "lvl";

    private float[] experience;

    public RPGClassComponent(RPGPlayer owner)
    {
        init();
        this.owner = owner;
    }
    private void init()
    {
        rpgClasses = Maps.newLinkedHashMap();//LinkedHashMap()<RPGClass,Integer>(1);
        experience = new float[1];
        //abilities = new ArrayList<RPGAbility>(20);
        //abilityBar = new RPGAbility[9];
    }

    public float getExperience(int index)
    {
        return experience[index];
    }

    public int getLevel(int index)
    {
        return rpgClasses.get(this.getRpgClass(index));
    }

    public void setExperience(int index, float value)
    {
        this.experience[index] = value;
        this.updateLvl(index);
    }

    private void updateLvl(int index) {
        RPGClass selectedClass = this.getRpgClass(index);
        while(this.experience[index] > selectedClass.getExpRequiredForLvl(this.rpgClasses.get(selectedClass)+1))
        {
            int newLevel = this.rpgClasses.get(selectedClass)+1;
            applyRewards(selectedClass.givePlayerRewards(newLevel));
            this.rpgClasses.replace(selectedClass, newLevel);
        }
    }

    public void addExperience(int index, float value)
    {
        Biow0rks.debug("adding %s xp",value);
        this.setExperience(index, this.experience[index]+value);
        this.owner.getNetworkHandlerS().sendPacket(Packets.SERVER.sendExperience(this.experience[index]));
    }

    // public RPGAbility[] getAvalibleAbilities()
    // {
    //     RPGClass rpgc = getRpgClass(0);
    //     if(rpgc != null)
    //     {
    //         int lvl = getRpgClassContext(rpgc).Lvl;
    //         return rpgc.abilitysAvalibleAtLevel(lvl);
    //     }
    //     return new RPGAbility[] {RpgAbilities.NONE};
    // }

    public RPGClass getRpgClass(int index)
    {
        if(hasRpgClass())
        {
            Object[] t = rpgClasses.keySet().toArray();
            return (RPGClass)t[index];
        }
        return null;
    }

    public boolean hasRpgClass()
    {

        return rpgClasses.size() > 0;
    }

    public void applyRewards(Reward reward)
    {
        if(this.owner.getPlayer().world.isClient) return;
        
        Biow0rks.debug("isClient %s", this.owner.getPlayer().world.isClient);
        List<ItemStack> items = Lists.newArrayList();
        reward.give(owner, items);
        PlayerEntity pe = this.owner.getPlayer();
        for(ItemStack iS : items)
        {
            if(!pe.inventory.insertStack(iS))
            {
                pe.world.spawnEntity(new ItemEntity(pe.world,pe.x,pe.y,pe.z,iS));
            }
        }
    }

    public void addRpgClass(RPGClass rpgClass)
    {
        if(rpgClasses.size() >= maxClasses)
        {
            Biow0rks.error("class list already max size");
            return;
        }
        rpgClasses.put(rpgClass, 1);
        applyRewards(rpgClass.givePlayerRewards(1));
        float[] temp = experience.clone();
        experience = new float[rpgClasses.size()];
        for(int i = 0; i < temp.length; i++)
        {
            experience[i] = temp[i];
        }
    }

    public void replaceClass(RPGClass OldrpgClass, RPGClass NewrpgClass)
    {
        rpgClasses.remove(OldrpgClass);
        rpgClasses.put(NewrpgClass,1);
    }

    public void serializeBuffer(PacketByteBuf pbb)
    {
        Iterator<RPGClass> classes = rpgClasses.keySet().iterator();
        RPGClass rpgclass;
        pbb.writeInt(rpgClasses.size());
        int i = 0;
        while(classes.hasNext())
        {
            rpgclass = classes.next();
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
        Iterator<RPGClass> e = this.rpgClasses.keySet().iterator();
        RPGClass rpgclass;
        int i = 0;
        while(e.hasNext())
        {
            rpgclass = e.next();
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
        ListTag lt = (ListTag)ct.getTag(RPG_COMPONENT);
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
        for(int i = 0; i < rpgClasses.size();i++)
        {
            updateLvl(i);
        }
    }

    @Override
    public <T extends IComponent> void clone(T origin) {
        this.init();
        RPGClassComponent original = (RPGClassComponent)origin;
        Iterator<RPGClass> iterator = original.rpgClasses.keySet().iterator();
        while(iterator.hasNext())
        {
            RPGClass rpgClass = iterator.next();
            this.rpgClasses.put(rpgClass, original.rpgClasses.get(rpgClass).intValue());
        }
        this.experience = original.experience;
    }

    @Override
    public <T extends IComponent> T getCopy() {
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