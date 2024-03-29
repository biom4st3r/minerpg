package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.gui.ComponentMenu;
import com.biom4st3r.minerpg.networking.Packets;

import org.lwjgl.glfw.GLFW;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.fabricmc.fabric.api.client.render.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;

public class ClientInit implements ClientModInitializer {
    public static KeyBinding swapHotBar;

    @Override
    public void onInitializeClient() {
        // EntityRenderDispatcher
        EntityRendererRegistry.INSTANCE.register(Fireball.class, (entityRenderDispatcher, context) -> {
            return new FlyingItemEntityRenderer<Fireball>(entityRenderDispatcher, context.getItemRenderer(), 10F);
        });

        ClientSpriteRegistryCallback.event(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).register((atlasTexture, registry)->
        {
            for(int i = 0; i <= 9; i++)
            {
                registry.register(new Identifier(MineRPG.MODID, "rpg_damage"+i));
            }
        });

        ScreenProviderRegistry.INSTANCE.registerFactory(
            MineRPG.COMPONENT_BAG_ID, 
            ComponentMenu::new);

        Packets.clientPacketReg();
        //Biow0rks.debug("Added minerpg keybind catagory %s", Key);
        KeyBindingRegistry.INSTANCE.addCategory(MineRPG.MODID);
        swapHotBar = FabricKeyBinding.Builder.create(new Identifier(MineRPG.MODID,"swaphotbars"), InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_GRAVE_ACCENT/*`*/ , MineRPG.MODID).build();
        KeyBindingRegistry.INSTANCE.register((FabricKeyBinding)swapHotBar);
        //Biow0rks.debug("` keybind registered %s", KeyBindingRegistry.INSTANCE.register((FabricKeyBinding)swapHotBar));
        //RecipeManager
        //CraftingTableContainer
    }
}