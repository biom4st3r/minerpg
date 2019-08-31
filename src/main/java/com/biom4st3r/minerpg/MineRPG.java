package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.items.ItemReg;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, COMPONENT_BAG);

    @SuppressWarnings({"unchecked"})
    public static final EntityType<Fireball> FIREBALL = (EntityType)Registry.register(
        Registry.ENTITY_TYPE, new Identifier(MODID,"fireball"), 
        FabricEntityTypeBuilder.create(EntityCategory.MISC, (type,world) ->
        {
            return new Fireball(world,(EntityType)type);
        }).build());
    

    @Override
    public void onInitialize() 
    {
        RpgAbilities.init();
        RpgClasses.init();
        ItemReg.init();
        Packets.serverPacketReg();
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                buf.writeBlockPos(player.getBlockPos());
                return ((RPGPlayer)player).getComponentContainer();
            }
        );
    }

}