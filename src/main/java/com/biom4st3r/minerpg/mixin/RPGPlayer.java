package com.biom4st3r.minerpg.mixin;

import java.util.ArrayList;

import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.api.Class;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayer extends LivingEntity {

    protected RPGPlayer(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    int strength;
    int dexterity;
    int intelligence;
    int wisdow;
    int constitution;
    int charisma;

    protected ArrayList<Ability> abilities;

    protected ArrayList<Class> classes;

    public int getStat(Identifier name)
    {
        return -1;
    }

    public ArrayList<Ability> getAbilities()
    {
        return abilities;
    }




}