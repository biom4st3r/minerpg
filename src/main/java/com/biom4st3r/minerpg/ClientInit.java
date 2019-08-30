package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.gui.ComponentMenu;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.util.Util;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class ClientInit implements ClientModInitializer
{
    public static KeyBinding swapHotBar;

    @Override
    public void onInitializeClient() {
        //EntityRenderDispatcher
        EntityRendererRegistry.INSTANCE.register(Fireball.class, (entityRenderDispatcher, context) ->
        {
            
            return new FlyingItemEntityRenderer<Fireball>(entityRenderDispatcher, context.getItemRenderer(), 5F);
        });



        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            ComponentMenu::new);

        Packets.clientPacketReg();
        KeyBindingRegistry.INSTANCE.addCategory(MineRPG.MODID);
        swapHotBar = FabricKeyBinding.Builder.create(new Identifier(MineRPG.MODID,"swaphotbars"), InputUtil.Type.KEYSYM, 41/*`*/ , MineRPG.MODID).build();
        KeyBindingRegistry.INSTANCE.register((FabricKeyBinding)swapHotBar);
    }
    //ClientPlayNetworkHandler
    //EntityRenderDispatcher

    
}