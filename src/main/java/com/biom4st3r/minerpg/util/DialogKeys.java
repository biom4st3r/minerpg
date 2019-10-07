package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.api.RPGAbility;

import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

public final class DialogKeys
{
    private static final String prefix = "dialog.minerpg.";
    public static final String
        Learned_Ability = prefix+"learnedability",
        Given_Item      = prefix+"givenitem",
        Given_SkillPoint= prefix+"givenskillpoint"
    
    ;
    public static TranslatableText[] createTranslated_LearnedAbility(RPGAbility ability)
    {
        return makeArray(
            new TranslatableText(DialogKeys.Learned_Ability),
            new TranslatableText(ability.getTranslationKey())
        );
    }
    public static TranslatableText[] createTranslated_GivenItem(ItemStack iS)
    {
        return makeArray(
          new TranslatableText(DialogKeys.Given_Item),
          new TranslatableText(iS.getTranslationKey())
        );
    }

    public static TranslatableText[] makeArray(TranslatableText... texts)
    {
        return texts;
    }

    
}