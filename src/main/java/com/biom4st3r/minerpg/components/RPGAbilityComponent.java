package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RpgAbilityContext;
import com.biom4st3r.minerpg.util.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements AbstractComponent {

    public  List<RPGAbility> specialAbilities;

    public DefaultedList<RpgAbilityContext> abilityBar;

    public static final String ABILITY_LIST = "rpgAbilList";
    public static final String ABILITY_LIST_SIZE = "rpgAbLSize";
    public static final String SLOT_ID = "slotid";
    public static final String ABILITY_ID = "abilid";
    public static final String ABILITY_BAR = "abilibar";


    //public Hashtable<RPGAbility, Object> abilityResults;

    public RPGAbilityComponent()
    {
        specialAbilities = new ArrayList<RPGAbility>();
        abilityBar = DefaultedList.ofSize(9, RpgAbilityContext.EMPTY);
        //MinecraftServer
    }

    @Override
    public <T extends AbstractComponent> void clone(T origin) {

    }

    @Override
    public void serializeNBT(CompoundTag tag) {
        ListTag lt = new ListTag();
        for(RPGAbility a : specialAbilities)
        {
            lt.add(new StringTag(a.name.toString()));
        }
        tag.put(ABILITY_LIST, lt);
        lt = new ListTag();
        //CompoundTag ct;
        for(int i = 0; i < 9; i++)
        {
            RpgAbilityContext rac = abilityBar.get(i);
            CompoundTag ct = new CompoundTag();
            rac.serializeNbt(ct);
            lt.add(ct);
        }
        tag.put(ABILITY_BAR, lt);
        Util.debug(tag.getType(ABILITY_BAR));
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        Util.debug(tag);
        ListTag lt = tag.getList(ABILITY_LIST,10);
        int i;
        for(i = 0; i < lt.size(); i++)
        {
            specialAbilities.add( getAbility(new Identifier(lt.getString(i))));
        }
        lt = tag.getList(ABILITY_BAR, 10);
        Util.debug(lt.size());
        Util.debug(lt);
        for(i = 0; i < 9 && lt.size() > 0 ; i++)
        {
            RpgAbilityContext rac = new RpgAbilityContext(null, -1, null);
            rac.deserializeNbt(lt.getCompoundTag(i));
            this.abilityBar.set(i, rac);
            
        }

    }
    private static RPGAbility getAbility(Identifier i)
    {
        return RPG_Registry.ABILITY_REGISTRY.get(i);
    }

    @Override
    public void serializeBuffer(PacketByteBuf buf) {

    }

    @Override
    public void deserializeBuffer(PacketByteBuf buf) {
        
    }

    @Override
    public <T extends AbstractComponent> T getCopy() {
        return null;
    }

}