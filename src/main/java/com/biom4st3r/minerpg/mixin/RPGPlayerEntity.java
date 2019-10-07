package com.biom4st3r.minerpg.mixin;
/*
Purpose
    Hub for all player based mods. May switched to LivingEntity 
    to provide for functionallity and flexiblity in the future.

*/

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.ComponentContainer;
import com.biom4st3r.minerpg.mixin_interfaces.BasicInventoryHelper;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
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

    // @Inject(at = @At(value = "FIELD",target="float_1:F"),method="attack")
    // public void attackk(Entity e,CallbackInfo ci,float baseDamage)
    // {ClientPlayerEntity

    // }

    // @Inject(at = @At("HEAD"),method="net/minecraft/entity/player/PlayerEntity.increaseStat(Lnet/minecraft/stat/Stat;I)V",cancellable = false)
    // public void increaseStat(Stat<?> stat, int int_1, CallbackInfo ci)
    // {
    //     RPGPlayer player = ((RPGPlayer)this);
    //     player.getRPGClassComponent().processStat(stat,i);
    //     Biow0rks.debug("processing Stat " + stat.getName());
    // }

    public void sendMessage(Text... texts)
    {
        String s = "";
        for(Text t : texts)
        {
            s += t.asFormattedString() + " ";
        }
        this.getPlayer().sendMessage(new LiteralText(s));
    }

    @Inject(at = @At("RETURN"), method = "<init>*")
    private void onConst(CallbackInfo ci) {
        if (bag == null) {
            bag = new BasicInventory(componentInvSize);
            //ServerPlayerEntity
        }
        this.componentInventory = new ComponentContainer(2834671, ((PlayerEntity) (Object) this).inventory, bag);
        //TODO: Change Component to map for custom Components
        this.statsComponent = new RPGStatsComponent();
        this.rpgClassComponent = new RPGClassComponent(this);
        this.rpgAbilityComponent = new RPGAbilityComponent(this);
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

    int i = 0;

    @Inject(at = @At("HEAD"),method="tick")
    public void tick(CallbackInfo ci)
    {
        this.rpgAbilityComponent.tick();
    }

    @Inject(at = @At("HEAD"),method = "addExperience")
    public void addExperience(int amount, CallbackInfo ci)
    {
        this.rpgClassComponent.processExperience(amount);
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
        for (int i = 0; i < lt.size(); i++) {
            CompoundTag ct = lt.getCompoundTag(i);
            int slot = ct.getByte(SLOT) & 255;
            if (slot >= 0 && slot < size) {
                ((BasicInventoryHelper) bi)._setInvStack(slot, Util.TagToShortItemStack(ct));// ItemStack.fromTag(ct));
            }
        }
        return bi;
    }

    // @Override
    // public int getArmor()
    // {
    //     if(this.rpgAbilityComponent.getNamedAbilitySlot(RPGAbility.PASSIVE_NAMES.ARMOR_OVERRIDE).isEmpty())
    //         return super.getArmor();
    //     return ((ArmorOverrideAbility)this.rpgAbilityComponent.getNamedAbilitySlot(RPGAbility.PASSIVE_NAMES.ARMOR_OVERRIDE).ability).getArmor(this);
    // }

    @Inject(at = @At("HEAD"), method = "jump")
    public void jump(CallbackInfo ci)
    {
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag", cancellable = false)
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo ci) 
    {
        Biow0rks.debug("read: ");
        this.statsComponent.deserializeNBT(tag);
        bag = deserialize((ListTag)tag.getTag(COMPONENT_BAG), componentInvSize);
        this.rpgClassComponent.deserializeNBT(tag);
        this.rpgAbilityComponent.deserializeNBT(tag);
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag", cancellable = false)
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo ci) 
    {
        Biow0rks.debug("write: ");
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