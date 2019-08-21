package com.biom4st3r.minerpg.api.abilities;

import com.biom4st3r.minerpg.api.RPGAbility;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

public abstract class EmulatePotionAbility extends RPGAbility
{
    protected StatusEffect se;
    protected int duration;
    protected int amplifier;
    protected boolean ambient = false;
    protected boolean showParticles = true;
    protected boolean showIcon = true;
    protected EmulatePotionAbility(Identifier name,StatusEffect se, int duration, int amplifier)
    {
        super(name);
        this.se = se;
        this.duration = duration;
        this.amplifier = amplifier;
    }
    protected EmulatePotionAbility(Identifier name,StatusEffect se, int duration, int amplifier,
        boolean ambient,boolean showParticles, boolean showIcon)
    {
        this(name,se,duration,amplifier);
        this.ambient = ambient;
        this.showParticles = showParticles;
        this.showIcon = showIcon;
    }

    public StatusEffectInstance getEffect()
    {
        return new StatusEffectInstance(se,duration,amplifier,ambient,showParticles,showIcon);
    }
    
    /*
    this.type = statusEffect_1;
    this.duration = int_1;
    this.amplifier = int_2;
    this.ambient = boolean_1;
    this.showParticles = boolean_2;
    this.showIcon = boolean_3;
    */

    
    
    




}