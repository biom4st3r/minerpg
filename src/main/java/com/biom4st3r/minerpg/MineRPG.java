package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.items.ItemReg;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.Util;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MineRPG implements ModInitializer
{
    //BedBlock
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);


    @Override
    public void onInitialize() 
    {
        //EnderChestBlock
        //Registry.register(RPG_Registry.CLASS_REGISTRY, "barbarian", BarbarianClass);
        RpgAbilities.init();
        RpgClasses.init();
        ItemReg.init();
        Packets.serverPacketReg();
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                // if(!player.world.isClient)
                // {
                //     ((ServerPlayerEntity)player).networkHandler.sendPacket(Packets.SERVER.updateStats((RPGPlayer)player));
                // }
                buf.writeBlockPos(player.getBlockPos());
                return Util.toRPG(player).getComponentContainer();
            }
        );
        

    }
}