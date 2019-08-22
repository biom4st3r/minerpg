package com.biom4st3r.minerpg.impl.abilities;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class FireballAbility extends RPGAbility {

    public FireballAbility(Identifier name,int coolDownDuration) {
        super(name,coolDownDuration);
    }

	@Override
    public void doAbility(RPGPlayer player) 
    {
        if(this.isCooledDown(player))
        {
            World world = player.getPlayer().getEntityWorld();
            FireballEntity fbe = new FireballEntity(world, player.getPlayer(), 0, 0, 0);
            world.spawnEntity(fbe);

        }        
        //BlazeEntity.ShootFireballGoal;
    }


    @Override
    public Type getType() {
        return Type.LEFT_CLICK;
    }

    @Override
	public List<String> getToolTips() {
		return null;
	}
    
}