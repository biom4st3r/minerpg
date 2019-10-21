package com.biom4st3r.minerpg.api;


import java.util.List;

import com.biom4st3r.minerpg.interfaces.Reward;
import com.google.common.collect.Lists;

import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

public abstract class RPGClass
{
    //ServerPlayerEntity


    /**
     * {@link RPGClass.ExpType#VANILLA_STAT} when a stat is incremented via {@link com.biom4st3r.minerpg.mixin.ServerPlayerStatWatch#increaseStat(Stat, int)} 
     * {@link com.biom4st3r.minerpg.components.RPGClassComponent#processStat(Stat, int)} will be called and notify the RPGClass.
     * 
     * {@link RPGClass.ExpType#VANILLA_EXP} when the player Receives VanillaEXP via {@link com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer#addExp(int)} 
     * {@link com.biom4st3r.minerpg.components.RPGClassComponent#processExperience(int)} will be called and notify the RPGClass.
     * 
     * {@link RPGClass.ExpType#CUSTOM} the develope will handle all Expereince.
     */
    public enum ExpType
    {
        VANILLA_EXP,
        VANILLA_STAT,
        CUSTOM
    };

    /**
     * 
     * @param Lvl the current {@link com.biom4st3r.minerpg.components.RPGClassComponent#getLevel()} +1 from the Player
     * @return how much total expereince  to needed to levelup
     */
    public float getExpRequiredForLvl(int Lvl)
    {
        if(Lvl <= 1)
        {
            return 0;
        }
        if(Lvl < 11)
        {
            return Lvl*20;
        }
        else
        {
            return (float)Math.pow(2, Lvl*0.8)*0.850145f;
        }
    }

    /**
     * This is called immediately when the player receives enough exp to Levelup. 
     * Called at {@link com.biom4st3r.minerpg.components.RPGClassComponent#applyRewards(Reward)} via {@link com.biom4st3r.minerpg.components.RPGClassComponent#updateLvl(int)}
     * @param level - the players new level immediately after levelup
     * @return - a {@link Reward} contains anything that should be given to the player on levelup
     * 
     * 
     */
    public abstract Reward givePlayerRewards(int level);

    /**
     * 
     * @return - {@link RPGClass.ExpType} to determine how gaining expereince should be handled.
     */
    public abstract ExpType getExpType();

    public RPGClass(Identifier id)
    {
        this.id = id;
        //Stats
    }

    public String getTranslationKey()
    {
        return SystemUtil.createTranslationKey("rpgclass", id);
    }

    public Text getDisplayName()
    {
        return new TranslatableText(this.getTranslationKey());
    }
    
    public final Identifier id;

    public int maxLvl;

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * Default is {@link RPGClass#getDisplayName()}
     * @return list of tooltips to be displayed via 
     * {@link com.biom4st3r.minerpg.gui.screens.InitClassMenu#isPointOverClassButton(com.biom4st3r.minerpg.gui.buttons.ClassButton, double, double)}
     * 
     */
    public List<String> getToolTips()
    {
        return Lists.newArrayList(this.getDisplayName().asFormattedString());
    }

    public Identifier getIcon()
    {
        return new Identifier(id.getNamespace(),"classes/icons/" + id.getPath() + ".png");
    }

    /**
     * 
     * @param stat the stat that was incremented. provided by {@link com.biom4st3r.minerpg.components.RPGClassComponent#processStat(Stat, int)} see {@link ExpType#VANILLA_STAT} 
     * @param amount amount the stat was incremented
     * @param lvl the players current {@link com.biom4st3r.minerpg.components.RPGClassComponent#getLevel()}
     * @return should return the amount of experience to be added to the players Class. Handled at {@link com.biom4st3r.minerpg.components.RPGClassComponent#processStat(Stat, int)}
     */
    public float getStatWorthAtLevel(Stat<?> stat, int amount, int lvl)
    {
        return 0;
    }

    /**
     * 
     * @param amount amount of VanillaEXP added to the player
     *      see {@link ExpType#VANILLA_STAT}
     * @param lvl the players current {@link com.biom4st3r.minerpg.components.RPGClassComponent#getLevel()}
     * @return should return the amount of experience to be added to the players Class. Handled at {@link com.biom4st3r.minerpg.components.RPGClassComponent#processExperience(int)}
     */
	public float getExperienceWorthAtLevel(int amount, int lvl) {
		return 0;
	}



}