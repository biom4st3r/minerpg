package com.biom4st3r.minerpg.impl.abilities;

import java.util.List;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.interfaces.DestructiveItemRequirement;
import com.biom4st3r.minerpg.interfaces.ItemRequirement;
import com.biom4st3r.minerpg.interfaces.Requirement;
import com.biom4st3r.minerpg.interfaces.StatRequirement;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
            //FireballEntity fbe = new FireballEntity(EntityType.FIREBALL, world);
            Fireball fbe = new Fireball(pe);
            //method_19207(fbe, player.getPlayer(), player.getPlayer().yaw, player.getPlayer().pitch, 0,0,0);

            // fbe.prevPitch = pe.pitch;
            // fbe.prevYaw = pe.yaw;

            //fbe.setPositionAndAngles(fbe.x, fbe.y, fbe.z, player.getPlayer().pitch, player.getPlayer().yaw);
            Biow0rks.debug("did fireball spawn: %s", world.spawnEntity(fbe));

            player.getRPGAbilityComponent().addCooldown(this);
            return true;
        }   
        return false;     
        //BlazeEntity.ShootFireballGoal;
        //GhastEntity.ShootFireballGoal;
        //SnowballItem
    }

    public void method_19207(Entity target, Entity player, float pitch, float yaw, float float_3, float velocityMultiplier, float randMultiplier) {
        float xVelo = -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        float yVelo = -MathHelper.sin((pitch) * 0.017453292F);
        float zVelo = MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
        setVelocity(target, (double)xVelo, (double)yVelo, (double)zVelo, velocityMultiplier, randMultiplier);
        Vec3d vec3d_1 = player.getVelocity();
        target.setVelocity(target.getVelocity().add(vec3d_1.x, player.onGround ? 0.0D : vec3d_1.y, vec3d_1.z));
    }
    public void setVelocity(Entity target, double double_1, double double_2, double double_3, float velocityMulitplier, float randMuliplier) {
        Vec3d vec3d_1 = (new Vec3d(double_1, double_2, double_3)).normalize()
            //.add(target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier,
            //     target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier,
            //     target.random.nextGaussian() * 0.007499999832361937D * (double)randMuliplier)
                    .multiply((double)velocityMulitplier);
        target.setVelocity(vec3d_1);
        float float_3 = MathHelper.sqrt(Entity.squaredHorizontalLength(vec3d_1));
        target.yaw = (float)(MathHelper.atan2(vec3d_1.x, vec3d_1.z) * 57.2957763671875D);
        target.pitch = (float)(MathHelper.atan2(vec3d_1.y, (double)float_3) * 57.2957763671875D);
        target.prevYaw = target.yaw;
        target.prevPitch = target.pitch;
    }

    @Override
    public Type getType() {
        return Type.LEFT_CLICK;
    }

    @Override
	public List<String> getToolTips() {
		return Lists.newArrayList(this.getDisplayName().asFormattedString(),"Cool Down: " + coolDownDuration/20);
	}

    @Override
    public void applyCost(RPGPlayer player) {

    }

    @Override
    public boolean hasCost(RPGPlayer player) {
        StatRequirement sr =  ()->
        {
            return new RPGStatsComponent(0, 0, 10, 12, 0, 0).getStats();
        };
        DestructiveItemRequirement dir = ()->
        {
            return Lists.newArrayList(new ItemStack(Items.GUNPOWDER,2));
        };
        ItemRequirement ir = ()->
        {
            return Lists.newArrayList(new ItemStack(Items.FLINT_AND_STEEL));
        };
        if( sr.meetsRequirements(player, player.getStatsComponent()) &&
            dir.meetsRequirements(player, player.getComponentContainer().bag) &&
            ir.meetsRequirements(player, player.getComponentContainer().bag))
        {
            dir.destory(player.getComponentContainer().bag);
            return true;
        }
        return false;
    }
    
}