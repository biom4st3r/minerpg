package com.biom4st3r.minerpg.impl.abilities.barbarian;

import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.api.abilities.ArmorOverrideAbility;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.util.Identifier;

public class UnarmoredDefenceAbility extends ArmorOverrideAbility {

    public UnarmoredDefenceAbility(Identifier id) {
        super(id, 0);
    }

	@Override
    public int getArmor(RPGPlayer player) {
        RPGStatsComponent stats = player.getStatsComponent();
        int dex = stats.getModifier(Stat.Stats.DEXTERITY);
        int con = stats.getModifier(Stat.Stats.CONSTITUTION);
        return 10 + dex + con;
	}
    
}