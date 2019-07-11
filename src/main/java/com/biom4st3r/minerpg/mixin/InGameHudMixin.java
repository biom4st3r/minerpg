package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.util.InGameHudHelper;
import com.biom4st3r.minerpg.util.Util;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper implements InGameHudHelper 
{
    private Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/widgets.png");
    
    private boolean abilityBar = false;
    
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    private PlayerEntity getCameraPlayer() {
        return null;
    }
    @Shadow
    private int scaledWidth;
    @Shadow
    private int scaledHeight;

    @Override
    public void toggleRenderAbilityBar() {
        abilityBar = !abilityBar;
    }

    // @Inject(at = @At("INVOKE"),method="render",cancellable = false)
    // public void render(CallbackInfo ci)
    // {
        
    // }

    @Inject(at = @At("HEAD"),method="renderHotbar",cancellable = true)
    protected void renderHotbarr(float float_1,CallbackInfo ci)
    {
        PlayerEntity player = this.getCameraPlayer();
        if(abilityBar)
        {
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(BG_Texture);
            ItemStack offHandStack = player.getOffHandStack();
            Arm nonMainHand = player.getMainArm().getOpposite();
            int width = this.scaledWidth / 2;

            this.blit(width - 91, this.scaledHeight - 22, 0, 0, 182, 22);
            this.blit(width - 91 - 1 + player.inventory.selectedSlot * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
            if (!offHandStack.isEmpty()) {
                if (nonMainHand == Arm.LEFT) {
                    this.blit(width - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                } else {
                    this.blit(width + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                }
            }

            //this.blitOffset = int_2;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GuiLighting.enableForItems();

            int i;
            int int_9;
            int int_10;
            for(i = 0; i < 9; ++i) 
            {
                int_9 = width - 90 + i * 20 + 2;
                int_10 = this.scaledHeight - 16 - 3;
                //this.renderHotbarItem(int_9, int_10, float_1, playerEntity_1, (ItemStack)playerEntity_1.inventory.main.get(i));
            }

            if (!offHandStack.isEmpty()) 
            {
                i = this.scaledHeight - 16 - 3;
                if (nonMainHand == Arm.LEFT) {
                    //this.renderHotbarItem(width - 91 - 26, i, float_1, playerEntity_1, offHandStack);
                } else {
                    //this.renderHotbarItem(width + 91 + 10, i, float_1, playerEntity_1, offHandStack);
                }
            }
            GuiLighting.disable();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            ci.cancel();

        }
    }

    // @Inject(at = @At("HEAD"), method="renderHotBar",cancellable = true)
    // protected void renderHotbar(float float_1, CallbackInfo ci)
    // {
    //     
    // }
}