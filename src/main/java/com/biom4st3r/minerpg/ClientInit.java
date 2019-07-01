package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.components.StatsComponents;
import com.biom4st3r.minerpg.gui.ComponentMenu;
import com.biom4st3r.minerpg.util.RPGPlayer;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.PacketByteBuf;

public class ClientInit implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            ComponentMenu::new);

        ClientSidePacketRegistry.INSTANCE.register(MineRPG.SEND_STAT_UPDATE, (context,buffer) ->
        {
            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            player.getRPGComponent().fromBuffer(buffer);

            
        });
    }
    public static CustomPayloadC2SPacket openRpgMenu()
    {
        return new CustomPayloadC2SPacket(MineRPG.COMPONENT_BAG_ID,new PacketByteBuf(Unpooled.buffer()));
    }

    public static CustomPayloadC2SPacket statChange(StatsComponents rpgc)
    {
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
        rpgc.toBuffer(pbb);
        return new CustomPayloadC2SPacket(MineRPG.CHANGE_STAT, pbb);
    }
    
}