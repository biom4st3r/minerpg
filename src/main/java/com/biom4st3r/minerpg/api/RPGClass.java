package com.biom4st3r.minerpg.api;


import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.stat.Stat;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.SystemUtil;

public abstract class RPGClass
{

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

    
    public abstract RPGAbility[] abilitysAvalibleAtLevel(int Lvl);

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public List<String> getToolTips()
    {
        return Lists.newArrayList(this.getDisplayName().asFormattedString());
    }

    public Identifier getIcon()
    {
        return new Identifier(id.getNamespace(),"classes/icons/" + id.getPath() + ".png");
    }

    public abstract float provideExpForStatAtLvl(Stat<?> stat, int Lvl);



}