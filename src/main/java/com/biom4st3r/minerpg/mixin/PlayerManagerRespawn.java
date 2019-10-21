package com.biom4st3r.minerpg.mixin;



import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

@Mixin(PlayerManager.class)
/**
Purpose: Properly transfers component to new PlayerEntity on death
*/
public abstract class PlayerManagerRespawn
{
    @Inject(at = @At("RETURN"),method = "respawnPlayer",cancellable = true)
    public void respawnPlayer(ServerPlayerEntity originalPlayerEntity, DimensionType dimensionType_1, boolean boolean_1,CallbackInfoReturnable<ServerPlayerEntity> ci)
    {
        ServerPlayerEntity playersNewEntity = ci.getReturnValue();
        ((RPGPlayer)playersNewEntity).respawn(originalPlayerEntity);
        ci.setReturnValue(playersNewEntity);
    }
}