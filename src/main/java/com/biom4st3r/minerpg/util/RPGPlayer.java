package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.StatsComponent;
import com.biom4st3r.minerpg.gui.ComponentContainer;

import net.minecraft.entity.player.PlayerEntity;

public interface RPGPlayer
{

    public void respawn(PlayerEntity spe);

    public void setComponentContainer(ComponentContainer cc);

    public ComponentContainer getComponentContainer();

    public RPGClassComponent getRPGClassComponent();

    public RPGAbilityComponent getRPGAbilityComponent();

    public StatsComponent getStatsComponent();

    
}