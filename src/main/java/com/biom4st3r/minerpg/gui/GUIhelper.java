package com.biom4st3r.minerpg.gui;

import java.util.List;

import com.biom4st3r.minerpg.gui.buttons.AbilityButton;
import com.biom4st3r.minerpg.gui.buttons.AbilitySlotButton;
import com.biom4st3r.minerpg.gui.buttons.ArrowButton;
import com.biom4st3r.minerpg.gui.buttons.ClassButton;
import com.biom4st3r.minerpg.gui.buttons.InventoryTab;
import com.biom4st3r.minerpg.gui.screens.ClassMenu;
import com.biom4st3r.minerpg.gui.screens.InitClassMenu;
import com.biom4st3r.minerpg.gui.screens.StatMenu;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public class GUIhelper
{
    public static int drawTabOffset = -88;

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

    public static ButtonWidget[] drawTabs(int xPos, int yPos, MinecraftClient mc,Boolean... bools)//vararg
    {        
        int buttonWidth = 26+5+5;
        ButtonWidget[] buttons = new ButtonWidget[4];

        buttons[0] = new InventoryTab(xPos, yPos, buttonWidth, "Main", button ->
        {
            mc.openScreen(new InventoryScreen(mc.player));
        }, bools[0], 0);
        

        buttons[1] = new InventoryTab(xPos+buttonWidth-1, yPos, buttonWidth, "Bag", (ButtonWidget) ->
        {
            mc.getNetworkHandler().sendPacket(Packets.CLIENT.openComponentBag());

        }, bools[1], 1);
        buttons[2] = new InventoryTab(xPos+(buttonWidth*2)-2, yPos, buttonWidth, "Stats", b ->
        {
            mc.getNetworkHandler().sendPacket(Packets.CLIENT.requestStatComp());
            mc.openScreen(new StatMenu());

        }, bools[2], 2);
        buttons[3] = new InventoryTab(xPos+(buttonWidth*3)-3, yPos, buttonWidth, "Class", b->
        {
            mc.getNetworkHandler().sendPacket(Packets.CLIENT.requestRpgClassComponent());
            if(!((RPGPlayer)mc.player).getRPGClassComponent().hasRpgClass())
            {
                mc.openScreen(new InitClassMenu());
            }
            else
            {
                mc.openScreen(new ClassMenu());
            }
        }, bools[3], 3);
        return buttons;
    }

    public static ButtonWidget[] drawAbilities(int xPos, int yPos)
    {
        ButtonWidget[] bw = new ButtonWidget[12];
        for(int i = 0; i < bw.length; i++)
        {
            if(!(i >= bw.length/2))
            {
                bw[i] = new AbilityButton(xPos+(i*24) , yPos, RpgAbilities.NONE, b->{});
            }
            else
            {
                bw[i] = new AbilityButton(xPos+((i-(bw.length/2))*24) , yPos+24, RpgAbilities.NONE, b->{});
            }
        }
        return bw;
    }

    public static ButtonWidget[] drawAbilityArrows(int xPos,int yPos)
    {
        ButtonWidget[] bw = new ButtonWidget[2];
        bw[0] = new ArrowButton(xPos-11, yPos+24-8, true, b->{});
        bw[1] = new ArrowButton(xPos+(6*24)+1,yPos+24-8,false,b->{});
        return bw;
    }

    public static boolean isPointOverClassButton(ClassButton cb, double mouseX, double mouseY) {
        return isPointWithinBounds(cb.x, cb.y, 21, 21, mouseX, mouseY);
    }
  
    public static boolean isPointWithinBounds(int xPos, int yPos, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(xPos - 1) &&
         mouseX < (double)(xPos + width + 1) &&
          mouseY >= (double)(yPos - 1) &&
           mouseY < (double)(yPos + height + 1);
    }
    public static boolean isPointOverAbilityButton(ButtonWidget ab, double mouseX, double mouseY)
    {
        return isPointWithinBounds(ab.x, ab.y, 16, 16, mouseX, mouseY);
    }

    public static ButtonWidget[] drawAbilitySlots(int xPos,int yPos)
    {
        ButtonWidget[] abilitySlotButtons = new ButtonWidget[9];
        for(int i =0; i < 9; i++)
        {
            abilitySlotButtons[i] = new AbilitySlotButton(xPos + ((i*18)), yPos, i,((RPGPlayer)MinecraftClient.getInstance().player).getRPGAbilityComponent(), b->{});
        }
        return abilitySlotButtons;
    }

    public static void drawLevel(TextRenderer tr, String lvlString, int width, int height)
    {
        tr.draw(lvlString, (float)(width + 1), (float)height, 0);
        tr.draw(lvlString, (float)(width - 1), (float)height, 0);
        tr.draw(lvlString, (float)width, (float)(height + 1), 0);
        tr.draw(lvlString, (float)width, (float)(height - 1), 0);
        tr.draw(lvlString, (float)width, (float)height, 0xDDAA00);

        
    }

	public static void renderXpSideBar(TextRenderer tr, int scaledWidth, int scaledHeight, List<Float> xpEarned,
            List<Integer> visiblity) 
    {
        //GlStateManager.enableAlphaTest();
        GlStateManager.pushMatrix();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        float scale = 1f;
        //float spacer = 10;
        GlStateManager.scalef(scale, scale, scale);
        for(int i = 0; i < xpEarned.size(); i++)
        {
            if(((visiblity.get(i) << 24) & -67108864) == 0) 
            {
                xpEarned.remove(i);
                visiblity.remove(i);
                //spacer--;
                continue;
            }
            // if(spacer < 10)
            // {
            //     if(scale < 1) spacer = 10;
            //     --spacer;
            // }
            //Biow0rks.log(Integer.toHexString(0x00000000^(visiblity.get(i)<<24)));
            tr.drawWithShadow(String.format("+%.2f Exp", xpEarned.get(i)), (scaledWidth-50)*scale, (5+(i*10))*scale, 0x00FFAA00^(visiblity.get(i)<<24));
            //GUIhelper.drawString(tr, String.format("+%.2f", xpEarned.get(i)), (int)((scaledWidth-20)*0.5f), (int)((i*5)*0.5f), 0xFFFFFF);
            
            visiblity.set(i, visiblity.get(i)-1);
        }
        GlStateManager.disableBlend();
        GlStateManager.disableAlphaTest();
        GlStateManager.popMatrix();
        //
	}
}
/*
	METHOD blit (IIFFIIII)V
		ARG 0 x
		ARG 1 y
		ARG 2 u
		ARG 3 v
		ARG 4 width
		ARG 5 height
		ARG 6 texWidth
		ARG 7 texHeight
	METHOD blit (IIIFFIIII)V
		ARG 0 x
		ARG 1 y
		ARG 2 z
		ARG 3 u
		ARG 4 v
		ARG 5 width
		ARG 6 height
		ARG 7 texHeight
		ARG 8 texWidth
	METHOD blit (IIIIFFIIII)V
		ARG 0 x
		ARG 1 y
		ARG 2 width
		ARG 3 height
		ARG 4 u
		ARG 5 v
		ARG 6 uWidth
		ARG 7 vHeight
		ARG 8 texWidth
		ARG 9 texHeight
	METHOD blit (IIIIII)V
		ARG 1 x
		ARG 2 y
		ARG 3 u
		ARG 4 v
		ARG 5 width
		ARG 6 height
	METHOD blit (IIIIILnet/minecraft/class_1058;)V
		ARG 0 x
		ARG 1 y
		ARG 2 z
		ARG 3 width
		ARG 4 height
		ARG 5 sprite

*/