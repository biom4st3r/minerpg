package com.biom4st3r.minerpg.components;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.PacketByteBuf;

public interface AbstractComponent
{
    public <T extends AbstractComponent> void clone(T origin);

    public void serializeNBT(CompoundTag tag);

    public void deserializeNBT(CompoundTag tag);

    public void serializeBuffer(PacketByteBuf buf);

    public void deserializeBuffer(PacketByteBuf buf);

    public <T extends AbstractComponent> T getCopy();
}