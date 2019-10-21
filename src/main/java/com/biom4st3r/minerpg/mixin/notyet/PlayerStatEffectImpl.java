// package com.biom4st3r.minerpg.mixin;

// import java.util.Iterator;

// import com.biom4st3r.minerpg.api.Stat.Stats;
// import com.biom4st3r.minerpg.components.RPGAbilityComponent;
// import com.biom4st3r.minerpg.components.RPGClassComponent;
// import com.biom4st3r.minerpg.components.RPGStatsComponent;
// import com.biom4st3r.minerpg.networking.Packets.SERVER;
// import com.biom4st3r.minerpg.util.RPGPlayer;
// import com.biom4st3r.minerpg.util.Util;

// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
// import org.spongepowered.asm.mixin.Unique;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// import net.minecraft.advancement.criterion.Criterions;
// import net.minecraft.client.network.packet.EntityVelocityUpdateS2CPacket;
// import net.minecraft.enchantment.EnchantmentHelper;
// import net.minecraft.entity.Entity;
// import net.minecraft.entity.EntityGroup;
// import net.minecraft.entity.EntityType;
// import net.minecraft.entity.LivingEntity;
// import net.minecraft.entity.attribute.EntityAttributes;
// import net.minecraft.entity.boss.dragon.EnderDragonPart;
// import net.minecraft.entity.damage.DamageSource;
// import net.minecraft.entity.player.HungerManager;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.item.FoodComponent;
// import net.minecraft.item.ItemStack;
// import net.minecraft.server.network.ServerPlayerEntity;
// import net.minecraft.server.world.ServerWorld;
// import net.minecraft.sound.SoundCategory;
// import net.minecraft.sound.SoundEvents;
// import net.minecraft.stat.Stat;
// import net.minecraft.util.Identifier;
// import net.minecraft.util.math.MathHelper;
// import net.minecraft.util.math.Vec3d;
// import net.minecraft.world.World;

// @Mixin(PlayerEntity.class)
// public abstract class PlayerStatEffectImpl extends LivingEntity
// {
//     protected PlayerStatEffectImpl(EntityType<? extends LivingEntity> entityType_1, World world_1) {
//             super(entityType_1, world_1);
//     }


//     @Shadow protected HungerManager hungerManager;

//     @Shadow public void incrementStat(Identifier identifier_1) {}
  
//     @Shadow public void increaseStat(Identifier identifier_1, int int_1) {}
  
//     @Shadow public void incrementStat(Stat<?> stat_1) {}
  
//     @Shadow public void increaseStat(Stat<?> stat_1, int int_1) {}

//     private RPGStatsComponent statsComponent = ((RPGPlayer)this).getStatsComponent();
//     private RPGClassComponent rpgClassComponent = ((RPGPlayer)this).getRPGClassComponent();
//     private RPGAbilityComponent rpgAbilityComponent = ((RPGPlayer)this).getRPGAbilityComponent();
    
//     @Unique
//     public PlayerEntity getPlayer()
//     {
//         return (PlayerEntity)(Object)this;
//     }

//     @Unique
//     public RPGPlayer getRPGPlayer()
//     {
//         return (RPGPlayer)this;
//     }

//     @Inject(at = @At("HEAD"),method = "addExhaustion")
//     public void addExhaustion(float amount, CallbackInfo ci)
//     {
//         if(this.rpgClassComponent.hasRpgClass())
//         {
//             amount = (amount/this.statsComponent.getStat(Stats.CONSTITUTION))*10;
//             //EnchantmentHelper
//             //EnchantingTableContainer
//         }
//     }

//     @Override
//     protected float getJumpVelocity() {
//         float jv = super.getJumpVelocity();
//         float avg = (this.statsComponent.getModifier(Stats.DEXTERITY) + this.statsComponent.getModifier(Stats.STRENGTH))/2f;
//         return Math.max(jv + (avg*0.1f),jv);
//     }

//     @Inject(at = @At("RETURN"),method = "getMovementSpeed",cancellable = true)
//     public void getMovementSpeed(CallbackInfoReturnable<Float> ci)
//     {
//         float speed = ci.getReturnValue();
//         speed = (speed * this.statsComponent.getStat(Stats.DEXTERITY)) / 10;
//         ci.setReturnValue(speed);
//     }

