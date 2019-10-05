package com.biom4st3r.minerpg.impl.abilities;

import java.util.List;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;

public class EvokerFangsAOEAbility extends EvokerFangsAbility {

    public EvokerFangsAOEAbility(Identifier id, int coolDownDuration) {
        super(id, coolDownDuration);
    }

    @Override
    public boolean doAbility(RPGPlayer player) {
        if(!player.getRPGAbilityComponent().isOnCooldown(this))
        {
            PlayerEntity pe = player.getPlayer();
            Box area = new Box(pe.getBlockPos().add(-10, -3, -10), pe.getBlockPos().add(10,3,10));
            List<Entity> eList = pe.world.getEntities(pe , area);
            Biow0rks.debug("Evoker AOE Entity List Size: ", eList.size());
            for(Entity e : eList.subList(0, eList.size() >= 7 ? 7 : 0))
            {
                if(e instanceof LivingEntity)
                {
                    this.conjureFangs(pe, e.x, e.z, e.y-2, e.y+2, pe.yaw, 2);
                }
            }
            player.getRPGAbilityComponent().addCooldown(this);
            return true;
        }
        return false;
    }

}