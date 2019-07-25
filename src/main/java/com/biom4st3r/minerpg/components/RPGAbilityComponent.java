package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements AbstractComponent {

    public  List<RPGAbility> specialAbilities;

    public DefaultedList<RPGAbility> abilityBar;

    public static final String ABILITY_LIST = "rpgAbilList";
    public static final String ABILITY_LIST_SIZE = "rpgAbLSize";
    public static final String SLOT_ID = "slotid";
    public static final String ABILITY_ID = "abilid";
    public static final String ABILITY_BAR = "abilibar";


    //public Hashtable<RPGAbility, Object> abilityResults;

    public RPGAbilityComponent()
    {
        specialAbilities = new ArrayList<RPGAbility>();
        abilityBar = DefaultedList.ofSize(9, RpgAbilities.NONE);
        //MinecraftServer
    }

    @Override
    public <T extends AbstractComponent> void clone(T origin) {

    }

    @Override
    public void serializeNBT(CompoundTag tag) {
        tag.putInt(ABILITY_LIST_SIZE, specialAbilities.size());
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
            lt.add(new StringTag(abilityBar.get(i).name.toString()));
        }
        tag.put(ABILITY_BAR, lt);
        
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        int size = tag.getInt(ABILITY_LIST_SIZE);
        ListTag lt = tag.getList(ABILITY_LIST, 10);
        int i;
        for(i = 0; i < size; i++)
        {
            specialAbilities.add( getAbility(new Identifier(lt.getString(i))));
        }
        lt = tag.getList(ABILITY_BAR, 10);
        for(i = 0; i < 9 ; i++)
        {
            abilityBar.set(i, getAbility(new Identifier(lt.getString(i))));
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