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
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class Fireball extends AbstractFireballEntity {
    public int explosionPower = 1;
    public float drag = 1.0f;
    public boolean onFire = false;
    

    public Fireball(EntityType<? extends AbstractFireballEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }
    public Fireball(World world, EntityType<? extends AbstractFireballEntity> entityType)
    {
        this(entityType,world);
    }

    public Fireball(LivingEntity owner)
    {
        super(MineRPG.FIREBALL,owner.world);

        init(owner);

    }

    @Environment(EnvType.CLIENT)
    public Fireball(World world, double x,double y, double z, double xVelo, double yVelo, double zVelo)
    {
        super(MineRPG.FIREBALL,x,y,z,xVelo,yVelo,zVelo,world);
    }

    @Override
    protected ParticleEffect getParticleType() {
        return ParticleTypes.POOF;
    }

    @Override
    public void tick() {
        Vec3d velo =  this.getVelocity();
        super.tick();
        this.setVelocity(velo);
    }

    public void init(LivingEntity e)
    {
        this.owner = e;
        this.setPositionAndAngles(e.x, e.y+e.getStandingEyeHeight(), e.z, e.yaw, e.pitch);
        //this.y += e.getStandingEyeHeight();
        //pi/180
        float radToDegree = 0.0174532925199F;//0.01745329251994329576923690768489‬
        float xVelo = -MathHelper.sin(e.yaw * radToDegree) * MathHelper.cos(e.pitch * radToDegree);
        float yVelo = -MathHelper.sin((e.pitch) * radToDegree);
        float zVelo = MathHelper.cos(e.yaw * radToDegree) * MathHelper.cos(e.pitch * radToDegree);
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