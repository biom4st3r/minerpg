package com.biom4st3r.minerpg.mixin;

import java.util.ArrayList;
import java.util.List;

import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.api.Class;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.util.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayerEntity extends LivingEntity implements RPGPlayer {

    protected RPGPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
        //PlayerContainer
    }


    @Inject(at = @At("RETURN"),method = "<init>*")
    private void onConst(CallbackInfo ci)
    {
        bag = new BasicInventory(componentInvSize);
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory,
        bag);
        
        //this.componentInventory.updateInv();
    }

    int componentInvSize = 3 * 4;
    
    //ShulkerBoxBlockEntity

    // public BasicInventory componentInventory = new
    // BasicInventory(componentInvSize);
    private BasicInventory bag;
    public ComponentContainer componentInventory;

    int strength;
    int dexterity;
    int intelligence;
    int wisdow;
    int constitution;
    int charisma;

    protected ArrayList<Ability> abilities;

    protected ArrayList<Class> classes;

    public int getStat(Identifier name) {
        return -1;
    }

    public ListTag toTags(Inventory bi) {
        ListTag lt = new ListTag();
        // ShulkerBoxContainer
        for (int i = 0; i < bi.getInvSize(); i++) {
            ItemStack iS = bi.getInvStack(i);
            if (!iS.isEmpty()) {
                CompoundTag ct = new CompoundTag();
                ct.putByte("Slot", (byte) i);
                iS.toTag(ct);
                lt.add(ct);
            }
        }
        return lt;
    }

    public BasicInventory fromTags(ListTag lt, int size) {
        BasicInventory bi = new BasicInventory(size);


        for (int i = 0; i < size; i++) {
            bi.setInvStack(i, ItemStack.EMPTY);
        }
        for (int i = 0; i < lt.size(); i++) {
            CompoundTag ct = lt.getCompoundTag(i);
            int slot = ct.getByte("Slot") & 255;
            if (slot >= 0 && slot < size) {
                bi.setInvStack(slot, ItemStack.fromTag(ct));
            }
        }
        return bi;
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag", cancellable = false)
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) {
        bag = fromTags(tag.getList("compInv", 10), componentInvSize);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag", cancellable = false)
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) {
        tag.put("compInv", toTags(bag));
    }

    @Override
    public ComponentContainer getComponentContainer() {
        return this.componentInventory;
    }

    @Override
    public List<Stat> getStats() {
        return null;
    }

    @Override
    public void setComponentContainer(ComponentContainer cc) {
        this.componentInventory = cc;
    }




}