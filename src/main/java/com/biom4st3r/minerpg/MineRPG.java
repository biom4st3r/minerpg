package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;


public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);

    @Override
    public void onInitialize() {
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                return Util.toRPG(player).getComponentContainer();
            }
        );
        
        ServerSidePacketRegistry.INSTANCE.register(COMPONENT_BAG_ID, (context,buffer) ->
        {
            ContainerProviderRegistry.INSTANCE.openContainer(COMPONENT_BAG_ID, context.getPlayer(), (blockpos)->{});
        });

    }


    

    
}