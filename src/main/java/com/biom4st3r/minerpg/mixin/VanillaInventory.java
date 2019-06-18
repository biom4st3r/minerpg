package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.gui.RPGMenu;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;

@Mixin(InventoryScreen.class)
public abstract class VanillaInventory extends AbstractInventoryScreen<PlayerContainer> implements RecipeBookProvider {
    public VanillaInventory(PlayerContainer container_1, PlayerInventory playerInventory_1, Component component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    @Inject(at = @At("TAIL"),method = "init",cancellable = false)
    protected void init(CallbackInfo ci)
    {//                                 xpos,  ypos , width, height, 
        int bWidth = 26;
        int bHeight = 14;
        this.addButton(new ButtonWidget((this.width/2)-bWidth, (this.height/2)-97, bWidth, bHeight, "Main", (ButtonWidget) ->
        {
            
        }));
        this.addButton(new ButtonWidget((this.width/2), (this.height/2)-97, bWidth, bHeight, "RPG", (ButtonWidget) ->
        {
            this.minecraft.openScreen(new RPGMenu((PlayerEntity)this.minecraft.player));
        }));
    }

    protected <T extends AbstractButtonWidget> T addButton(T abw)
    {
        this.children.add(abw);
        this.buttons.add(abw);
        return abw;
    }
    
}