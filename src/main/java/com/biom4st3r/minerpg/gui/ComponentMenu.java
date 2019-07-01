package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ComponentMenu extends AbstractContainerScreen<ComponentContainer> {
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/rpgmenu.png");
    private float mouseX;
    private float mouseY;

    public ComponentMenu(ComponentContainer cc)
    {
        super(cc,cc.playerInv,new TranslatableText(""));
        //this.modY = new int[] {yMid()-60,yMid()-50,yMid()-40,yMid()-30,yMid()-20,yMid()-10};
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
    public void tick() {

    }


    public void drawString(String string_1, int xPos, int yPos, int color) {
        this.font.drawStringBounded(string_1, xPos, yPos, 160,color);
    }

    public void drawCenteredString(String string_1, int xCenter, int yPos, int color) {
        this.font.drawStringBounded(string_1, (xCenter - this.font.getStringWidth(string_1) / 2), yPos, 160,
                color);
    }

    @Override
    protected void init()
    {
        super.init();
        //int buttonWidth = 26+5;
        
        this.modY = new int[6];
        {
            int ySpacing = 8;
            modY[0] = yMid()-57;
            modY[1] = modY[0]+ySpacing;
            modY[2] = modY[1]+ySpacing;
            modY[3] = modY[2]+ySpacing;
            modY[4] = modY[3]+ySpacing;
            modY[5] = modY[4]+ySpacing;
        }
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,true,false,false))
        {
            this.addButton(button);
        }
    }
    int modY[];
        

    @Override
    public void render(int int1,int int2, float float1)
    {
        {
            int ySpacing = 8;
            modY[0] = yMid()-57;
            modY[1] = modY[0]+ySpacing;
            modY[2] = modY[1]+ySpacing;
            modY[3] = modY[2]+ySpacing;
            modY[4] = modY[3]+ySpacing;
            modY[5] = modY[4]+ySpacing;
        }
        this.renderBackground();
        this.drawBackground(float1, int1, int2);
        this.mouseX = (float)int1;
        this.mouseY = (float)int2;
        super.render(int1, int2, float1);

        //EnchantingScreen
        float scale = 0.91f;

        //int modX = 28;


        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale,scale,scale);
        this.drawString("Component Bag", (int)((xMid()+12)/scale), (int)((yMid()-68)/scale)+1, 0x000000);
        GlStateManager.scalef(1f,1f,1f);
        GlStateManager.popMatrix();
        scale = 0.68f;
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale,scale,scale);

        GlStateManager.scalef(0,0,0);
        GlStateManager.popMatrix();

        this.drawMouseoverTooltip(int1, int2);
        //this.drawString("___________", xMid(), yMid(), 0x000000);
    }

    @Override
    protected void drawBackground(float arg0, int arg1, int arg2) 
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - this.mouseX, (float)(int_4 + 75 - 50) - this.mouseY, this.minecraft.player);
    }
    
}