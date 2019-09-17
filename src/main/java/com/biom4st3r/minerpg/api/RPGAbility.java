package com.biom4st3r.minerpg.api;

import java.util.List;

import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

public abstract class RPGAbility
{
    protected RPGAbility(Identifier id,int coolDownDuration)
    {
        this.id = id;
        this.coolDownDuration = coolDownDuration;
    }

    protected final int coolDownDuration;

    public int getMaxTokens(RPGPlayer player)
    {
        return -1;
    }

    public int getCoolDown()
    {
        return coolDownDuration;
    }

    public abstract void applyCost(RPGPlayer player);

    public abstract boolean hasCost(RPGPlayer player);

    public String getTranslationKey()
    {
        return SystemUtil.createTranslationKey("rpgability", id);
    }

    public Text  getDisplayName()
    {
        return new TranslatableText(this.getTranslationKey());
    }

    public final Identifier id;

    public Identifier getIcon()
    {
        return new Identifier(id.getNamespace(),"abilities/icons/" + id.getPath() + ".png");
    }

    public boolean isCooledDown(RPGPlayer player)
    {
        return !player.getRPGAbilityComponent().cooldowns.containsKey(id);
    }

    public abstract boolean doAbility(RPGPlayer player);

    public abstract Type getType();

    public enum Type{
        PASSIVE,
        RIGHT_CLICK,
        LEFT_CLICK, 
        USE
    }
    public List<String> getToolTips()
    {
        List<String> t = Lists.newArrayList(this.getDisplayName().asFormattedString());
        t.add(this.getType().name());
        return  t;
    }

    @Override
    public String toString() {
        return this.id.toString();
    }

    


}