//     @Inject(at = @At("HEAD"),method="eatFood",cancellable = true)
//     public void eatFood(World world, ItemStack iS, CallbackInfoReturnable<ItemStack> ci)
//     {
//         if(this.rpgClassComponent.hasRpgClass())
//         {
//             FoodComponent food = iS.getItem().getFoodComponent();
//             this.hungerManager.add(food.getHunger(), food.getSaturationModifier()+this.statsComponent.getModifier(Stats.CONSTITUTION));
//             this.incrementStat(net.minecraft.stat.Stats.USED.getOrCreateStat(iS.getItem()));
//             world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, world.random.nextFloat() * 0.1F + 0.9F);
//             if (((Object)this) instanceof ServerPlayerEntity) {
//                 Criterions.CONSUME_ITEM.handle((ServerPlayerEntity)(Object)this, iS);
//             }
       
//             ci.setReturnValue(super.eatFood(world, iS));
//         }
//     }


//     @Inject(at = @At("HEAD"),method = "attack",cancellable = true)
//     public void attack(Entity target, CallbackInfo ci)
//     {
//         if(this.rpgClassComponent.hasRpgClass() )
//         {
//             if(target.isAttackable())
//             {
//                 if(!target.handleAttack(this))
//                 {                    
//                     float weaponDamage = (float)Util.getAttackDamage(this.getMainHandStack());
//                     float baseDamage = !this.getMainHandStack().isEmpty() ? 
//                         (float)this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue()-weaponDamage :
//                         this.random.nextInt(3)+1;
//                     float strDamage = this.statsComponent.getModifier(Stats.STRENGTH);
//                     float coolDownNerf = this.getPlayer().getAttackCooldownProgress(0.5f);
//                     LivingEntity livingTarget = target instanceof LivingEntity ? ((LivingEntity)target) : null; 
//                     boolean isLiving = livingTarget != null;
//                     float magicDamage = (float)EnchantmentHelper.getAttackDamage(this.getMainHandStack(), isLiving ? livingTarget.getGroup() : EntityGroup.DEFAULT);
//                     int roll = this.random.nextInt(20)+1;
//                     float totaldamage = 0;
//                     coolDownNerf = 0.2F + coolDownNerf * coolDownNerf * 0.8F;
//                     totaldamage = totaldamage + strDamage + baseDamage + magicDamage + (weaponDamage > 0 ? this.random.nextInt((int)weaponDamage) + 1 : 0);
//                     totaldamage *= coolDownNerf;
//                     if(roll == 20 && weaponDamage > 0)
//                     {
//                         totaldamage+=this.random.nextInt((int)weaponDamage)+1;
//                     }
                    
//                     Biow0rks.logger.debug(totaldamage);
//                     if(totaldamage == 0.0f)
//                     {
//                         totaldamage = (this.random.nextFloat() * this.statsComponent.getStat(Stats.STRENGTH))/10f;
//                     }
//                     if(totaldamage > 0.0f)
//                     {
//                         boolean cooledDown = coolDownNerf > 0.9f;
//                         boolean fallingCrit = cooledDown && this.fallDistance > 0.0f && !this.onGround && !this.isClimbing() && !this.isInWater() && !this.hasVehicle();
//                         boolean hasFireAspect = false;

//                         float targetHealth = isLiving ? livingTarget.getHealth() : 0.0f;
//                         Vec3d targetVelo = target.getVelocity();

//                         int knockBack = EnchantmentHelper.getKnockback(this);
//                         if(this.isSprinting() && cooledDown)
//                         {
//                             ++knockBack;
//                         }
//                         if(fallingCrit)
//                         {
//                             totaldamage *= 1.5f;
//                         }
//                         if(EnchantmentHelper.getFireAspect(this) > 0 & isLiving && !target.isOnFire())
//                         {
//                             hasFireAspect = true;
//                             target.setOnFireFor(1);
//                         }
//                         Biow0rks.logger.debug(String.format("\nweaponDamage: %s\nbaseDamage: %s\nstrDamage: %s\ncoolDownNerf: %s\nmagicDamage: %s\ncoolDown: %s", weaponDamage,baseDamage,strDamage,coolDownNerf,magicDamage,this.getPlayer().getAttackCooldownProgress(0.5f)));
//                         if(target.damage(DamageSource.player(this.getPlayer()),totaldamage))
//                         {

