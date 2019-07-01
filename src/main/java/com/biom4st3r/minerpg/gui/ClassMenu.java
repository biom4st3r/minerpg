package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class ClassMenu extends Screen
{
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    protected int containerWidth = 176;
    protected int containerHeight = 166;
    protected int left;
    protected int top;

    protected ClassMenu() 
    {
		super(new TranslatableText(""));
    }

    private int xMid()
    {
        return this.width/2;
    }
    private int yMid()
    {
        return this.height/2;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() 
    {
        super.init();
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,false,false,true))
        {
            this.addButton(button);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float float_1) 
    {
        this.renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        super.render(mouseX, mouseY, float_1);
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - mouseX, (float)(int_4 + 75 - 50) - mouseY, this.minecraft.player);
    }
}