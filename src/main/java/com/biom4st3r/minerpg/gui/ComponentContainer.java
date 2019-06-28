package com.biom4st3r.minerpg.gui;

import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.container.SlotActionType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.ItemStack;

public class ComponentContainer extends Container {

    public final PlayerInventory playerInv;
    public final BasicInventory bag;
    public ComponentContainer(int syncid, PlayerInventory playerInv, BasicInventory components) 
    {
        //PlayerContainer
        //CottonScreenController
        super(null, syncid);
        //this.addListener(((ServerPlayerEntity)playerInv.player));
        this.playerInv = playerInv;
        checkContainerSize(components, 4*3);
        bag = components;
        this.bag.onInvOpen(this.playerInv.player);
        int xPos = 98;
        int yPos = 26;

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
        System.out.println("transferSlot");
        ItemStack temp = ItemStack.EMPTY;
        Slot clickedSlot = (Slot)this.slotList.get(slotIndex);
        int lastIndexCBag = 12-1;
        //int lastIndexInv = lastIndexCBag+27;
        //int lastIndexHot = lastIndexInv+9;
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
            } 
            else if (!this.insertItem(stackInSlot, 0, lastIndexCBag+1, false)) 
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
    
    @Override
    protected boolean insertItem(ItemStack sourceStack, int startIndex, int endIndex, boolean inReverse) {
        System.out.println(String.format("insertItem | %s %s", sourceStack.getCount(), sourceStack.getItem().getName().getFormattedText()));

        boolean successful = false;
        int startingIndex = startIndex;
        if (inReverse) {
            startingIndex = endIndex - 1;
        }
        
        Slot currSlot = (Slot)this.slotList.get(startingIndex);
        ItemStack currStack;
        if (sourceStack.isStackable() || currSlot instanceof ComponentSlot) 
        {
            while(!sourceStack.isEmpty()) {
                if (inReverse) {
                    if (startingIndex < startIndex) {
                        break;
                    }
                } else if (startingIndex >= endIndex) {
                    break;
                }

                currSlot = (Slot)this.slotList.get(startingIndex);
                currStack = currSlot.getStack();
                if (!currStack.isEmpty() && canStacksCombine(sourceStack, currStack)) {
                    int combineItemCount = currStack.getCount() + sourceStack.getCount();
                    if (combineItemCount <= currSlot.getMaxStackAmount()) {
                        sourceStack.setCount(0);
                        currStack.setCount(combineItemCount);
                        currSlot.markDirty();
                        successful = true;
                    } 
                    else if (currStack.getCount() < currSlot.getMaxStackAmount()) 
                    {
                        sourceStack.decrement(currSlot.getMaxStackAmount() - currStack.getCount());
                        currStack.setCount(currSlot.getMaxStackAmount());
                        currSlot.markDirty();
                        successful = true;
                    }
                }

                if (inReverse) {
                    --startingIndex;
                } else {
                    ++startingIndex;
                }
            }
        }

        if (!sourceStack.isEmpty()) 
        {
            if (inReverse) {
                startingIndex = endIndex - 1;
            } else {
                startingIndex = startIndex;
            }

            while(true) 
            {
                if (inReverse) {
                    if (startingIndex < startIndex) {
                        break;
                    }
                } else if (startingIndex >= endIndex) {
                    break;
                }

                currSlot = (Slot)this.slotList.get(startingIndex);
                currStack = currSlot.getStack();
                if (currStack.isEmpty() && currSlot.canInsert(sourceStack)) 
                {
                    if(sourceStack.isStackable() || currSlot instanceof ComponentSlot)
                    {
                        if (sourceStack.getCount() > currSlot.getMaxStackAmount()) 
                        {
                            currSlot.setStack(sourceStack.split(currSlot.getMaxStackAmount()));
                        } else 
                        {
                            currSlot.setStack(sourceStack.split(sourceStack.getCount()));
                        }
                    }
                    else
                    {
                        currSlot.setStack(sourceStack.split(1));
                    }

                    currSlot.markDirty();
                    successful = true;
                    break;
                }

                if (inReverse) {
                    --startingIndex;
                } else {
                    ++startingIndex;
                }
            }
        }

        return successful;
    }
    
    private int quickCraftStage = -1;
    private int quickCraftButton;
    private final Set<Slot> quickCraftSlots = Sets.newHashSet();

    @Override
    protected void endQuickCraft() {
        this.quickCraftButton = 0;
        this.quickCraftSlots.clear();
    }

