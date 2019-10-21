package com.biom4st3r.minerpg.api;

import net.minecraft.nbt.CompoundTag;

public interface NbtSerializable 
{
    /**
     * for saving all nessisary information 
     * @param tag CompoundTag that will be saved. Meant to be written directly to
     * 
     */
    public void serializeNBT(CompoundTag tag);


    /**
     * should reverse the packing done in {@link NbtSerializable#serializeNBT(CompoundTag)} 
     * @param tag
     * 
     */
    public void deserializeNBT(CompoundTag tag);
    //Inject
}