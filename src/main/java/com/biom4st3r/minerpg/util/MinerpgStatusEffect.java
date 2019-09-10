package com.biom4st3r.minerpg.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.biom4st3r.minerpg.MineRPG;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(StatusEffect.class)
public abstract class MinerpgStatusEffect {
    
    public static StatusEffect PAPER_SKIN;

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
    
    public static void init()
    {
        PAPER_SKIN = register("paper_skin", StatusEffectType.HARMFUL, 0xFFFFFF);
        //minerpg:textures/mob_effect/paper_skin.png
        //effect.minerpg.paper_skin
    } 
    
    public static StatusEffect register(Identifier i, StatusEffectType type, int hexColor)
    {
        return Registry.register(Registry.STATUS_EFFECT, i, 
            MinerpgStatusEffect.create(type, hexColor));
    }

    private static StatusEffect register(String s, StatusEffectType type,int hexColor)
    {
        return Registry.register(Registry.STATUS_EFFECT, 
            new Identifier(MineRPG.MODID,s), 
            MinerpgStatusEffect.create(type, hexColor));
    }
    
}