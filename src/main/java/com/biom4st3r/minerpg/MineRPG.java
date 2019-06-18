package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.client.gui.screen.ingame.FurnaceScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.container.FurnaceContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    public static final String COMPONENT_BAG = "componentbag";

    @Override
    public void onInitialize() {
        ContainerProviderRegistry.INSTANCE.registerFactory(
            new Identifier(MODID, COMPONENT_BAG),
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) -> new ComponentContainer(syncId, player.inventory,((RPGPlayer)(Object)player).componentInventory.bag));
        //InventoryScreen
        //MinecraftClient
        //ShulkerBoxContainer
        //ShulkerBoxScreen
    }

    
}