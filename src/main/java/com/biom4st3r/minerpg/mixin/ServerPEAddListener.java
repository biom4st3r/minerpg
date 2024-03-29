package com.biom4st3r.minerpg.mixin;



import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(ServerPlayerEntity.class)
/**
Provides synconization for the ComponentBag
*/
public abstract class ServerPEAddListener
{
    @Inject(at = @At("TAIL"),method = "method_14235")
    public void addListenerToInventory(CallbackInfo ci)
    {
        ((RPGPlayer)(Object)this).getComponentContainer().addListener((ServerPlayerEntity)(Object)this);
        
    }
}