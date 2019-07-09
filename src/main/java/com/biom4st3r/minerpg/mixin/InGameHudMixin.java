package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.util.InGameHudHelper;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements InGameHudHelper 
{
    private boolean abilityBar = false;


    @Override
    public void toggleRenderAbilityBar() {
        abilityBar = !abilityBar;
    }

    @Inject(at = @At("HEAD"),method="renderHotBra",cancellable = true)
    protected void renderHotbar(float float_1,CallbackInfo ci)
    {
        if(abilityBar)
        {





            ci.cancel();
        }
    }



}