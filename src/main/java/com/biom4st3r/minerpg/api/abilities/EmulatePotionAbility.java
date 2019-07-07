package com.biom4st3r.minerpg.api.abilities;

import com.biom4st3r.minerpg.api.RPGAbility;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.Identifier;

public abstract class EmulatePotionAbility extends RPGAbility
{
    protected EmulatePotionAbility(Identifier name,StatusEffect effect)
    {
        super(name);
    }
}