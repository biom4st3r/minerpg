package com.biom4st3r.minerpg.api;

import net.minecraft.nbt.CompoundTag;

public interface NbtSerializable 
{
    public void serializeNBT(CompoundTag tag);

    public void deserializeNBT(CompoundTag tag);
}