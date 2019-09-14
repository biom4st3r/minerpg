package com.biom4st3r.minerpg.mixin;
/*
Purpose
    Hub for all player based mods. May switched to LivingEntity 
    to provide for functionallity and flexiblity in the future.




*/

import java.util.Iterator;
import java.util.List;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.api.abilities.ArmorOverrideAbility;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.networking.Packets.SERVER;
import com.biom4st3r.minerpg.util.BasicInventoryHelper;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.EntityVelocityUpdateS2CPacket;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayerEntity extends LivingEntity implements RPGPlayer { // Entity

    protected RPGPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    @Override
    public PlayerEntity getPlayer() {
        return (PlayerEntity)(Object)this;
    }

    @Inject(at = @At("HEAD"),method = "attack",cancellable = true)
    public void attack(Entity target, CallbackInfo ci)
    {
        if(this.rpgClassComponent.hasRpgClass() )
        {
            if(target.isAttackable())
            {
                if(!target.handleAttack(this))
                {                    
                    float weaponDamage = (float)Util.getAttackDamage(this.getMainHandStack());
                    float baseDamage = (float)this.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).getValue()-weaponDamage;
                    float strDamage = this.statsComponent.getModifier(Stats.STRENGTH);
                    float coolDownNerf = this.getPlayer().getAttackCooldownProgress(0.5f);
                    LivingEntity livingTarget = target instanceof LivingEntity ? ((LivingEntity)target) : null; 
                    boolean isLiving = livingTarget != null;
                    float magicDamage = (float)EnchantmentHelper.getAttackDamage(this.getMainHandStack(), isLiving ? livingTarget.getGroup() : EntityGroup.DEFAULT);
                    int roll = this.random.nextInt(20)+1;
                    float totaldamage = 0;
                    coolDownNerf = 0.2F + coolDownNerf * coolDownNerf * 0.8F;
                    totaldamage = totaldamage + strDamage + baseDamage + magicDamage + (weaponDamage > 0 ? this.random.nextInt((int)weaponDamage) + 1 : 0);
                    totaldamage *= coolDownNerf;
                    if(roll == 20 && weaponDamage > 0)
                    {
                        totaldamage+=this.random.nextInt((int)weaponDamage)+1;
                    }
                    
                    Util.debug(totaldamage);
                    if(totaldamage > 0.0f)
                    {
                        boolean cooledDown = coolDownNerf > 0.9f;
                        boolean fallingCrit = cooledDown && this.fallDistance > 0.0f && !this.onGround && !this.isClimbing() && !this.isInWater() && !this.hasVehicle();
                        boolean hasFireAspect = false;

                        float targetHealth = isLiving ? livingTarget.getHealth() : 0.0f;
                        Vec3d targetVelo = target.getVelocity();

                        int knockBack = EnchantmentHelper.getKnockback(this);
                        if(this.isSprinting() && cooledDown)
                        {
                            ++knockBack;
                        }
                        if(fallingCrit)
                        {
                            totaldamage *= 1.5f;
                        }
                        if(EnchantmentHelper.getFireAspect(this) > 0 & isLiving && !target.isOnFire())
                        {
                            hasFireAspect = true;
                            target.setOnFireFor(1);
                        }
                        Util.debug(String.format("\nweaponDamage: %s\nbaseDamage: %s\nstrDamage: %s\ncoolDownNerf: %s\nmagicDamage: %s\ncoolDown: %s", weaponDamage,baseDamage,strDamage,coolDownNerf,magicDamage,this.getPlayer().getAttackCooldownProgress(0.5f)));
                        if(target.damage(DamageSource.player(this.getPlayer()),totaldamage))
                        {

                            if(knockBack > 0)
                            {
                                if(isLiving)
                                {
                                    livingTarget.takeKnockback(this, (float)knockBack * 0.5F, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
                                }
                                else
                                {
                                    target.addVelocity((double)(-MathHelper.sin(this.yaw * 0.017453292F) * (float)knockBack * 0.5F), 0.1D, (double)(MathHelper.cos(this.yaw * 0.017453292F) * (float)knockBack * 0.5F));
                                }
                                this.setVelocity(this.getVelocity().multiply(0.6D,1.0D,0.6D));
                                this.setSprinting(false);
                            }
                            if(!this.getMainHandStack().isEmpty() && EnchantmentHelper.getSweepingMultiplier(this) > 0)
                            {
                                if((this.horizontalSpeed-this.prevHorizontalSpeed) < this.getMovementSpeed())
                                {
                                    Iterator<LivingEntity> nearby = this.world.getEntities(LivingEntity.class, target.getBoundingBox().expand(1.0D, 0.25D, 1.0D)).iterator();
                                    LivingEntity next;
                                    while(nearby.hasNext())
                                    {
                                        next = nearby.next();
                                        if(next != this && next != target && !this.isTeammate(next) && this.squaredDistanceTo(next) < 9.0D)
                                        {
                                            next.takeKnockback(this, 0.4f, (double)MathHelper.sin(this.yaw * 0.017453292F), (double)(-MathHelper.cos(this.yaw * 0.017453292F)));
                                            next.damage(DamageSource.player(this.getPlayer()), 1.0f + EnchantmentHelper.getSweepingMultiplier(this) * baseDamage);
                                        }
                                        
                                    }
                                    this.getPlayer().method_7263();
                                }
                            }
                            if(target instanceof ServerPlayerEntity && target.velocityModified)
                            {
                                ((RPGPlayer)target).getNetworkHandlerS().sendPacket(new EntityVelocityUpdateS2CPacket(target));
                                target.velocityModified = false;
                                target.setVelocity(targetVelo);
                            }
                            if(fallingCrit)
                            {
                                this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
                                this.getPlayer().addCritParticles(target);
                            }
                            else if(/*!fallingCrit && */EnchantmentHelper.getSweepingMultiplier(this) > 0)
                            {
                                if(cooledDown)
                                    this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
                                else
                                    this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
                            }
                            if(magicDamage> 0.0f)
                            {
                                this.getPlayer().addEnchantedHitParticles(target);
                            }
                            this.onAttacking(target);
                            if(isLiving)
                            {
                                EnchantmentHelper.onUserDamaged(livingTarget,this);
                            }
                            EnchantmentHelper.onTargetDamaged(this, target);
                            if(target instanceof EnderDragonPart)
                            {
                                target = ((EnderDragonPart)target).owner;
                            }
                            if(!getMainHandStack().isEmpty())
                            {
                                this.getMainHandStack().postHit(livingTarget, this.getPlayer());
                            }
                            if(isLiving)
                            {
                                float finalDamage = targetHealth-livingTarget.getHealth();
                                this.getPlayer().increaseStat(net.minecraft.stat.Stats.DAMAGE_DEALT, (int)(finalDamage*10.0f));
                                if(hasFireAspect)
                                {
                                    target.setOnFireFor(EnchantmentHelper.getFireAspect(this)*4);
                                }
                                if(this.world instanceof ServerWorld)
                                {
                                    this.getNetworkHandlerS().sendPacket(SERVER.sendDamageParticle(target.getBlockPos().add(0,target.getStandingEyeHeight(),0), finalDamage));
                                }
                            }
                            this.getPlayer().addExhaustion(0.1f);
                        }
                        else
                        {
                            this.world.playSound((PlayerEntity)null, this.x, this.y, this.z, SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);
                            if (hasFireAspect) {
                               target.extinguish();
                            }
                        }
                    }

                }
            }
            ci.cancel();
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onConst(CallbackInfo ci) {
        if (bag == null) {
            bag = new BasicInventory(componentInvSize);
            //ServerPlayerEntity
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        //TODO: Change Component to map for custom Components
        //TODO: Maybe registery for Components ?
        this.statsComponent = new RPGStatsComponent();
        this.rpgClassComponent = new RPGClassComponent();
        this.rpgAbilityComponent = new RPGAbilityComponent();
    }

    private final String COMPONENT_BAG = "compInv";
    private final String SLOT = "Slot";
    private RPGStatsComponent statsComponent;
    private RPGClassComponent rpgClassComponent;
    private RPGAbilityComponent rpgAbilityComponent;

    @Override
    public RPGStatsComponent getStatsComponent() 
    {
        return statsComponent;
    }

    public void asdf() throws InstantiationException, IllegalAccessException
    {
        statsComponent.getClass().newInstance();
    }

    @Override
    public ClientPlayNetworkHandler getNetworkHandlerC() {
        if(this.world.isClient)
        {
            return MinecraftClient.getInstance().getNetworkHandler();
        }
        return null;
    }
    @Override
    public ServerPlayNetworkHandler getNetworkHandlerS() {
        if(!this.world.isClient)
        {
            return ((ServerPlayerEntity)(Object)this).networkHandler;
        }
        return null;
    }

    @Inject(at = @At("HEAD"),method="tick")
    public void tick(CallbackInfo ci)
    {
        RPGAbilityComponent abC = this.rpgAbilityComponent;
        if(abC.cooldowns.size() != 0)
        {
            for(Identifier i : abC.cooldowns.keySet())
            {
                abC.cooldowns.put(i, abC.cooldowns.get(i).intValue()-1);
                if(abC.cooldowns.get(i) <= 0)
                {
                    abC.cooldowns.remove(i); 
                    break;
                }
            }
        }
    }

    @Override
    public RPGClassComponent getRPGClassComponent() {
        return rpgClassComponent;
    }

    @Override
    public RPGAbilityComponent getRPGAbilityComponent() {
        return rpgAbilityComponent;
    }

    @Override
    public void respawn(PlayerEntity originalPlayerEntity)
    {
        RPGPlayer pe = (RPGPlayer)originalPlayerEntity;
        this.bag = new BasicInventory(componentInvSize);
        BasicInventory originalBag = pe.getComponentContainer().bag;
        ((BasicInventoryHelper) this.bag).copy(originalBag);
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        this.statsComponent.clone(pe.getStatsComponent());
        this.rpgClassComponent.clone(pe.getRPGClassComponent());
        this.rpgAbilityComponent.clone(pe.getRPGAbilityComponent());
    }

    int componentInvSize = 3 * 4;

    public BasicInventory bag;

    public ComponentContainer componentInventory;

    public ListTag serialize(BasicInventory bi) {
        ListTag lt = new ListTag();
        Util.debug("write: ");

        for (int i = 0; i < bi.getInvSize(); i++) {
            ItemStack iS = bi.getInvStack(i);

            if (!iS.isEmpty()) 
            {
                CompoundTag ct = new CompoundTag();
                ct.putByte(SLOT, (byte) i);
                Util.ShortItemStackToTag(iS, ct);
                lt.add(ct);
            }
        }
        return lt;
    }

    public BasicInventory deserialize(ListTag lt, int size) {
        BasicInventory bi = bag;

        Util.debug("read: ");
        for (int i = 0; i < lt.size(); i++) {
            CompoundTag ct = lt.getCompoundTag(i);
            int slot = ct.getByte(SLOT) & 255;
            if (slot >= 0 && slot < size) {
                ((BasicInventoryHelper) bi)._setInvStack(slot, Util.TagToShortItemStack(ct));// ItemStack.fromTag(ct));
            }
        }
        return bi;
    }

    @Override
    public int getArmor()
    {
        //Util.debug(this.rpgAbilityComponent.armorOverride.isEmpty());
        //DamageUtil.getDamageLeft
        //damageAmount, ArmorRating, ArmorToughness
        /*
        no armor: 3f damage
        20 20 armor: 0.78f damage
        diamond armor: 0.69000006f damage

        */
        if(this.rpgAbilityComponent.getNamedAbilitySlot(RPGAbilityComponent.SLOT_ARMOROVERRIDE).isEmpty())
            return super.getArmor();
        return ((ArmorOverrideAbility)this.rpgAbilityComponent.getNamedAbilitySlot(RPGAbilityComponent.SLOT_ARMOROVERRIDE).ability).getArmor(this);
    }

    // @Override
    // protected float applyArmorToDamage(DamageSource damageSource_1, float float_1) {
        
    //     float f = super.applyArmorToDamage(damageSource_1, float_1);
    //     Util.debug("Damage: " + f);
    //     return f;
    // }

    @Inject(at = @At("HEAD"), method = "jump")
    public void jump(CallbackInfo ci)
    {
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag", cancellable = false)
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) 
    {
        this.statsComponent.deserializeNBT(tag);
        bag = deserialize(tag.getList(COMPONENT_BAG, 10), componentInvSize);
        this.rpgClassComponent.deserializeNBT(tag);
        this.rpgAbilityComponent.deserializeNBT(tag);
       // Util.debug(this == null);
        //this.getNetworkHandlerS().sendPacket(Packets.SERVER.sendRPGClassComponent(this));
        //((ServerPlayerEntity)(Object)this).networkHandler.sendPacket(Packets.SERVER.sendRPGClassComponent(this));
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag", cancellable = false)
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) 
    {
        this.statsComponent.serializeNBT(tag);
        tag.put(COMPONENT_BAG, serialize(this.componentInventory.bag));
        this.rpgClassComponent.serializeNBT(tag);
        this.rpgAbilityComponent.serializeNBT(tag);
    }

    @Override
    public ComponentContainer getComponentContainer() {
        return this.componentInventory;
    }

    @Override
    public void setComponentContainer(ComponentContainer cc) {
        this.componentInventory = cc;
    }
}