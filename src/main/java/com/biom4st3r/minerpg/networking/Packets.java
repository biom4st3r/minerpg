package com.biom4st3r.minerpg.networking;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.StatsComponent;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RPGPlayer;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class Packets
{
    public static final Identifier SEND_STAT_UPDATE = new Identifier(MineRPG.MODID,"sndstats");
    public static final Identifier SEND_RPG_COMPONENT = new Identifier(MineRPG.MODID, "sndrpgcomp");

    public static final Identifier REQ_RPG_COMPONENT = new Identifier(MineRPG.MODID, "reqrpgcomp");
    public static final Identifier REQ_CHANGE_STAT = new Identifier(MineRPG.MODID,"changestat");
    public static final Identifier REQ_ADD_CLASS = new Identifier(MineRPG.MODID,"addclass");
    public static final Identifier REQ_STAT_COMPONENT = new Identifier(MineRPG.MODID,"reqstatcomp");

    @Environment(EnvType.CLIENT)
    public static void clientPacketReg()
    {
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_RPG_COMPONENT, (context,buffer) ->
        {

            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            player.getRPGComponent().fromBuffer(buffer);

        });
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_STAT_UPDATE, (context,buffer) ->
        {
            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            player.getStatsComponent().fromBuffer(buffer);
        });

    }

    public static void serverPacketReg()
    {
        ServerSidePacketRegistry.INSTANCE.register(REQ_RPG_COMPONENT, (context,buffer) ->
        {

            ((ServerPlayerEntity)context.getPlayer()).networkHandler.sendPacket(SERVER.sendRPGComps(((RPGPlayer)context.getPlayer())));
        });
        ServerSidePacketRegistry.INSTANCE.register(MineRPG.COMPONENT_BAG_ID, (context,buffer) ->
        {
            ContainerProviderRegistry.INSTANCE.openContainer(MineRPG.COMPONENT_BAG_ID, context.getPlayer(), (blockpos)->{});
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_CHANGE_STAT, (context,buffer) ->
        {
            StatsComponent rpgClientc = new StatsComponent();
            rpgClientc.fromBuffer(buffer);
            ((RPGPlayer)context.getPlayer()).getStatsComponent().clientRequestChanges(rpgClientc);

        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_ADD_CLASS, (context,buffer) ->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getRPGComponent().addRpgClass(RPG_Registry.CLASS_REGISTRY.get(buffer.readIdentifier()));
            ((ServerPlayerEntity)context.getPlayer()).networkHandler.sendPacket(SERVER.sendRPGComps(player));
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_STAT_COMPONENT, (context,buffer) ->
        {
            ((ServerPlayerEntity)context.getPlayer()).networkHandler.sendPacket(SERVER.sendStats(((RPGPlayer)(context.getPlayer()))));
        });

    }
    
    @Environment(EnvType.CLIENT)
    public static class CLIENT
    {
        public static CustomPayloadC2SPacket openComponentBag()
        {
            return new CustomPayloadC2SPacket(MineRPG.COMPONENT_BAG_ID,new PacketByteBuf(Unpooled.buffer()));
        }

        public static CustomPayloadC2SPacket requestRpgComponent()
        {
            return new CustomPayloadC2SPacket(REQ_RPG_COMPONENT,new PacketByteBuf(Unpooled.buffer()));
        }
    
        public static CustomPayloadC2SPacket statChange(StatsComponent statC)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            statC.toBuffer(pbb);
            return new CustomPayloadC2SPacket(REQ_CHANGE_STAT, pbb);
        }
    
        public static CustomPayloadC2SPacket addClass(RPGClass rpgClass)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeIdentifier(rpgClass.name);
            return new CustomPayloadC2SPacket(REQ_ADD_CLASS,pbb);
        }

        public static CustomPayloadC2SPacket requestStatComp()
        {
            return new CustomPayloadC2SPacket(REQ_STAT_COMPONENT,new PacketByteBuf(Unpooled.buffer()));
        }
    }

    //@Environment(EnvType.SERVER)
    public static class SERVER
    {
        public static CustomPayloadS2CPacket sendStats(RPGPlayer player)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            player.getStatsComponent().toBuffer(pbb);
        
            return new CustomPayloadS2CPacket(Packets.SEND_STAT_UPDATE,pbb);
        }
    
        public static CustomPayloadS2CPacket sendRPGComps(RPGPlayer player)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            player.getRPGComponent().toBuffer(pbb);
            return new CustomPayloadS2CPacket(Packets.SEND_RPG_COMPONENT,pbb);
        }
    }

}