package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.gui.InventoryTab;
import com.biom4st3r.minerpg.gui.StatMenu;
import com.biom4st3r.minerpg.util.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;

@Mixin(InventoryScreen.class)
public abstract class VanillaInventory extends AbstractInventoryScreen<PlayerContainer> implements RecipeBookProvider {
    public VanillaInventory(PlayerContainer container_1, PlayerInventory playerInventory_1, TranslatableText component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    // @Inject(at = @At("RETURN"),method = "<init>*")
    // private void onConst(CallbackInfo ci)
    // {
    //     for(int i = 0; i < 10; i++)
    //         System.out.println("new InventoryuSDcrren");
    // }
    
    private int xMid()
    {
        return this.width/2;
    }
    private int yMid()
    {
        return this.height/2;
    }

    @Inject(at = @At("TAIL"),method = "init",cancellable = false)
    protected void init(CallbackInfo ci)
    {//                                 xpos,  ypos , width, height, 
        int bWidth = 26+5;
        //int bHeight = 13;
        int yPos = this.yMid()-96;
        this.addButton(new InventoryTab(this.xMid()-(bWidth-1), yPos, bWidth, "Main", button ->
        {
            // this.minecraft.player.playerContainer.close(this.minecraft.player);

            // this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());
        }, true, 0));

        this.addButton(new InventoryTab(this.xMid(), yPos, bWidth, "Bag", (ButtonWidget) ->
        {
            PlayerEntity pe = this.minecraft.player;
            pe.playerContainer.close(this.minecraft.player);
            // pe.dropItem(pe.inventory.getCursorStack(), true);
            // pe.inventory.setCursorStack(ItemStack.EMPTY);
            this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());
            //pe.inventory.setCursorStack(pe.inventory.getCursorStack());

        },false,1));
        this.addButton(new InventoryTab(this.xMid()+(bWidth-1), yPos, bWidth, "Stats", b ->{
            this.minecraft.openScreen(new StatMenu());
        }, false, 2));
    }

    protected <T extends AbstractButtonWidget> T addButton(T abw)
    {
        this.children.add(abw);
        this.buttons.add(abw);
        return abw;
    }
    
}