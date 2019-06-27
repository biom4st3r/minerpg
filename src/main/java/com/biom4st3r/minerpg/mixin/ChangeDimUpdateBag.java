package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.util.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerPlayerEntity.class)
public abstract class ChangeDimUpdateBag
{
    @Inject(at = @At("RETURN"), method = "changeDimension")
    public void changeDimension(DimensionType dimensionType_1, CallbackInfoReturnable<Entity> ci)
    {
        //tell client the items in ComponentBag to fix desync
        //((RPGPlayer)this).getComponentContainer()c
    }
}