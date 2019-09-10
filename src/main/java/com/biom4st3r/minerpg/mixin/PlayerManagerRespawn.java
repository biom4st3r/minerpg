package com.biom4st3r.minerpg.mixin;

    /*
    Purpose
        Protects/maintains all custom data provided from RPGPlayer
        on death. Without this ComponentBag, Stats, Abilities
        , and class are all wiped on death

    */

import com.biom4st3r.minerpg.util.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
}