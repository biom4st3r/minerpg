package com.biom4st3r.minerpg;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.StatsComponents;
import com.biom4st3r.minerpg.gui.InventoryTab;
import com.biom4st3r.minerpg.impl.rpgclass.BarbarianClass;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;


public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);
    public static final Identifier SEND_STAT_UPDATE = new Identifier("updaterpgstats");
    public static final Identifier CHANGE_STAT = new Identifier("changestat");
    public static final RPGClass BarbarianClass = new BarbarianClass(new Identifier(MODID, "barbarian"));

    @Override
    public void onInitialize() 
    {
        Registry.register(RPG_Registry.CLASS_REGISTRY, "barbarian", BarbarianClass);

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