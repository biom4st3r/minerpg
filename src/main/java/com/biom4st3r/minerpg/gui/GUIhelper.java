package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
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

    public static ButtonWidget[] drawTabs(int xPos, int yPos, MinecraftClient mc,Boolean... bools)
    {        
        int buttonWidth = 26+5;
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
            mc.getNetworkHandler().sendPacket(Packets.CLIENT.requestRpgComponent());
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
}