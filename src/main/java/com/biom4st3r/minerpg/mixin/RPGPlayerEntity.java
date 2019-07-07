package com.biom4st3r.minerpg.mixin;

import java.util.Enumeration;

import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.RPGComponent;
import com.biom4st3r.minerpg.components.StatsComponent;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.util.BasicInventoryHelper;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayerEntity extends LivingEntity implements RPGPlayer { // Entity

    protected RPGPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
        //this.RPGContainer = new RPGComponent();
    }

    private final String COMPONENT_BAG = "compInv";
    private final String SLOT = "Slot";

    private StatsComponent statsComponent;
    private RPGComponent rpgComponent;

    @Override
    public StatsComponent getStatsComponent() 
    {
        return statsComponent;
    }

    @Override
    public RPGComponent getRPGComponent() {
        return rpgComponent;
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onConst(CallbackInfo ci) {
        if (bag == null) {
            bag = new BasicInventory(componentInvSize);
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        this.statsComponent = new StatsComponent();
        this.rpgComponent = new RPGComponent();
    }

    @Override
    public void respawn(PlayerEntity spe)
    {
        RPGPlayer pe = (RPGPlayer)spe;

        this.bag = new BasicInventory(componentInvSize);
        
        
        BasicInventory originalBag = pe.getComponentContainer().bag;
        
        if(this.bag == null)
        {
            this.bag = new BasicInventory(componentInvSize);
        }
        for(int i = 0; i < originalBag.getInvSize(); i++)
        {
            ((BasicInventoryHelper)this.bag)._setInvStack(i, originalBag.getInvStack(i).copy());
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);

        this.statsComponent.updatStats(spe);
    }

    int componentInvSize = 3 * 4;

    public BasicInventory bag;

    public ComponentContainer componentInventory;

    public ListTag serialize(BasicInventory bi) {
        ListTag lt = new ListTag();
        System.out.println("write: ");
        //new Exception().printStackTrace();
        for (int i = 0; i < bi.getInvSize(); i++) {
            ItemStack iS = bi.getInvStack(i);
            //System.out.println(iS.getItem().getName().getFormattedText());
            if (!iS.isEmpty()) 
            {
                CompoundTag ct = new CompoundTag();
                ct.putByte(SLOT, (byte) i);
                Util.ShortItemStackToTag(iS, ct);// iS.toTag(ct);
                lt.add(ct);
            }
        }
        return lt;
    }

    public BasicInventory deserialize(ListTag lt, int size) {
        BasicInventory bi = bag;

        System.out.println("read: ");
        for (int i = 0; i < lt.size(); i++) {
            CompoundTag ct = lt.getCompoundTag(i);
            int slot = ct.getByte(SLOT) & 255;
            //System.out.println(ItemStack.fromTag(ct).getItem().getName().getFormattedText());
            if (slot >= 0 && slot < size) {
                ((BasicInventoryHelper) bi)._setInvStack(slot, Util.TagToShortItemStack(ct));// ItemStack.fromTag(ct));
            }
        }
        return bi;
    }

    // @Inject(at = @At("HEAD"), method = "onDeath")
    // public void onDeath(DamageSource damageSource_1,CallbackInfo ci)
    // {
        
    //     System.out.println(this.bag.getInvStack(0).getItem().getName().getFormattedText());
    // }

    @Inject(at = @At("HEAD"), method = "jump")
    public void jump(CallbackInfo ci)
    {
        Enumeration<RPGClass> rpgclasses = this.rpgComponent.rpgClasses.keys();
        while(rpgclasses.hasMoreElements())
        {
            System.out.println(rpgclasses.nextElement().name.getPath());
        }
        
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag", cancellable = false)
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) 
    {
        this.statsComponent.deserialize(tag);
        bag = deserialize(tag.getList(COMPONENT_BAG, 10), componentInvSize);
        rpgComponent = RPGComponent.readNbt(tag);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag", cancellable = false)
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) 
    {
        this.statsComponent.serialize(tag);
        tag.put(COMPONENT_BAG, serialize(this.componentInventory.bag));
        this.rpgComponent.writeNbt(tag);
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