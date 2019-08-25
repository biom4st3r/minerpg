package com.biom4st3r.minerpg.impl.abilities;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.abilities.EmulatePotionAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PotionAbility extends EmulatePotionAbility {

    public PotionAbility(Identifier name,int coolDownDuration, StatusEffect se, int duration, int amplifier) {
        super(name, coolDownDuration, se, duration, amplifier);
        this.coolDownDuration = coolDownDuration;
    }

	@Override
    public void doAbility(RPGPlayer player) {
        if(!player.getRPGAbilityComponent().timeouts.containsKey(id))
        {
            player.getPlayer().addPotionEffect(this.getEffect());
            //player.getRPGAbilityComponent().timeouts.put(name, coolDownDuration);
            player.getRPGAbilityComponent().addCooldown(this);
        }
    }

    @Override
    public Type getType() {
        return Type.USE;
    }

    @Override
	public List<String> getToolTips() {
        List<String> list = new ArrayList<String>(1);
        //list.add(id.getPath().toUpperCase());
        list.add(this.getDisplayName().asFormattedString());
        list.add("Gives you " + new TranslatableText(this.se.getTranslationKey()).asFormattedString() + " " + (amplifier+1) + " for " + duration/20 + " seconds.");
        list.add("Cool Down of " + coolDownDuration/20 + " seconds");
        return list;
	}
    
} 