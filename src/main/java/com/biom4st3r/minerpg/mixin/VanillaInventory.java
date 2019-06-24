package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.gui.InventoryTab;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.container.PlayerContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.chat.Component;

@Mixin(InventoryScreen.class)
public abstract class VanillaInventory extends AbstractInventoryScreen<PlayerContainer> implements RecipeBookProvider {
    public VanillaInventory(PlayerContainer container_1, PlayerInventory playerInventory_1, Component component_1) {
        super(container_1, playerInventory_1, component_1);
    }

    // @Inject(at = @At("RETURN"),method = "<init>*")
    // private void onConst(CallbackInfo ci)
    // {
    //     for(int i = 0; i < 10; i++)
    //         System.out.println("new InventoryuSDcrren");
    // }

    @Inject(at = @At("TAIL"),method = "init",cancellable = false)
    protected void init(CallbackInfo ci)
    {//                                 xpos,  ypos , width, height, 
        int bWidth = 26;
        //int bHeight = 13;

        // this.addButton(new ButtonWidget((this.width/2)-bWidth, (this.height/2)-97, bWidth, bHeight, "Main", (ButtonWidget) ->
        // {
            
        // }));
        // this.addButton(new ButtonWidget((this.width/2), (this.height/2)-97, bWidth, bHeight, "RPG", (ButtonWidget) ->
        // {
        //     //this.minecraft.player.closeContainer();
        //     //this.minecraft.player.closeScreen();
        //     this.minecraft.player.playerContainer.close(this.minecraft.player);
        //     //this.minecraft.openScreen(new RPGMenu(MineRPG.toRPG(this.minecraft.player).getComponentContainer()));
        //     this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());
        //     //ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(MineRPG.MODID,MineRPG.COMPONENT_BAG), this.minecraft.player, (buf) -> {});
        // }));
        this.addButton(new InventoryTab((this.width/2)-(bWidth-1), (this.height/2)-96, bWidth, "Main", button ->
        {
            // this.minecraft.player.playerContainer.close(this.minecraft.player);

            // this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());
        }, true, 1));

        this.addButton(new InventoryTab((this.width/2), (this.height/2)-96, bWidth, "RPG", (ButtonWidget) ->
        {
            this.minecraft.player.playerContainer.close(this.minecraft.player);
            this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());

        },false,1));
    }

    protected <T extends AbstractButtonWidget> T addButton(T abw)
    {
        this.children.add(abw);
        this.buttons.add(abw);
        return abw;
    }
    
}