package com.biom4st3r.minerpg.mixin;

import java.util.List;

import com.biom4st3r.minerpg.util.BasicInventoryHelper;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.texture.TextureStitcher.Slot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.InventoryListener;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.PacketByteBuf;

@Mixin(BasicInventory.class)
public abstract class BasicInventoryMaxSize implements BasicInventoryHelper {

    @Shadow
    @Final
    private DefaultedList<ItemStack> stackList;

    @Shadow
    private List<InventoryListener> listeners;

    @Shadow
    public void markDirty() {

    }

    @Override
    public void _setInvStack(int index, ItemStack iS) {
        this.stackList.set(index, iS);

        this.markDirty();
    }

    @Override
    public void copy(BasicInventory original) {
        for (int i = 0; i < original.getInvSize(); i++) {
            this._setInvStack(i, original.getInvStack(i).copy());
        }
    }

    // public static final String SLOT = "slot";
    // @Override
    // public void serializeBuffer(PacketByteBuf pbb) {
    //     ListTag lt = new ListTag();
    //     CompoundTag ct;
    //     for(int i = 0; i < stackList.size(); i++)
    //     {
    //         if(!stackList.get(i).isEmpty())
    //         {
    //             ct = new CompoundTag();
    //             ct.putByte(SLOT, (byte)i);
    //             Util.ShortItemStackToTag(stackList.get(i), ct);
    //             lt.add(ct);
    //         }
    //     }
    //     //PlayerInventory
    // }

    // @Override
    // public void deserializeBuffer(PacketByteBuf pbb) {

    // }

}