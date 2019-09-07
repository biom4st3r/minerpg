package com.biom4st3r.minerpg.api.abilities;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.RpgClassContext;

import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class ArmorOverrideAbility extends RPGAbility {

    protected ArmorOverrideAbility(Identifier id, int coolDownDuration) {
        super(id, coolDownDuration);
    }

    @Override
    public void applyCost(RPGPlayer player) {

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        return false;
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        RPGClassComponent rcc = player.getRPGClassComponent();
        RpgClassContext rcx = rcc.getRpgClassContext(rcc.getRpgClass(0)); 
        player.getRPGAbilityComponent().setNamedAbilitySlot("armoroverride", rcx.getAbilityContext(this)); 
        player.getPlayer().sendMessage(new TranslatableText( this.getDisplayName().asFormattedString() + " applied as armor"));
        return true;
    }

    @Override
    public Type getType() {
        return Type.PASSIVE;
    }
    
    public abstract int getArmor(RPGPlayer player);

    @Override
    public List<String> getToolTips() {
        List<String> t = super.getToolTips();
        t.add("Armor Override");
        return t;
    }
    
}