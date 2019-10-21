package com.biom4st3r.minerpg.mixin;

    
import com.biom4st3r.minerpg.gui.GUIhelper;

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
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.TranslatableText;

@Mixin(InventoryScreen.class)
    /**
    Purpose
        Provides custom tabs for the the mods menus 
    */
public abstract class VanillaInventory extends AbstractInventoryScreen<PlayerContainer> implements RecipeBookProvider {
    public VanillaInventory(PlayerContainer container_1, PlayerInventory playerInventory_1, TranslatableText component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    
    private int xMid()
    {
        return this.width/2;
    }
    private int yMid()
    {
        return this.height/2;
    }

    @Inject(at = @At("TAIL"),method = "init",cancellable = false)
    protected void init(CallbackInfo ci)
    {
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()+GUIhelper.drawTabOffset, yPos, this.minecraft, true,false,false,false))
        {
            this.addButton(button);
        }
    }

    protected <T extends AbstractButtonWidget> T addButton(T abw)
    {
        this.children.add(abw);
        this.buttons.add(abw);
        return abw;
    }
    
}