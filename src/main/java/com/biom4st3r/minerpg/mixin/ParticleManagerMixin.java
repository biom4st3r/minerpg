package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.particles.RpgDamageEffect;
import com.biom4st3r.minerpg.particles.RpgDamageParticle;
import com.biom4st3r.minerpg.util.ParticleRegHelper;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin implements ParticleRegHelper {

    @Shadow
    @Final
    private SpriteAtlasTexture particleAtlasTexture;

    @Shadow
    private <T extends ParticleEffect> void registerFactory(ParticleType<T> particleType_1,
            ParticleFactory<T> particleFactory_1) {

    }

    @Override
    public SpriteAtlasTexture getAtlas() {
        return particleAtlasTexture;
    }

    @Inject(method = "registerDefaultFactories", at = @At("RETURN"))
    private void registerCustomFactories(CallbackInfo cbi) {
        registerFactory(RpgDamageEffect.TYPE, (particleEffect,world,x,y,z,vx,vy,vz) ->
        {
            return new RpgDamageParticle(world, x, y, z, vx, vy, vz);
        });
    }



}