//                             if(knockBack > 0)
//                             {
//                                 if(isLiving)
//                                 {
//                                     livingTarget.takeKnockback(this, (float)knockBack * 0.5F, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
//                                 }
//                                 else
//                                 {
//                                     target.addVelocity((double)(-MathHelper.sin(this.yaw * 0.017453292F) * (float)knockBack * 0.5F), 0.1D, (double)(MathHelper.cos(this.yaw * 0.017453292F) * (float)knockBack * 0.5F));
//                                 }
//                                 this.setVelocity(this.getVelocity().multiply(0.6D,1.0D,0.6D));
//                                 this.setSprinting(false);
//                             }
//                             if(!this.getMainHandStack().isEmpty() && EnchantmentHelper.getSweepingMultiplier(this) > 0)
//                             {
//                                 if((this.horizontalSpeed-this.prevHorizontalSpeed) < this.getMovementSpeed())
//                                 {
//                                     Iterator<LivingEntity> nearby = this.world.getEntities(LivingEntity.class, target.getBoundingBox().expand(1.0D, 0.25D, 1.0D)).iterator();
//                                     LivingEntity next;
//                                     while(nearby.hasNext())
//                                     {
//                                         next = nearby.next();
//                                         if(next != this && next != target && !this.isTeammate(next) && this.squaredDistanceTo(next) < 9.0D)
//                                         {
//                                             next.takeKnockback(this, 0.4f, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
//                                             next.damage(DamageSource.player(this.getPlayer()), 1.0f + EnchantmentHelper.getSweepingMultiplier(this) * baseDamage);
//                                         }
                                        
//                                     }
//                                     this.getPlayer().method_7263();
//                                 }
//                             }
//                             if(target instanceof ServerPlayerEntity && target.velocityModified)
//                             {
//                                 ((RPGPlayer)target).getNetworkHandlerS().sendPacket(new EntityVelocityUpdateS2CPacket(target));
//                                 target.velocityModified = false;
//                                 target.setVelocity(targetVelo);
//                             }
//                             if(fallingCrit)
//                             {
//                                 this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
//                                 this.getPlayer().addCritParticles(target);
//                             }
//                             else if(/*!fallingCrit && */EnchantmentHelper.getSweepingMultiplier(this) > 0)
//                             {
//                                 if(cooledDown)
//                                     this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
//                                 else
//                                     this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
//                             }
//                             if(magicDamage> 0.0f)
//                             {
//                                 this.getPlayer().addEnchantedHitParticles(target);
//                             }
//                             this.onAttacking(target);
//                             if(isLiving)
//                             {
//                                 EnchantmentHelper.onUserDamaged(livingTarget,this);
//                             }
//                             EnchantmentHelper.onTargetDamaged(this, target);
//                             if(target instanceof EnderDragonPart)
//                             {
//                                 target = ((EnderDragonPart)target).owner;
//                             }
//                             if(!getMainHandStack().isEmpty())
//                             {
//                                 this.getMainHandStack().postHit(livingTarget, this.getPlayer());
//                             }
//                             if(isLiving)
//                             {
//                                 float finalDamage = targetHealth-livingTarget.getHealth();
//                                 this.getPlayer().increaseStat(net.minecraft.stat.Stats.DAMAGE_DEALT, (int)(finalDamage*10.0f));
//                                 if(hasFireAspect)
//                                 {
//                                     target.setOnFireFor(EnchantmentHelper.getFireAspect(this)*4);
//                                 }
//                                 if(this.world instanceof ServerWorld)
//                                 {
//                                     //TODO TEST AND MAKE SURE THIS IS VALID
//                                     this.getRPGPlayer().getNetworkHandlerS().sendPacket(SERVER.sendDamageParticle(target.getBlockPos().add(0,target.getStandingEyeHeight(),0), finalDamage));
//                                 }
//                             }
//                             this.getPlayer().addExhaustion(0.1f);
//                         }
//                         else
//                         {
//                             this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
//                             if (hasFireAspect) {
//                                target.extinguish();
//                             }
//                         }
//                     }

//                 }
//             }
//             ci.cancel();
//         }
//     }

// }