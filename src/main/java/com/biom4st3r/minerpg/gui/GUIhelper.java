package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.RPGComponent;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;

public class GUIhelper
{
    public static void drawString(TextRenderer textRenderer, String string_1, int xPos, int yPos, int color) {
        textRenderer.drawStringBounded(string_1, xPos, yPos, 160,color);
        // textRenderer.drawWithShadow(string_1, (float)(int_1 -
        // textRenderer_1.getStringWidth(string_1) / 2), (float)int_2, int_3);
    }

    public static void drawCenteredString(TextRenderer textRenderer, String string_1, int xCenter, int yPos, int color) {
        textRenderer.drawStringBounded(string_1, (xCenter - textRenderer.getStringWidth(string_1) / 2), yPos, 160,
                color);
        // textRenderer.drawWithShadow(string_1, (float)(int_1 -
        // textRenderer_1.getStringWidth(string_1) / 2), (float)int_2, int_3);
    }

    public static void renderStats(int xMid,int yMid,int[] modY,TextRenderer textRenderer, RPGComponent rpgC)//, Screen screen)
    {
        float scale = 0.91f;
        int modX = 28;
        scale = 0.68f;
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale,scale,scale);
        //drawString("Component Bag", (int)((xMid()+12)/scale), (int)((yMid()-68)/scale)+1, 0x000000);
        drawString(textRenderer, "Stats", (int)((xMid-22)/scale), (int)((yMid-68)/scale)+1, 0x000000);
        //GlStateManager.scalef(1f,1f,1f);
        GlStateManager.popMatrix();
        scale = 0.68f;
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale,scale,scale);

        for(int i = 0; i < 6; i++)
        {
            int xPos = (int)((xMid-modX)/scale);
            int yPos = (int)(modY[i]/scale);
            Stats stat = Stats.values()[i];

            //System.out.println(player == null);
            //System.out.println(player.getStats() == null);
            //System.out.println(player.getStat(stat));
            
            modX = 28;
            xPos = (int)((xMid-modX)/scale);
            drawString(textRenderer, stat.toString().substring(0, 3), xPos, yPos, 0x000000);
            modX = 15;
            xPos = (int)((xMid-modX)/scale);
            drawString(textRenderer, ":", xPos, yPos, 0x000000);
            modX = 1;
            xPos = (int)((xMid-modX)/scale);
            drawCenteredString(textRenderer, "" + rpgC.getStat(stat), xPos, yPos, 0x000000);

            
        }
        if(rpgC.remainingPoints != 0)
        {
            drawString(textRenderer, "Points: ", (int)((xMid-28)/scale), (int)(yMid/scale)-11, 0x000000);
            drawCenteredString(textRenderer, "" + rpgC.remainingPoints, (int)((xMid-1)/scale), (int)(yMid/scale)-11, 0x000000);
        }

        //GlStateManager.scalef(1,1,1);
        GlStateManager.popMatrix();
        //GlStateManager.pushMatrix();
        if(rpgC.remainingPoints != 0)
        {
            Screen.fill(xMid-29, yMid-11, xMid+2, yMid-10, 0xff000000);
        }
    }


}