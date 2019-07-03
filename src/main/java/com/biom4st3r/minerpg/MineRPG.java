package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.components.StatsComponents;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;


public class MineRPG implements ModInitializer
{
    //BedBlock
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);
    public static final Identifier SEND_STAT_UPDATE = new Identifier("updaterpgstats");
    public static final Identifier CHANGE_STAT = new Identifier("changestat");

    @Override
    public void onInitialize() 
    {
        //Registry.register(RPG_Registry.CLASS_REGISTRY, "barbarian", BarbarianClass);
        RpgAbilities.init();
        RpgClasses.init();
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
        ServerSidePacketRegistry.INSTANCE.register(CHANGE_STAT, (context,buffer) ->
        {
            //System.out.println(context.getPlayer() instanceof ClientPlayerEntity);
            StatsComponents rpgClientc = new StatsComponents();
            rpgClientc.fromBuffer(buffer);
            ((RPGPlayer)context.getPlayer()).getRPGComponent().clientRequestChanges(rpgClientc);

        });
    }
    

    public static CustomPayloadS2CPacket updateStats(RPGPlayer player)
    {
        PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
        //System.out.println(player.getComponentContainer() == null);
        //System.out.println(player instanceof ServerPlayerEntity);
        player.getRPGComponent().toBuffer(pbb);
    
        return new CustomPayloadS2CPacket(SEND_STAT_UPDATE,pbb);
    }
}