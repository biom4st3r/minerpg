package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.gui.ComponentContainer;

import net.minecraft.entity.player.PlayerEntity;

public interface RPGPlayer
{
    public RPGComponent getRPGComponent();

    public void respawn(PlayerEntity spe);

    public void setComponentContainer(ComponentContainer cc);

    public ComponentContainer getComponentContainer();
    
}