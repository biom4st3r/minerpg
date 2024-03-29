package com.biom4st3r.minerpg.registery;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.google.common.base.Supplier;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;

public final class RPG_Registry
{
    public static final Registry<RPGClass> CLASS_REGISTRY = create("rpgclasses", () -> 
    {
        return RpgClasses.NONE;
    });

    public static final Registry<RPGAbility> ABILITY_REGISTRY = create("rpgabilities", ()->
    {
        return RpgAbilities.NONE;
    });

    public static <T> Registry<T> create(String string_1, Supplier<T> supplier_1) 
    {
        return putDefaultEntry(string_1, new SimpleRegistry<T>(), supplier_1);
    }

    private static <T, R extends MutableRegistry<T>> R putDefaultEntry(String string_1, R mutableRegistry_1, Supplier<T> supplier_1) 
    {
        Identifier identifier_1 = new Identifier(string_1);
        //DEFAULT_ENTRIES.put(identifier_1, supplier_1);
        return Registry.REGISTRIES.add(identifier_1, mutableRegistry_1);
    }

}