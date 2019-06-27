package com.biom4st3r.minerpg.mixin;

import java.util.ArrayList;
import java.util.HashMap;

import com.biom4st3r.minerpg.api.Ability;
import com.biom4st3r.minerpg.api.Class;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.util.BasicInventoryHelper;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.google.common.collect.Maps;

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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class RPGPlayerEntity extends LivingEntity implements RPGPlayer { // Entity

    protected RPGPlayerEntity(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    private final String COMPONENT_BAG = "compInv";
    private final String SLOT = "Slot";
    private final String STATS = "rpgstats";
    private final String SPAREPOINTS = "points";



    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onConst(CallbackInfo ci) {
        if (bag == null) {
            bag = new BasicInventory(componentInvSize);
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        stats = Maps.newHashMap();


    }

    @Override
    public void respawn(PlayerEntity spe)
    {
        //MinecraftClient.getInstance().getNetworkHandler()
        //ServerPlayerInteractionManager
        //ClientPlayNetworkHandler
        RPGPlayer pe = (RPGPlayer)spe;

        this.bag = new BasicInventory(componentInvSize);
        
        
        BasicInventory originalBag = pe.getComponentContainer().bag;
        //ClickWindowC2SPacket
        
        if(this.bag == null)
        {
            this.bag = new BasicInventory(componentInvSize);
        }
        for(int i = 0; i < originalBag.getInvSize(); i++)
        {
            ((BasicInventoryHelper)this.bag)._setInvStack(i, originalBag.getInvStack(i).copy());
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        for(Stat.Stats s : Stat.Stats.values())
        {
            this.stats.put(s, pe.getStats().get(s));
        }
        this.freeStatPoints = pe.getStatPoints();
    }

    @Override
    public void updateStats(PlayerEntity pe)
    {
        RPGPlayer rpgpe = (RPGPlayer)pe;
        for(Stat.Stats s : Stat.Stats.values())
        {
            this.stats.put(s, rpgpe.getStats().get(s));
        }
        this.freeStatPoints = rpgpe.getStatPoints();
    }

    public int getStatPoints()
    {
        return this.freeStatPoints;
    }

    HashMap<Stats,Integer> stats;

    int componentInvSize = 3 * 4;

    public BasicInventory bag;

    public ComponentContainer componentInventory;

    protected ArrayList<Ability> abilities;

    protected ArrayList<Class> classes;

    public int freeStatPoints;

    @Override
    public int getStat(Stat.Stats name) {
        
        return this.stats.get(name) != null ? this.stats.get(name) : -1;
    }

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
        if(this.world.isClient)
        {
            System.out.println(stats.size());
            //this.bag.add(new ItemStack(Items.IRON_AXE,1));
        }
        if(!this.world.isClient)
        {
            System.out.println(stats.size());
            //this.bag.add(new ItemStack(Items.IRON_AXE,1));
        }
        //System.out.println(this.bag.getInvStack(0).getItem().getName().getFormattedText());
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag", cancellable = false)
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) 
    {
        System.out.println(this.world.isClient);
        if(tag.containsKey(STATS))
        {
            CompoundTag statsTag = tag.getCompound(STATS);
            for(Stats i : Stat.Stats.values())
            {
                this.stats.put(i, (int)statsTag.getByte(i.text));
            }
            this.freeStatPoints = tag.getByte(SPAREPOINTS);
        }
        System.out.println(this.stats.get(Stats.DEXTERITY));
        bag = deserialize(tag.getList(COMPONENT_BAG, 10), componentInvSize);
        //((ServerPlayerEntity)(PlayerEntity)(Object)this).networkHandler.sendPacket(updateStats());
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag", cancellable = false)
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) 
    {
        CompoundTag statTag = new CompoundTag();
        if(!tag.containsKey(STATS))
        {
            for(Stats i : Stat.Stats.values())
            {   
                statTag.putByte(i.text,(byte)8);
            }
            tag.putByte(SPAREPOINTS, (byte)27);
        }
        else
        {
            for(Stat.Stats s : Stats.values())
            {
                statTag.putByte(s.text, (byte)(int)this.stats.get(s));
            }
        }
        tag.put(STATS, statTag);
        tag.putByte(SPAREPOINTS, (byte)freeStatPoints);
        tag.put(COMPONENT_BAG, serialize(this.componentInventory.bag));
    }

    @Override
    public ComponentContainer getComponentContainer() {
        return this.componentInventory;
    }

    @Override
    public HashMap<Stat.Stats,Integer> getStats() {
        return this.stats;
    }

    @Override
    public void setComponentContainer(ComponentContainer cc) {
        this.componentInventory = cc;
    }




}