package com.biom4st3r.minerpg.components;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.RpgClassContext;
import com.biom4st3r.minerpg.util.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGClassComponent implements AbstractComponent
{

    //private List<RPGClass> rpgclasses;
    public Hashtable<RPGClass,Integer> rpgClasses;

    //private List<RPGAbility> abilities;
    //ServerPlayerEntity

    private int maxClasses = 1;

    public static final String RPG_COMPONENT = "rpgclasscomponent";
    public static final String CLASS_ID = "id";
    public static final String LEVEL = "lvl";
    
    //public RPGAbility[] abilityBar;

    public RPGClassComponent()
    {
        init();
    }
    private void init()
    {
        rpgClasses = new Hashtable<RPGClass,Integer>(1);
        //abilities = new ArrayList<RPGAbility>(20);
        //abilityBar = new RPGAbility[9];
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
        // try
        // {
        //     return rpgClasses.get(rpgclass).intValue();
        // }
        // catch(NullPointerException e)
        // {
        //     return 1;
        // }
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
        while(classes.hasMoreElements())
        {
            rpgclass = classes.nextElement();
            pbb.writeIdentifier(rpgclass.name);
            pbb.writeInt(rpgClasses.get(rpgclass));
        }

    }
    public void deserializeBuffer(PacketByteBuf pbb)
    {
        int size = pbb.readInt();
        for(int i = 0; i < size; i++)
        {
            rpgClasses.put(RPG_Registry.CLASS_REGISTRY.get(pbb.readIdentifier()), pbb.readInt());
        }
    }

    public void serializeNBT(CompoundTag ct)
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

    public void deserializeNBT(CompoundTag ct)
    {
        ListTag lt = ct.getList(RPG_COMPONENT, 10);
        RPGClass rpgclass;
        int lvl;

        for(int i = 0; i < lt.size(); i++)
        {
            rpgclass = RPG_Registry.CLASS_REGISTRY.get(new Identifier(lt.getCompoundTag(i).getString(CLASS_ID)));
            if(rpgclass == null)
            {
                continue;
            }
            lvl = lt.getCompoundTag(i).getInt(LEVEL);
            this.rpgClasses.put(rpgclass, lvl);
            //ClientPlayerInteractionManager
            //System.out.println(this.getRpgClass(0));
            //Util.debugV(this.getRpgClass(0), 10);
            //rpgClasses.put(lt.getCompoundTag(i)., value)
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

    // @Override
    // public int hashCode() {
    //     int i = super.hashCode();
    //     for(RPGClass s : this.rpgClasses.keySet())
    //     {
    //         i ^= s.hashCode();
    //         i ^= rpgClasses.get(s).hashCode();
    //     }
    //     return i;
    // }

    


}