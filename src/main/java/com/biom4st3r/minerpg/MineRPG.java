package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;


public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);


    @Override
    public void onInitialize() {
        // for(String s : new String[] {"strength","dexterity","intelligence","wisdow","constitution","charisma"})
        // {
        //     Stat.addStat(MODID ,s);
        // }
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                if(!player.world.isClient)
                {
                    ((ServerPlayerEntity)player).networkHandler.sendPacket(updateStats((RPGPlayer)player));
                }
                buf.writeBlockPos(player.getBlockPos());
                return Util.toRPG(player).getComponentContainer();
            }
        );

        
        ServerSidePacketRegistry.INSTANCE.register(COMPONENT_BAG_ID, (context,buffer) ->
        {
            
            ContainerProviderRegistry.INSTANCE.openContainer(COMPONENT_BAG_ID, context.getPlayer(), (blockpos)->{});
        });

    }

    public static CustomPayloadS2CPacket updateStats(RPGPlayer player)
    {
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
        for(Stats stat : Stats.values())
        {
            pbb.writeByte(player.getStat(stat));
        }
        pbb.writeByte(player.getStatPoints());
        return new CustomPayloadS2CPacket(new Identifier("updaterpgstats"),pbb);
    }
}