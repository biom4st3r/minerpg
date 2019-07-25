package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.networking.Packets;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class CreateClientPlayerMixin
{
    @Inject(at = @At("RETURN"), method = "createPlayer")
    public void createPlayer(CallbackInfoReturnable<ClientPlayerEntity> ci) 
    {
        ClientPlayNetworkHandler cph = ci.getReturnValue().networkHandler;
        cph.sendPacket(Packets.CLIENT.requestRpgClassComponent());
        cph.sendPacket(Packets.CLIENT.requestStatComp());
        cph.sendPacket(Packets.CLIENT.requestAbilityComp());
    }
}