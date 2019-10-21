package com.biom4st3r.minerpg.api;

import net.minecraft.util.Tickable;

public interface IComponent extends Tickable
{
    /**
     * Turned this Component into an exact clone of origin
     * @param <T> Represents your components type
     * @param origin Source object
     * 
     */


    public <T extends IComponent> void clone(T origin);

    // public void serializeNBT(CompoundTag tag);

    // public void deserializeNBT(CompoundTag tag);

    // public void serializeBuffer(PacketByteBuf buf);

    // public void deserializeBuffer(PacketByteBuf buf);


    /**
     * 
     * @param <T> Represents you components type
     * @return returns an exact clone of this object
     */
    public <T extends IComponent> T getCopy();

}