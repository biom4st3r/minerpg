package com.biom4st3r.minerpg.api;

import net.minecraft.util.PacketByteBuf;

public interface BufferSerializable
{
    public void serializeBuffer(PacketByteBuf buf);

    public void deserializeBuffer(PacketByteBuf buf);
}