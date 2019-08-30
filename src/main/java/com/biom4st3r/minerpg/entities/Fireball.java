package com.biom4st3r.minerpg.entities;

import com.biom4st3r.minerpg.MineRPG;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class Fireball extends AbstractFireballEntity {
    public int explosionPower = 1;
    public float drag = 1.0f;
    public boolean onFire = false;

    public Fireball(EntityType<? extends AbstractFireballEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
		// TODO Auto-generated constructor stub
    }
    public Fireball(World world, EntityType<? extends AbstractFireballEntity> entityType)
    {
        super(entityType,world);
    }
    @Environment(EnvType.CLIENT)
    public Fireball(World world, double x,double y, double z, double xVelo, double yVelo, double zVelo)
    {
        //this(MineRPG.FIREBALL,world);
        super(MineRPG.FIREBALL,x,y,z,xVelo,yVelo,zVelo,world);
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void init(LivingEntity e)
    {
        this.owner = e;
        this.copyPositionAndRotation(e);
        this.y += e.getStandingEyeHeight();
        float xVelo = -MathHelper.sin(e.yaw * 0.017453292F) * MathHelper.cos(e.pitch * 0.017453292F);
        float yVelo = -MathHelper.sin((e.pitch) * 0.017453292F);
        float zVelo = MathHelper.cos(e.yaw * 0.017453292F) * MathHelper.cos(e.pitch * 0.017453292F);
        this.setItem(new ItemStack(Items.FIRE_CHARGE,1));
        this.setVelocity(xVelo, yVelo, zVelo);
    }

    @Override 
    protected void onCollision(HitResult hitResult_1) {
        if (!this.world.isClient) {
            if (hitResult_1.getType() == HitResult.Type.ENTITY && ((EntityHitResult)hitResult_1).getEntity()!=this.owner) {
               Entity entity_1 = ((EntityHitResult)hitResult_1).getEntity();
               entity_1.damage(DamageSource.explosiveProjectile(this, this.owner), 6.0F);
               this.dealDamage(this.owner, entity_1);
            }
   
            boolean boolean_1 = this.world.getGameRules().getBoolean(GameRules.MOB_GRIEFING);
            this.world.createExplosion((Entity)null, this.x, this.y, this.z, (float)this.explosionPower, boolean_1, boolean_1 ? DestructionType.DESTROY : DestructionType.NONE);
            this.remove();
         }   
    }
    
    @Override
    protected float getDrag() {
        return drag;
    }
    @Override
    public boolean isOnFire() {
        return onFire;
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compoundTag_1) {
        super.writeCustomDataToTag(compoundTag_1);
        compoundTag_1.putInt("ExplosionPower", this.explosionPower);
     }
     @Override
     public void readCustomDataFromTag(CompoundTag compoundTag_1) {
        super.readCustomDataFromTag(compoundTag_1);
        if (compoundTag_1.containsKey("ExplosionPower", 99)) {
           this.explosionPower = compoundTag_1.getInt("ExplosionPower");
        }
  
     }
    
}