package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RPG_Registry;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGComponent
{

    //private List<RPGClass> rpgclasses;
    public Hashtable<RPGClass,Integer> rpgClasses;

    private List<RPGAbility> abilities;

    private int maxClasses = 1;

    public static final String RPG_COMPONENT = "rpgcomponent";
    public static final String CLASS_ID = "id";
    public static final String LEVEL = "lvl";

    public RPGComponent()
    {
        rpgClasses = new Hashtable<RPGClass,Integer>(1);
        //rpgclasses = new ArrayList<RPGClass>(1);
        abilities = new ArrayList<RPGAbility>(20);
    }

    public boolean hasRpgClass()
    {
        return rpgClasses.size() > 0;
    }

    public void addRpgClass(RPGClass rpgClass)
    {
        if(rpgClasses.size() >= maxClasses)
        {
            System.out.println("class list already max size");
            return;
        }
        rpgClasses.put(rpgClass, 1);

    }

    public void replaceClass(RPGClass OldrpgClass, RPGClass NewrpgClass)
    {
        rpgClasses.remove(OldrpgClass);
        rpgClasses.put(NewrpgClass,1);
    }

    public void toBuffer(PacketByteBuf pbb)
    {
        Enumeration<RPGClass> classes = rpgClasses.keys();
        RPGClass rpgclass;
        pbb.writeInt(rpgClasses.size());
        while(classes.hasMoreElements())
        {
            rpgclass = classes.nextElement();
            pbb.writeIdentifier(rpgclass.name);
            pbb.writeInt(rpgClasses.get(rpgclass));
        }

    }
    public void fromBuffer(PacketByteBuf pbb)
    {
        int size = pbb.readInt();
        for(int i = 0; i < size; i++)
        {
            rpgClasses.put(RPG_Registry.CLASS_REGISTRY.get(pbb.readIdentifier()), pbb.readInt());
        }
    }

    public void writeNbt(CompoundTag ct)
    {
        ListTag lt = new ListTag();
        Enumeration<RPGClass> e = this.rpgClasses.keys();
        RPGClass rpgclass;
        while(e.hasMoreElements())
        {
            rpgclass = e.nextElement();
            CompoundTag set = new CompoundTag();
            set.putString(CLASS_ID, rpgclass.name.toString());
            set.putInt(LEVEL, this.rpgClasses.get(rpgclass));
            lt.add(set);
        }
        ct.put(RPG_COMPONENT, lt);
        
    }

    public static RPGComponent readNbt(CompoundTag ct)
    {
        RPGComponent rpgC = new RPGComponent();
        System.out.println(rpgC.rpgClasses == null);
        ListTag lt = ct.getList(RPG_COMPONENT, 10);
        RPGClass rpgclass;
        int lvl;
        System.out.println(lt.toString());
        for(int i = 0; i < lt.size(); i++)
        {
            rpgclass = RPG_Registry.CLASS_REGISTRY.get(new Identifier(lt.getCompoundTag(i).getString(CLASS_ID)));
            if(rpgclass == null)
            {
                continue;
            }
            System.out.println(lt.getCompoundTag(i).getString(CLASS_ID) + " here i am");
            lvl = lt.getCompoundTag(i).getInt(LEVEL);
            rpgC.rpgClasses.put(rpgclass, lvl);
            //rpgClasses.put(lt.getCompoundTag(i)., value)
        }
        return rpgC;
    }
    


}