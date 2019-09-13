package com.biom4st3r.minerpg.mixin;

import java.util.Map;

import com.biom4st3r.minerpg.registery.MinerpgStatusEffect;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;

@Mixin(LivingEntity.class)
public abstract class PaperSkinEffectMixin
{
    @Shadow
    @Final
    private Map<StatusEffect, StatusEffectInstance> activeStatusEffects;


    public boolean hasEffect(StatusEffect se)
    {
        return this.activeStatusEffects.containsKey(se);
    }

    @Inject(at = @At(value="INVOKE", target="net/minecraft/entity/LivingEntity.hasStatusEffect(Lnet/minecraft/entity/effect/StatusEffect;)Z",ordinal = 0),method = "applyEnchantmentsToDamage")
    public void applyMagicToDamage(DamageSource source, float damage, CallbackInfoReturnable<Float> ci)
    {
        if(this.hasEffect(MinerpgStatusEffect.PAPER_SKIN) && source != DamageSource.DROWN && source != DamageSource.STARVE)
        {
            int modifier = 5;
            int level = activeStatusEffects.get(MinerpgStatusEffect.PAPER_SKIN).getAmplifier()+1;
            damage = Math.max(((modifier+level)*(float)damage)/modifier, 0.0f);
        }
    }
}