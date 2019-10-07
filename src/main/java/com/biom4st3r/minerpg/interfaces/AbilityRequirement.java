package com.biom4st3r.minerpg.interfaces;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

@FunctionalInterface
public interface AbilityRequirement extends Requirement<RPGAbilityComponent>
{
    public List<RPGAbility> suppy();

    @Override
    default boolean meetsRequirements(RPGPlayer player,RPGAbilityComponent ac, Object... args) {
        for (RPGAbility ability : this.suppy()) {
            if (!ac.getAbilities().contains(ability)) {
                return false;
            }
        }
        return true;
    }
}