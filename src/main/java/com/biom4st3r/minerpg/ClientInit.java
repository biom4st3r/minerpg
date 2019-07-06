package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.gui.ComponentMenu;
import com.biom4st3r.minerpg.networking.Packets;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;

public class ClientInit implements ClientModInitializer
{
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            ComponentMenu::new);

        Packets.clientPacketReg();
    }

    
}