package com.biom4st3r.minerpg.impl.abilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.biom4st3r.minerpg.api.abilities.EmulatePotionAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class PotionAbility extends EmulatePotionAbility {

    public PotionAbility(Identifier name,int coolDown, StatusEffect se, int duration, int amplifier) {
        super(name, se, duration, amplifier);
        this.coolDown = coolDown;
        // TODO Auto-generated constructor stub
    }
    int coolDown;

	@Override
    public void doAbility(RPGPlayer player) {
        if(!player.getRPGAbilityComponent().timeouts.containsKey(name))
        {
            player.getPlayer().addPotionEffect(this.getEffect());
            player.getRPGAbilityComponent().timeouts.put(name, coolDown);
        }
    }

    @Override
    public Type getType() {
        return Type.USE;
    }

    @Override
	public List<String> getToolTips() {
        List<String> list = new ArrayList<String>(1);
        list.add(name.getPath());
        list.add("Gives you " + new TranslatableText(this.se.getTranslationKey()).asFormattedString() + " " + amplifier + " for " + duration + "tick.");
		return list;
	}
    
} 