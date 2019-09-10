package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.mob.EvokerFangsEntity;

@Mixin(EvokerFangsEntity.class)
public abstract class EvokerAnimationMixin
{
    @Inject(at = @At("HEAD"),method="getAnimationProgress")
    public void asdf(CallbackInfoReturnable<Float> ci)
    {
        Util.debugV("animation", 7);
    }
}