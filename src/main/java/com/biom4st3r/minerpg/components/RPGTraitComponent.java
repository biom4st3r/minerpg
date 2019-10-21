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
    public <T extends IComponent> void clone(T origin) {
        

    }

    @Override
    public <T extends IComponent> T getCopy() {
        
        return null;
    }


}