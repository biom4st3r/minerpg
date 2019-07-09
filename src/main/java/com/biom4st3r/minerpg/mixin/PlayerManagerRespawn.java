package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.util.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerRespawn
{
    @Inject(at = @At("RETURN"),method = "respawnPlayer",cancellable = true)
    public void respawnPlayer(ServerPlayerEntity originalPlayerEntity, DimensionType dimensionType_1, boolean boolean_1,CallbackInfoReturnable<ServerPlayerEntity> ci)
    {
        ServerPlayerEntity playersNewEntity = ci.getReturnValue();
        ((RPGPlayer)playersNewEntity).respawn(originalPlayerEntity);
        ci.setReturnValue(playersNewEntity);
    }

    @Inject(at = @At("HEAD"),method = "method_14594")
    public void updateInventory(ServerPlayerEntity spe,CallbackInfo ci)
    {
        //spe.openContainer(((RPGPlayer)spe).getComponentContainer());
    }
}