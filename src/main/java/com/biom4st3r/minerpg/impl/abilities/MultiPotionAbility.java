package com.biom4st3r.minerpg.impl.abilities;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.Identifier;

public class MultiPotionAbility extends RPGAbility {

    private StatusEffect[] effects;
    private int[] durations;
    private int[] amps;
    protected boolean ambient = false;
    protected boolean showParticles = true;
    protected boolean showIcon = true;

    public MultiPotionAbility(Identifier id, int coolDownDuration,StatusEffect[] effects,int[] durations,int[] amps) 
    {
        super(id, coolDownDuration);
        this.effects = effects;
        this.durations = durations;
        this.amps = amps;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void applyCost(RPGPlayer player) 
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean hasCost(RPGPlayer player) {

        return false;
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        if(!player.getRPGAbilityComponent().isOnCooldown(this))
        {
            for(int i = 0; i < effects.length; i++)
            {
                player.getPlayer().addPotionEffect(new StatusEffectInstance(effects[i],durations[i],amps[i],ambient,showParticles,showIcon));
            }
            player.getRPGAbilityComponent().addCooldown(this);
            return true;
        }
        return false;
    }

    @Override
    public Type getType() {
        // TODO Auto-generated method stub
        return Type.USE;
    }
    
    @Override
    public List<String> getToolTips() {
        List<String> tips = super.getToolTips();
        for(int i = 0; i < effects.length; i++)
        {
            tips.add(String.format("%s %s for %s seconds",effects[i].method_5560().asFormattedString(), amps[i]+1,durations[i]/20));
        }
        return tips;
    }
    
}