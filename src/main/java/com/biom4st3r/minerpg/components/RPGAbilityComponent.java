package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements AbstractComponent {

    public  List<RPGAbility> abilities;

    public RPGAbility[] abilityBar;

    //public Hashtable<RPGAbility, Object> abilityResults;

    public RPGAbilityComponent()
    {
        abilities = new ArrayList<RPGAbility>();
        abilityBar = new RPGAbility[9];
    }

    @Override
    public <T extends AbstractComponent> void clone(T origin) {

    }

    @Override
    public void serializeNBT(CompoundTag tag) {

    }

    @Override
    public void deserializeNBT(CompoundTag tag) {

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