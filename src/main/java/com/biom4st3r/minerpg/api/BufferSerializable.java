package com.biom4st3r.minerpg.api;

import net.minecraft.util.PacketByteBuf;

public interface BufferSerializable
{
    /**
     * 
     * @param buf PacketByteBuffer that will be sent in packet. Meant tobe Written directly to
     */
    public void serializeBuffer(PacketByteBuf buf);


    /**
     * should reverse the packing done in {@link BufferSerializable#serializeBuffer(PacketByteBuf)} 
     * @param buf PacketByteBuffer received.
     * 
     */
    public void deserializeBuffer(PacketByteBuf buf);
}