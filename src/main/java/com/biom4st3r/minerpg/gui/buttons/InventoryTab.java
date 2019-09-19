package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.MineRPG;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class InventoryTab extends ButtonWidget
{
    TextRenderer textRenderer = MinecraftClient.getInstance().getFontManager().getTextRenderer(MinecraftClient.DEFAULT_TEXT_RENDERER_ID);
    private String text;
    private static final Identifier INVENTORY = new Identifier(MineRPG.MODID,"textures/gui/rpgmenu.png");
    private boolean activeTab;
    private int pallet;


    public InventoryTab(int xPos, int yPos, int width, String text,
            PressAction action,boolean enabled,int pallet) {
        super(xPos, yPos, Math.max(13, width), 16, "", action);
        this.text = text;
        this.activeTab = enabled;
        this.pallet = pallet;
    }


    public void setHeight(int height)
    {
        this.height = height;
    }

    int u = 256-14;
    int v = 0;
    int sideWidth = 6;


    @Override
    public void onClick(double double_1, double double_2) 
    {
        if(!activeTab)
        {
            super.onClick(double_1, double_2);
        }
    }

    @Override
    public void renderButton(int int_1, int int_2, float float_1) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        MinecraftClient.getInstance().getTextureManager().bindTexture(INVENTORY);
        v = pallet*(height*2);
        v += activeTab ? height : 0;

        this.blit(this.x,this.y,u,v,sideWidth,height);//left side
        //blit(this.x+6,this.y,u,v,2,16,this.width-12,16); 
        blit(this.x+sideWidth,this.y,width-(sideWidth*2),height,u+sideWidth,v,2,height,256,256);// middle
        this.blit(this.x+(width-sideWidth*2)+sideWidth,this.y,u+sideWidth+2,v,sideWidth,height); // right side

        this.drawCenteredString(textRenderer, this.text, this.x+(width/2)+1, this.y+(height/2)-3, 0x694069);
    }

    public void drawCenteredString(TextRenderer textRenderer_1, String string_1, int xCenter, int y, int color) {
        textRenderer.drawStringBounded(string_1, (xCenter - textRenderer_1.getStringWidth(string_1) / 2), y, 160,
                color);
    }

    
}
/*
 public static void blit(int x, int y, int z, int width, int height, Sprite sprite) {
        innerBlit(x, x + width, y, y + height, z, sprite.getMinU(), sprite.getMaxU(), sprite.getMinV(), sprite.getMaxV());
    }
    
    public void blit(int x, int y, int u, int v, int width, int height) {
        blit(x, y, this.blitOffset, (float)u, (float)v, width, height, 256, 256);
    }
    
    public static void blit(int x, int y, int z, float u, float v, int width, int height, int texHeight, int texWidth) {
        innerBlit(x, x + width, y, y + height, z, width, height, u, v, texWidth, texHeight);
    }
    
    public static void blit(int x, int y, int width, int height, float u, float v, int uWidth, int vHeight, int texWidth, int texHeight) { XXXX
        innerBlit(x, x + width, y, y + height, 0, uWidth, vHeight, u, v, texWidth, texHeight);
    }
    
    public static void blit(int x, int y, float u, float v, int width, int height, int texWidth, int texHeight) {
        blit(x, y, width, height, u, v, width, height, texWidth, texHeight);
    }
    
    private static void innerBlit(int xStart, int xEnd, int yStart, int yEnd, int z, int width, int height, float u, float v, int texWidth, int texHeight) {
        innerBlit(xStart, xEnd, yStart, yEnd, z, (u + 0.0f) / texWidth, (u + width) / texWidth, (v + 0.0f) / texHeight, (v + height) / texHeight);
    }
    */