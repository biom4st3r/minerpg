package com.biom4st3r.minerpg.gui;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.container.AbstractFurnaceContainer;
import net.minecraft.container.Container;
import net.minecraft.container.ContainerType;
import net.minecraft.container.Generic3x3Container;
import net.minecraft.container.PlayerContainer;
import net.minecraft.container.ShulkerBoxContainer;
import net.minecraft.container.Slot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;

public class ComponentContainer extends Container {

    public final PlayerInventory playerInv;
    public final BasicInventory bag;
    //Generic3x3Container
    //ShulkerBoxContainer
    //ContainerType
    //MinecraftClient
    //PlayerEntity
    //PlayerInventory
    // public ComponentContainer(int syncid, final PlayerInventory playerInventory_1) {
    //     this(syncid, playerInventory_1, new BasicInventory(4*3));
    // }

    public ComponentContainer(int syncid, PlayerInventory playerInv, BasicInventory components) 
    {
        //PlayerContainer
        super(null, syncid);
        this.playerInv = playerInv;
        checkContainerSize(components, 4*3);
        bag = components;
        this.bag.onInvOpen(this.playerInv.player);
        //Entity
        //ClientPlayerEntity
        int xPos = 98;
        int yPos = 8;

        for(int row = 0; row < 3; row++)
        {
            for(int colum = 0; colum < 4; colum++)
            {
                int index = 4 * row + colum;
                //                              inv, index, xPos,           yPos
                this.addSlot(new ComponentSlot(bag, index, xPos+(colum*18), yPos+(row*18)));
            }
        }

        for(int y = 0; y < 3; ++y) 
        {
            for(int x = 0; x < 9; ++x) 
            {
                this.addSlot(new Slot(this.playerInv, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int i = 0; i < 9; ++i) 
        {
            this.addSlot(new Slot(this.playerInv, i, 8 + i * 18, 142));
            
        }
    }

    @Override
    public boolean canUse(PlayerEntity arg0) {
        return true;
    }

    @Override
    public void close(PlayerEntity playerEntity_1) {
        super.close(playerEntity_1);
        this.bag.onInvClose(playerEntity_1);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity pe, int slotIndex) {
        ItemStack temp = ItemStack.EMPTY;
        Slot clickedSlot = (Slot)this.slotList.get(slotIndex);
        int lastIndexCBag = 12-1;
        int lastIndexInv = lastIndexCBag+27;
        int lastIndexHot = lastIndexInv+9;
        if (clickedSlot != null && clickedSlot.hasStack()) 
        {
            ItemStack stackInSlot = clickedSlot.getStack();
            temp = stackInSlot.copy();
            if (slotIndex < lastIndexCBag+1) 
            { // from component Bag
                if (!this.insertItem(stackInSlot, lastIndexCBag+1, this.slotList.size(), true)) 
                {
                    return ItemStack.EMPTY;
                }
            //(ItemStack stack, int startIndex, int endIndex, boolean fromLast)
            } else if (!this.insertItem(stackInSlot, 0, lastIndexCBag+1, false)) 
            { // anywhere else
                return ItemStack.EMPTY;
            }

            if (stackInSlot.isEmpty()) 
            {
                clickedSlot.setStack(ItemStack.EMPTY);
            } else 
            {
                clickedSlot.markDirty();
            }
        }

        return temp;
    }
    
    // @Override
    // protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean inReverse) {
    //     if(startIndex > 11)
    //     {
    //         return super.insertItem(stack, startIndex, endIndex, inReverse);
    //     }
    //     boolean successful = false;
    //     int startingIndex = startIndex;
    //     if (inReverse) {
    //         startingIndex = endIndex - 1;
    //     }

    //     Slot currSlot;
    //     ItemStack currStack;
    //     if (stack.isStackable() || true) {
    //         while(!stack.isEmpty()) {
    //             if (inReverse) {
    //                 if (startingIndex < startIndex) {
    //                     break;
    //                 }
    //             } else if (startingIndex >= endIndex) {
    //                 break;
    //             }

    //             currSlot = (Slot)this.slotList.get(startingIndex);
    //             currStack = currSlot.getStack();
    //             if (!currStack.isEmpty() && canStacksCombine(stack, currStack)) {
    //                 int combineItemCount = currStack.getCount() + stack.getCount();
    //                 if (combineItemCount <= currSlot.getMaxStackAmount()) {
    //                     stack.setCount(0);
    //                     currStack.setCount(combineItemCount);
    //                     currSlot.markDirty();
    //                     successful = true;
    //                 } else if (currStack.getCount() < currSlot.getMaxStackAmount()) {
    //                     stack.decrement(currSlot.getMaxStackAmount() - currStack.getCount());
    //                     currStack.setCount(currSlot.getMaxStackAmount());
    //                     currSlot.markDirty();
    //                     successful = true;
    //                 }
    //             }

    //             if (inReverse) {
    //                 --startingIndex;
    //             } else {
    //                 ++startingIndex;
    //             }
    //         }
    //     }

    //     if (!stack.isEmpty()) {
    //     if (inReverse) {
    //         startingIndex = endIndex - 1;
    //     } else {
    //         startingIndex = startIndex;
    //     }

    //     while(true) {
    //         if (inReverse) {
    //             if (startingIndex < startIndex) {
    //                 break;
    //             }
    //         } else if (startingIndex >= endIndex) {
    //             break;
    //         }

    //         currSlot = (Slot)this.slotList.get(startingIndex);
    //         currStack = currSlot.getStack();
    //         if (currStack.isEmpty() && currSlot.canInsert(stack)) {
    //             if (stack.getCount() > currSlot.getMaxStackAmount()) {
    //                 currSlot.setStack(stack.split(currSlot.getMaxStackAmount()));
    //             } else {
    //                 currSlot.setStack(stack.split(stack.getCount()));
    //             }

    //             currSlot.markDirty();
    //             successful = true;
    //             break;
    //         }

    //         if (inReverse) {
    //             --startingIndex;
    //         } else {
    //             ++startingIndex;
    //         }
    //     }
    //     }

    //     return successful;
    // }
    
}