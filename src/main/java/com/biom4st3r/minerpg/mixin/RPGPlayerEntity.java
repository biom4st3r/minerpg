package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.util.BasicInventoryHelper;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayerEntity extends LivingEntity implements RPGPlayer { // Entity

    protected RPGPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
        //this.RPGContainer = new RPGComponent();
        //MinecraftServer
    }
    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onConst(CallbackInfo ci) {
        if (bag == null) {
            bag = new BasicInventory(componentInvSize);
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
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

    @Override
    public ClientPlayNetworkHandler getNetworkHandlerC() {
        if(this.world.isClient)
        {
            return MinecraftClient.getInstance().getNetworkHandler();
        }
        //ClientPlayerEntity
        //AbstractClientPlayerEntity
        //ServerPlayerEntity
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

    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource damageSource_1,CallbackInfo ci)
    {
        
    }

    @Inject(at = @At("HEAD"), method = "jump")
    public void jump(CallbackInfo ci)
    {
        //List<RPGAbility> a = this.rpgAbilityComponent.abilityBar;
        //Util.debug(String.format("%s, %s, %s, %s, %s, %s, %s, %s, %s", a.get(0),a.get(1),a.get(2),a.get(3),a.get(4),a.get(5),a.get(6),a.get(7),a.get(8)));
        
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