package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.client.gui.screen.ingame.GrindstoneScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.container.Container;
import net.minecraft.container.GrindstoneContainer;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;

public class ComponentContainer extends Container {

    public final PlayerInventory playerInv;
    public final BasicInventory bag;
    //ShulkerBoxContainer
    //MinecraftClient
    //PlayerEntity
    //PlayerInventory
    public ComponentContainer(int syncid, final PlayerInventory playerInventory_1) {
        this(syncid, playerInventory_1, new BasicInventory(4*3));
    }

    public ComponentContainer(int syncid, PlayerInventory playerInv, BasicInventory components) 
    {
        super(null, syncid);
        this.playerInv = playerInv;
        //GrindstoneContainer
        //GrindstoneScreen
        checkContainerSize(components, 4*3);
        bag = components;
        this.playerInv.onInvOpen(this.playerInv.player);
        int xPos = 98;
        int yPos = 8;

        for(int row = 0; row < 3; row++)
        {
            for(int colum = 0; colum < 4; colum++)
            {
                int index = 4 * row + colum;
                //System.out.println(index);
                //                              inv, index, xPos,           yPos
                this.addSlot(new ComponentSlot(bag, index, xPos+(colum*18), yPos+(row*18)));
            }
        }

        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                //System.out.println(x + " " + y);
                this.addSlot(new Slot(this.playerInv, x + (y + 1) * 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for(int i = 0; i < 9; ++i) {
            //System.out.println(i);
            //ClientPlayerInteractionManager
            //ServerPlayNetworkHandler
            this.addSlot(new Slot(this.playerInv, i, 8 + i * 18, 142));
        }
    }

    // public void updateInv()
    // {
    //     System.out.println("1");
    //     RPGPlayer player = MineRPG.toRPG(this.playerInv.player);
    //     System.out.println("2");
    //     CompoundTag t = new CompoundTag();
    //     System.out.println("3");
    //     this.playerInv.player.readCustomDataFromTag(t);
    //     System.out.println("4");
    //     bag = player.fromTags(t.getList("compInv", 10), 4*3);
    //     System.out.println("hello");
    // }

    @Override
    public boolean canUse(PlayerEntity arg0) {
        return true;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity playerEntity_1, int int_1) {
        ItemStack itemStack_1 = ItemStack.EMPTY;
        Slot slot_1 = (Slot)this.slotList.get(int_1);
        if (slot_1 != null && slot_1.hasStack()) {
           ItemStack itemStack_2 = slot_1.getStack();
           itemStack_1 = itemStack_2.copy();
           if (int_1 < this.bag.getInvSize()) {
              if (!this.insertItem(itemStack_2, this.bag.getInvSize(), this.slotList.size(), true)) {
                 return ItemStack.EMPTY;
              }
           } else if (!this.insertItem(itemStack_2, 0, this.bag.getInvSize(), false)) {
              return ItemStack.EMPTY;
           }
  
           if (itemStack_2.isEmpty()) {
              slot_1.setStack(ItemStack.EMPTY);
           } else {
              slot_1.markDirty();
           }
        }
  
        return itemStack_1;
     }
    
}