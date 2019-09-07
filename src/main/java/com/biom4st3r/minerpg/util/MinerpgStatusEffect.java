package com.biom4st3r.minerpg.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

@Mixin(StatusEffect.class)
public abstract class MinerpgStatusEffect {

    public static StatusEffect create(StatusEffectType set, int hexColor) {
        Constructor<?> construct = StatusEffect.class.getDeclaredConstructors()[0];
        construct.setAccessible(true);
        StatusEffect se = null;
        try {
            se = (StatusEffect)construct.newInstance(set, hexColor);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e1) {
            e1.printStackTrace();
        }
        
        return se;
    }
    
    
}