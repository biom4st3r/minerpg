package com.biom4st3r.minerpg.components;

import java.util.Map;

import com.biom4st3r.minerpg.api.BufferSerializable;
import com.biom4st3r.minerpg.api.IComponent;
import com.biom4st3r.minerpg.api.NbtSerializable;
import com.biom4st3r.minerpg.api.RPGTrait;
import com.biom4st3r.minerpg.api.TraitCatagory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public class RPGTraitComponent implements IComponent, BufferSerializable, NbtSerializable 
{

    public Map<TraitCatagory,RPGTrait<?>> activeTraits;

    public <T> RPGTrait<T> isUsingTrait(TraitCatagory tc)
    {
        return activeTraits.containsKey(tc) ? (RPGTrait<T>)activeTraits.get(tc) : (RPGTrait<T>)RPGTrait.NONE;
    }

    @Override
    public void tick() {
        // TODO Auto-generated method stub

    }

    @Override
    public void serializeNBT(CompoundTag tag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void serializeBuffer(PacketByteBuf buf) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deserializeBuffer(PacketByteBuf buf) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends IComponent> void clone(T origin) {
        // TODO Auto-generated method stub

    }

    @Override
    public <T extends IComponent> T getCopy() {
        // TODO Auto-generated method stub
        return null;
    }


}