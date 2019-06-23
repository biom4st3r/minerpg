package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.gui.RPGMenu;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

public class ClientInit implements ClientModInitializer
{


    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            RPGMenu::new);
    }
    public static CustomPayloadC2SPacket openRpgMenu()
    {
        return new CustomPayloadC2SPacket(MineRPG.COMPONENT_BAG_ID,new PacketByteBuf(Unpooled.buffer()));
    }
    
}