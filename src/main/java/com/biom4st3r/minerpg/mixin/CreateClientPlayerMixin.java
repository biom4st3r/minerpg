package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class CreateClientPlayerMixin
{
    @Inject(at = @At("RETURN"), method = "createPlayer")
    public void createPlayer(CallbackInfoReturnable<ClientPlayerEntity> ci) 
    {
        Util.requestAllComponents(ci.getReturnValue().networkHandler);
    }
}