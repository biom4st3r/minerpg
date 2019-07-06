package com.biom4st3r.minerpg.impl.rpgclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.impl.abilities.barbarian.RageAbility;
import com.google.common.collect.Maps;

import net.minecraft.util.Identifier;

public class BarbarianClass extends RPGClass {

    // protected HashMap<Integer,Ability[]> abilities = new HashMap<Integer,Ability[]>(){{
    //     put(1,new Ability[]{});
    //     put(2,new Ability[]{})
    // }};
    
    public BarbarianClass(Identifier name) {
        super(name);
    }

    @Override
    public RPGAbility[] abilitysAvalibleAtLevel(int Lvl) {
        List<RPGAbility> abilities = new ArrayList<RPGAbility>(20);
        switch(Lvl)
        {
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
            abilities.add(null);
            default:
            break;
        }
        return (RPGAbility[])abilities.toArray();
    }

    @Override
    public List<String> getToolTips() {
        List<String> tips = new ArrayList<>(3);
        tips.add(this.name.getPath().toUpperCase());
        return tips;
    }
}