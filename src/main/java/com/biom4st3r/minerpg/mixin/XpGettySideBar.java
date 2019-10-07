package com.biom4st3r.minerpg.mixin;

/*
    Purpose: Visible display to the player when gaining xp
    i hope this doesn't look to much like "Item get" because
    i purposfully didn't look at their source.



*/

import java.util.List;

import com.biom4st3r.biow0rks.Mxn;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.mixin_interfaces.HudExpDisplayer;
import com.google.common.collect.Lists;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public abstract class XpGettySideBar implements HudExpDisplayer {
    List<Float> xpEarned = Lists.newArrayList();
    List<Integer> visiblity = Lists.newArrayList();

    TextRenderer tr = MinecraftClient.getInstance().textRenderer;

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;

    @Inject(at = @At(Mxn.At.BeforeReturn), method = "render")
    private void renderXpGetty(float float_1,CallbackInfo ci) {
        if (xpEarned == null) {
            xpEarned = Lists.newArrayList();
            visiblity = Lists.newArrayList();
        }
        //GlStateManager.pushMatrix();
        //GlStateManager.scalef(0.5f, 0.5f, 0.5f);
        //tr.draw("Bitch I'm Here", scaledWidth/2, scaledHeight/2, 0xFFFFFF);
        GUIhelper.renderXpSideBar(tr,scaledWidth,scaledHeight,xpEarned,visiblity);
        //GlStateManager.popMatrix();
    }

    @Override
    public void displayExp(float value) {
        this.xpEarned.add(value);
        this.visiblity.add(0xFF);

    }

}