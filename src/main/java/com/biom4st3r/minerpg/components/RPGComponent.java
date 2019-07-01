package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;
import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.api.RPGClass;

public class RPGComponent
{

    private List<RPGClass> rpgclasses;

    private List<Ability> abilities;

    public RPGComponent()
    {
        rpgclasses = new ArrayList<RPGClass>(1);
        abilities = new ArrayList<Ability>(20);
    }

    


}