    @Override
    public ItemStack onSlotClick(int slotIndex, int packedBtnId, SlotActionType slotAction, PlayerEntity pe) {
        System.out.println(String.format("onSlotClick | slot: %s | action: %s", slotIndex,slotAction.name()));
        //new Exception().printStackTrace();
        //ClientPlayerInteractionManager
        ItemStack tempStack = ItemStack.EMPTY;
        PlayerInventory playerInv = pe.inventory;
        ItemStack currStack;
        ItemStack cursorStack;
        int int_6;
        int int_4;
        if (slotAction == SlotActionType.QUICK_CRAFT)
        {
            int int_3 = this.quickCraftButton;
            this.quickCraftButton = unpackButtonId(packedBtnId);
            if ((int_3 != 1 || this.quickCraftButton != 2) && int_3 != this.quickCraftButton) {
                this.endQuickCraft();
            } else if (playerInv.getCursorStack().isEmpty()) {
                this.endQuickCraft();
            } else if (this.quickCraftButton == 0) {
                this.quickCraftStage = unpackQuickCraftStage(packedBtnId);
                if (shouldQuickCraftContinue(this.quickCraftStage, pe)) {
                    this.quickCraftButton = 1;
                    this.quickCraftSlots.clear();
                } else {
                    this.endQuickCraft();
                }
            } else if (this.quickCraftButton == 1) {
                Slot slot_1 = (Slot)this.slotList.get(slotIndex);
                cursorStack = playerInv.getCursorStack();
                if (slot_1 != null && canInsertItemIntoSlot(slot_1, cursorStack, true) && slot_1.canInsert(cursorStack) && (this.quickCraftStage == 2 || cursorStack.getCount() > this.quickCraftSlots.size()) && this.canInsertIntoSlot(slot_1)) {
                    this.quickCraftSlots.add(slot_1);
                }
            } else if (this.quickCraftButton == 2) {
                if (!this.quickCraftSlots.isEmpty()) {
                    currStack = playerInv.getCursorStack().copy();
                    int_4 = playerInv.getCursorStack().getCount();
                    Iterator<Slot> var23 = this.quickCraftSlots.iterator();

                    label342:
                    while(true) {

                        Slot slot_2;
                        ItemStack itemStack_4;
                        do {
                        do {
                            do {
                                do {
                                    if (!var23.hasNext()) {
                                    currStack.setCount(int_4);
                                    playerInv.setCursorStack(currStack);
                                    break label342;
                                    }

                                    slot_2 = (Slot)var23.next();
                                    itemStack_4 = playerInv.getCursorStack();
                                } while(slot_2 == null);
                            } while(!canInsertItemIntoSlot(slot_2, itemStack_4, true));
                        } while(!slot_2.canInsert(itemStack_4));
                        } while(this.quickCraftStage != 2 && itemStack_4.getCount() < this.quickCraftSlots.size());

                        if (this.canInsertIntoSlot(slot_2)) {
                        ItemStack itemStack_5 = currStack.copy();
                        int int_5 = slot_2.hasStack() ? slot_2.getStack().getCount() : 0;
                        calculateStackSize(this.quickCraftSlots, this.quickCraftStage, itemStack_5, int_5);
                        int_6 = Math.min(itemStack_5.getMaxCount(), slot_2.getMaxStackAmount(itemStack_5));
                        if (itemStack_5.getCount() > int_6) {
                            itemStack_5.setCount(int_6);
                        }

                        int_4 -= itemStack_5.getCount() - int_5;
                        slot_2.setStack(itemStack_5);
                        }
                    }
                }

                this.endQuickCraft();
            } else {
                this.endQuickCraft();
            }
        } 
        else if (this.quickCraftButton != 0) {
        this.endQuickCraft();
        } 
        else {
        Slot currSlot;
        int cursorStackCount;
        
        if (slotAction != SlotActionType.PICKUP && slotAction != SlotActionType.QUICK_MOVE || packedBtnId != 0 && packedBtnId != 1) 
        {
            //SWAP or CLONE
            if (slotAction == SlotActionType.SWAP && packedBtnId >= 0 && packedBtnId < 9) 
            { // TODO using buttons to swap items allows 999 items in inventory
                currSlot = (Slot)this.slotList.get(slotIndex);
                currStack = playerInv.getInvStack(packedBtnId);
                cursorStack = currSlot.getStack();
                if (!currStack.isEmpty() || !cursorStack.isEmpty()) 
                {
                    System.out.println("stack not empty or cursor not empty");
                    if (currStack.isEmpty()) 
                    {
                        if (currSlot.canTakeItems(pe)) 
                        {
                            playerInv.setInvStack(packedBtnId, cursorStack);
                            //slot_4.onTake(itemStack_8.getCount());
                            currSlot.setStack(ItemStack.EMPTY);
                            currSlot.onTakeItem(pe, cursorStack);
                        }
                    } else if (cursorStack.isEmpty()) 
                    {
                        if (currSlot.canInsert(currStack)) {
                            cursorStackCount = currSlot.getMaxStackAmount(currStack);
                            if (currStack.getCount() > cursorStackCount) {
                                currSlot.setStack(currStack.split(cursorStackCount));
                            } else {
                                currSlot.setStack(currStack);
                                playerInv.setInvStack(packedBtnId, ItemStack.EMPTY);
                            }
                        }
                    } else if (currSlot.canTakeItems(pe) && currSlot.canInsert(currStack)) 
                    {
                        cursorStackCount = currSlot.getMaxStackAmount(currStack);
                        if (currStack.getCount() > cursorStackCount) {
                            currSlot.setStack(currStack.split(cursorStackCount));
                            currSlot.onTakeItem(pe, cursorStack);
                            if (!playerInv.insertStack(cursorStack)) {
                                pe.dropItem(cursorStack, true);
                            }
                        } else {
                            currSlot.setStack(currStack);
                            playerInv.setInvStack(packedBtnId, cursorStack);
                            currSlot.onTakeItem(pe, cursorStack);
                        }
                    }
                }
            } else if (slotAction == SlotActionType.CLONE && pe.abilities.creativeMode && playerInv.getCursorStack().isEmpty() && slotIndex >= 0) {
                currSlot = (Slot)this.slotList.get(slotIndex);
                if (currSlot != null && currSlot.hasStack()) {
                    currStack = currSlot.getStack().copy();
                    currStack.setCount(currStack.getMaxCount());
                    playerInv.setCursorStack(currStack);
                }
            } else if (slotAction == SlotActionType.THROW && playerInv.getCursorStack().isEmpty() && slotIndex >= 0) {
                currSlot = (Slot)this.slotList.get(slotIndex);
                if (currSlot != null && currSlot.hasStack() && currSlot.canTakeItems(pe) && !(currSlot instanceof ComponentSlot)) {
                    currStack = currSlot.takeStack(packedBtnId == 0 ? 1 : currSlot.getStack().getCount());
                    currSlot.onTakeItem(pe, currStack);
                    pe.dropItem(currStack, true);
                }
            } else if (slotAction == SlotActionType.PICKUP_ALL && slotIndex >= 0) {
                currSlot = (Slot)this.slotList.get(slotIndex);
                currStack = playerInv.getCursorStack();
                if (!currStack.isEmpty() && (currSlot == null || !currSlot.hasStack() || !currSlot.canTakeItems(pe))) {
                    int_4 = packedBtnId == 0 ? 0 : this.slotList.size() - 1;
                    cursorStackCount = packedBtnId == 0 ? 1 : -1;

                    for(int int_15 = 0; int_15 < 2; ++int_15) {
                        for(int int_16 = int_4; int_16 >= 0 && int_16 < this.slotList.size() && currStack.getCount() < currStack.getMaxCount(); int_16 += cursorStackCount) {
                            Slot slot_9 = (Slot)this.slotList.get(int_16);
                            if (slot_9.hasStack() && canInsertItemIntoSlot(slot_9, currStack, true) && slot_9.canTakeItems(pe) && this.canInsertIntoSlot(currStack, slot_9)) {
                                ItemStack itemStack_14 = slot_9.getStack();
                                if (int_15 != 0 || itemStack_14.getCount() != itemStack_14.getMaxCount()) {
                                    int_6 = Math.min(currStack.getMaxCount() - currStack.getCount(), itemStack_14.getCount());
                                    ItemStack itemStack_15 = slot_9.takeStack(int_6);
                                    currStack.increment(int_6);
                                    if (itemStack_15.isEmpty()) {
                                    slot_9.setStack(ItemStack.EMPTY);
                                    }

                                    slot_9.onTakeItem(pe, itemStack_15);
                                }
                            }
                        }
                    }
                }

                this.sendContentUpdates();
            }
        } 
        else if (slotIndex == -999) 
        {
            //OutSide Gui
            if (!playerInv.getCursorStack().isEmpty()) {
                if (packedBtnId == 0) {
                    pe.dropItem(playerInv.getCursorStack(), true);
                    playerInv.setCursorStack(ItemStack.EMPTY);
                }

                if (packedBtnId == 1) {
                    pe.dropItem(playerInv.getCursorStack().split(1), true);
                }
            }

        } 
        else if (slotAction == SlotActionType.QUICK_MOVE) 
        {
            //Quick Move
            if (slotIndex < 0) {
                return ItemStack.EMPTY;
            }

            currSlot = (Slot)this.slotList.get(slotIndex);
            if (currSlot == null || !currSlot.canTakeItems(pe)) {
                return ItemStack.EMPTY;
            }

            for(
                currStack = this.transferSlot(pe, slotIndex); 
                !currStack.isEmpty() && 
                ItemStack.areItemsEqualIgnoreDamage(currSlot.getStack(), currStack);
                 currStack = this.transferSlot(pe, slotIndex)) 
            {
                tempStack = currStack.copy();
            }

        } 
        else /*if slotAction == SlotActionType.PICK_UP*/ {
            
            if (slotIndex < 0) {
                //Invalid Index
                return ItemStack.EMPTY;
            }

            currSlot = (Slot)this.slotList.get(slotIndex);
            if (currSlot != null) {
                currStack = currSlot.getStack();
                cursorStack = playerInv.getCursorStack();
                if (!currStack.isEmpty()) {
                    tempStack = currStack.copy();
                }

                if (currStack.isEmpty()) 
                {
                    // Slot is Empty
                    //System.out.println("currStack.isEmpty");
                    
                    if (!cursorStack.isEmpty() && currSlot.canInsert(cursorStack)) {
                        //Item in hand and valid slot for item
                        cursorStackCount = packedBtnId == 0 ? cursorStack.getCount() : 1;
                        if (cursorStackCount > currSlot.getMaxStackAmount(cursorStack) || !cursorStack.isStackable()) {
                            // Items in hand more than slot can hold
                            System.out.println("Items in hand more than slot can hold");
                            cursorStackCount = 
                            cursorStack.isStackable() || currSlot instanceof ComponentSlot ? 
                                Math.min(currSlot.getMaxStackAmount(cursorStack), cursorStack.getCount()): 
                                1;
                        }
                        int quant = Math.min(cursorStackCount, currSlot.getMaxStackAmount());
                        ItemStack newSlotStack = new ItemStack(cursorStack.getItem(),quant);
                        currSlot.setStack(newSlotStack);
                        cursorStack.decrement(quant);
                    }
                } 
                else if (currSlot.canTakeItems(pe)) 
                {
                    //System.out.println("currSlot.canTakeItems");
                    if (cursorStack.isEmpty()) {
                        //curser is empty
                        if (currStack.isEmpty()) {
                            // current Stack is empty
                            currSlot.setStack(ItemStack.EMPTY);
                            playerInv.setCursorStack(ItemStack.EMPTY);
                        } else {
                            // current Stack not empty
                            cursorStackCount = packedBtnId == 0 ? currStack.getCount() : (currStack.getCount() + 1) / 2;
                            playerInv.setCursorStack(currSlot.takeStack(cursorStackCount));
                            if (currStack.isEmpty()) {
                                currSlot.setStack(ItemStack.EMPTY);
                            }

                            currSlot.onTakeItem(pe, playerInv.getCursorStack());
                        }
                    } 
                    else if (currSlot.canInsert(cursorStack)) 
                    {
                        //System.out.println("currSlot.canInsert");
                        if (canStacksCombine(currStack, cursorStack)) {
                            cursorStackCount = packedBtnId == 0 ? cursorStack.getCount() : 1;
                            if (cursorStackCount > currSlot.getMaxStackAmount(cursorStack) - currStack.getCount()) {
                                cursorStackCount = currSlot.getMaxStackAmount(cursorStack) - currStack.getCount();
                            }

                            if (cursorStackCount > cursorStack.getMaxCount() - currStack.getCount() && slotIndex > 11) 
                            {
                                cursorStackCount = cursorStack.getMaxCount() - currStack.getCount();
                            }

                            cursorStack.decrement(cursorStackCount);
                            currStack.increment(cursorStackCount);
                        } else if (cursorStack.getCount() <= currSlot.getMaxStackAmount(cursorStack)) {
                            currSlot.setStack(cursorStack);
                            playerInv.setCursorStack(currStack);
                        }
                    } 
                    else if (cursorStack.getMaxCount() > 1 && canStacksCombine(currStack, cursorStack) && !currStack.isEmpty()) 
                    {
                        System.out.println("cursorStack.getMaxCount() > 1 && canStacksCombine(currStack, cursorStack) && !currStack.isEmpty()");
                        cursorStackCount = currStack.getCount();
                        if (cursorStackCount + cursorStack.getCount() <= cursorStack.getMaxCount()) {
                            cursorStack.increment(cursorStackCount);
                            currStack = currSlot.takeStack(cursorStackCount);
                            if (currStack.isEmpty()) {
                                currSlot.setStack(ItemStack.EMPTY);
                            }

                            currSlot.onTakeItem(pe, playerInv.getCursorStack());
                        }
                    }
                }

                currSlot.markDirty();
            }
        }
        }

        return tempStack;
    }
    
}