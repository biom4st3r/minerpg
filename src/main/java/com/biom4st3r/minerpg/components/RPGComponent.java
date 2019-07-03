package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;

public class RPGComponent
{

    private List<RPGClass> rpgclasses;

    private List<RPGAbility> abilities;

    public RPGComponent()
    {
        rpgclasses = new ArrayList<RPGClass>(1);
        abilities = new ArrayList<RPGAbility>(20);
    }

    


}