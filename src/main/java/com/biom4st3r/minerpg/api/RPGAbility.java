package com.biom4st3r.minerpg.api;

import java.util.List;

import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.google.common.collect.Lists;

import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

public abstract class RPGAbility
{
    public final Identifier id;


    /**
     * called when {@link RPGAbility} is removed from {@link RPGAbilityComponent#cooldowns}
     * @param rpgplayer
     * 
     */
    public void onCooledDown(RPGPlayer rpgplayer) {}

    protected RPGAbility(Identifier id,int coolDownDuration)
    {
        this.id = id;
        this.coolDownDuration = coolDownDuration;
    }


    /**
     * length of time in ticks that this RPGAbility should remain in {@link RPGAbilityComponent#cooldowns}
     */
    protected final int coolDownDuration;


    public int getCoolDownDuration()
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

    public Identifier getIcon()
    {
        return new Identifier(id.getNamespace(),"abilities/icons/" + id.getPath() + ".png");
    }


    /**
     * 
     * @param player
     * @return Checks if this RPGAbility in currently in {@link RPGAbilityComponent#cooldowns} via  {@link RPGAbilityComponent#isOnCooldown(Identifier)}
     */
    public boolean isCooledDown(RPGPlayer player)
    {
        return !player.getRPGAbilityComponent().isOnCooldown(id);
    }


    /**
     * This is run whenever a player attempts to use an ability from {@link com.biom4st3r.minerpg.mixin.MinecraftClientMixin#handleInputAttack()} or 
     * {@link com.biom4st3r.minerpg.mixin.MinecraftClientMixin#handleInputUse()}
     * which is then send to the Server via {@link com.biom4st3r.minerpg.networking.Packets.CLIENT#useAbility()}
     * @param player
     * @return Should return false in the {@link RPGAbility#isCooledDown(RPGPlayer)} is false. otherwise returns true and does the ability
     * 
     */
    public abstract boolean doAbility(RPGPlayer player);

    /**
     * 
     * @return returns a {@link RPGAbility.Type} based on how it should be activiated
     */
    public abstract Type getType();

    public enum Type{
        RIGHT_CLICK, 
        LEFT_CLICK, 
        USE
    }

    /**
     * 
     * @return list of string when hovered over in an {@link com.biom4st3r.minerpg.gui.screens.AbstractAbilitiesContainer}
     */
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

    public boolean isNone()
    {
        return this == RpgAbilities.NONE;
    }

    


}