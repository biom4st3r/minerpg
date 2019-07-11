package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.util.InGameHudHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{

    @Shadow
    public InGameHud inGameHud;

    @Inject(
        at = 
            @At(
                value = "INVOKE",
                target = "net/minecraft/client/options/KeyBinding.wasPressed()Z",
                ordinal = 3),
        method="handleInputEvents"
        )
    private void handleInputEvents(CallbackInfo ci)
    {
        while(ClientInit.swapHotBar.wasPressed())
        {
            ((InGameHudHelper)inGameHud).toggleRenderAbilityBar();
        }
    }


}