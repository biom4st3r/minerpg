package com.biom4st3r.minerpg.api;

import net.minecraft.util.Tickable;

public interface IComponent extends Tickable
{
    public <T extends IComponent> void clone(T origin);

    // public void serializeNBT(CompoundTag tag);

    // public void deserializeNBT(CompoundTag tag);

    // public void serializeBuffer(PacketByteBuf buf);

    // public void deserializeBuffer(PacketByteBuf buf);

    public <T extends IComponent> T getCopy();

}