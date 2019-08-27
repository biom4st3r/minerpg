package com.biom4st3r.minerpg.impl.abilities;

import java.util.List;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.mob.GhastEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FireballAbility extends RPGAbility {

    public FireballAbility(Identifier name,int coolDownDuration) {
        super(name,coolDownDuration);
    }

	@Override
    public boolean doAbility(RPGPlayer player) 
    {
        if(this.isCooledDown(player))
        {
            World world = player.getPlayer().getEntityWorld();
            PlayerEntity pe = player.getPlayer();
            float xVelo = -MathHelper.sin(pe.yaw * 0.017453292F) * MathHelper.cos(pe.pitch * 0.017453292F);
            float yVelo = -MathHelper.sin((pe.pitch) * 0.017453292F);
            float zVelo = MathHelper.cos(pe.yaw * 0.017453292F) * MathHelper.cos(pe.pitch * 0.017453292F);
            FireballEntity fbe = new FireballEntity(world, player.getPlayer(),1,1,1);
            fbe.setPositionAndAngles(fbe.x, pe.y+pe.getStandingEyeHeight(), fbe.z, pe.headYaw, pe.pitch);
            fbe.setVelocity(xVelo, yVelo, zVelo);
            //fbe.setPositionAndAngles(fbe.posX, fbe.posY, fbe.posZ, pe.yaw, pe.pitch);
            
            //method_19207(fbe, player.getPlayer(), player.getPlayer().yaw, player.getPlayer().pitch, 1,1,1);

            fbe.prevPitch = pe.pitch;
            fbe.prevYaw = pe.yaw;

            //fbe.setPositionAndAngles(fbe.x, fbe.y, fbe.z, player.getPlayer().pitch, player.getPlayer().yaw);
            Util.debug( world.spawnEntity(fbe));

            player.getRPGAbilityComponent().addCooldown(this);
            return true;
        }   
        return false;     
        //BlazeEntity.ShootFireballGoal;
        //GhastEntity.ShootFireballGoal;
        
    }

    // public void method_19207(Entity target, Entity player, float pitch, float yaw, float float_3, float velocityMultiplier, float randMultiplier) {
    //     float xVelo = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
    //     float yVelo = -MathHelper.sin((pitch + float_3) * 0.017453292F);
    //     float zVelo = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
    //     setVelocity(target, (double)xVelo, (double)yVelo, (double)zVelo, velocityMultiplier, randMultiplier);
    //     Vec3d vec3d_1 = player.getVelocity();
    //     target.setVelocity(target.getVelocity().add(vec3d_1.x, player.onGround ? 0.0D : vec3d_1.y, vec3d_1.z));
    // }
    // public void setVelocity(Entity target, double double_1, double double_2, double double_3, float velocityMulitplier, float randMuliplier) {
    //     Vec3d vec3d_1 = (new Vec3d(double_1, double_2, double_3)).normalize()
    //         //.add(target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier,
    //         //     target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier,
    //         //     target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier)
    //                 .multiply((double)velocityMulitplier);
    //     target.setVelocity(vec3d_1);
    //     float float_3 = MathHelper.sqrt(Entity.squaredHorizontalLength(vec3d_1));
    //     target.yaw = (float)(MathHelper.atan2(vec3d_1.x, vec3d_1.z) * 57.2957763671875D);
    //     target.pitch = (float)(MathHelper.atan2(vec3d_1.y, (double)float_3) * 57.2957763671875D);
    //     target.prevYaw = target.yaw;
    //     target.prevPitch = target.pitch;
    // }

    @Override
    public Type getType() {
        return Type.LEFT_CLICK;
    }

    @Override
	public List<String> getToolTips() {
		return Lists.newArrayList(this.getDisplayName().asFormattedString(),"Cool Down: " + coolDownDuration/20);
	}

    @Override
    public void applyCost() {

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        return true;
    }
    
}