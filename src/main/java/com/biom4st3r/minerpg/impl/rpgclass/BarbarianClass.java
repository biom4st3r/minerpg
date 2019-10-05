
package com.biom4st3r.minerpg.impl.rpgclass;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.interfaces.Reward;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;

public class BarbarianClass extends RPGClass {

    public BarbarianClass(Identifier name) {
        super(name);
    }

    @Override
    public Reward givePlayerRewards(int level)
    {
        switch (level) {
            case 20:
            case 19:
            case 18:
            case 17:
            case 16:
            case 15:
            case 14:
            case 13:
            case 12:
            case 11:
            case 10:
            case 9:
            case 8:
            case 7:
            case 6:
            case 5:
            case 4:
            case 3:
            case 2:
            case 1:
                return ((rpgplayer, list)->
                {
                    //player.getRPGAbilityComponent().give(RpgAbilities.EVOKER_AOE);
                    rpgplayer.getStatsComponent().remainingPoints+=2;
                    list.add(new ItemStack(Items.DIAMOND,1));
                });
            default:
                return ((player,list)->{});
        }


    }

    // @Override
    // public RPGAbility[] abilitysAvalibleAtLevel(int Lvl) {
    //     List<RPGAbility> abilities = new ArrayList<RPGAbility>(2);
    //     switch (Lvl) {
    //         case 20:
    //         case 19:
    //         case 18:
    //         case 17:
    //         case 16:
    //         case 15:
    //         case 14:
    //         case 13:
    //         case 12:
    //         case 11:
    //         case 10:
    //         case 9:
    //         case 8:
    //         case 7:
    //         case 6:
    //         case 5:
    //         case 4:
    //         case 3:
    //         case 2:
    //         case 1:
    //         default:
    //             abilities.add(RpgAbilities.EVOKER_AOE);
    //             abilities.add(RpgAbilities.EVOKER_FANGS);
    //             abilities.add(RpgAbilities.RECKLESS_ATK);
    //             abilities.add(RpgAbilities.FIREBALL_ABILITY);
    //             //abilities.add(RpgAbilities.UNARMORED_DEFENCE);
    //             abilities.add(RpgAbilities.RAGE_ABILITY);
    //             break;
    //     }
    //     return Util.reverse(abilities.toArray(new RPGAbility[0]));
    // }

    @Override
    public List<String> getToolTips() {
        List<String> tips = new ArrayList<>(3);
        tips.add(this.getDisplayName().asFormattedString());
        return tips;
    }

    @Override
    public ExpType getExpType() {
        return ExpType.VANILLA_STAT;
    }

    @Override
    public float getStatWorthAtLevel(Stat<?> stat, int amount, int Lvl) {
        if(stat.getType() == Stats.KILLED)
        {
            
            return 5;
        }
        return 0;
    }
}