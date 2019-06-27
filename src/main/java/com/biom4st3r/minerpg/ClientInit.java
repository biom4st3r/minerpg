package com.biom4st3r.minerpg;

import java.util.HashMap;
import java.util.HashSet;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.gui.RPGMenu;
import com.biom4st3r.minerpg.util.RPGPlayer;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class ClientInit implements ClientModInitializer
{


    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            RPGMenu::new);

        ClientSidePacketRegistry.INSTANCE.register(new Identifier("updaterpgstats"), (context,buffer) ->
        {
            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            HashMap<Stats, Integer> map = player.getStats();
            for(Stats stat: Stats.values())
            {
                map.put(stat, (int)buffer.readByte());
            }
            player
            
        });
    }
    public static CustomPayloadC2SPacket openRpgMenu()
    {
        return new CustomPayloadC2SPacket(MineRPG.COMPONENT_BAG_ID,new PacketByteBuf(Unpooled.buffer()));
    }
    
}