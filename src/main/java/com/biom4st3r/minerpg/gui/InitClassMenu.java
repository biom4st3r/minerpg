package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InitClassMenu extends Screen
{
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    protected int containerWidth = 176;
    protected int containerHeight = 166;
    protected int left;
    protected int top;
    private ButtonWidget confirm;
    private ClassButton[] classButtons;
    private int focusedButton = -1;
    private ArrowButton[] arrowButtons;
    int pageIndex = 0;
    Registry<RPGClass> classReg = RPG_Registry.CLASS_REGISTRY;
    RPGPlayer player;

    public InitClassMenu()
    {
        super(new TranslatableText(""));
        player = (RPGPlayer)MinecraftClient.getInstance().player;
    }

    private int xMid()
    {
        return this.width/2;
    }
    private int yMid()
    { 
        return this.height/2;
    }

    public int yGrid(int i)
    {
        int ySpacing = 9;
        return (yMid()-76) + (i * ySpacing);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public static boolean isPointOverClassButton(ClassButton cb, double mouseX, double mouseY) {
        return InitClassMenu. isPointWithinBounds(cb.x, cb.y, 22, 22, mouseX, mouseY);
    }
  
    public static boolean isPointWithinBounds(int xPos, int yPos, int width, int height, double mouseX, double mouseY) {
        return mouseX >= (double)(xPos - 1) &&
         mouseX < (double)(xPos + width + 1) &&
          mouseY >= (double)(yPos - 1) &&
           mouseY < (double)(yPos + height + 1);
    }

    @Override
    protected void init() 
    {
        super.init();
        classButtons = new ClassButton[8];
        arrowButtons = new ArrowButton[2];
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,false,false,true))
        {
            this.addButton(button);
        }
        int startX = xMid()-20;
        int startY = yGrid(1)+4;
        int buttonSize = 22;
        int spacing = 2;
        int index;

        arrowButtons[0] = this.addButton(new ArrowButton(xMid()-29, yGrid(3)+2, true, b->{}));
        arrowButtons[0].active = false;
        arrowButtons[0].visible = classReg.get(8) != null;
        arrowButtons[1] = this.addButton(new ArrowButton(xMid()+75, yGrid(3)+2, false, b->{}));
        arrowButtons[1].active = false;
        arrowButtons[1].visible = classReg.get(8) != null;

        for(int y = 0; y < 2; y++)
        {
            for(int x = 0; x < 4; x++)
            {
                index = (y*4)+x;
                classButtons[index] = this.addButton(new ClassButton(startX + (x*(buttonSize+spacing)), startY+(y*(buttonSize + spacing)), RPG_Registry.CLASS_REGISTRY.get(0), button ->
                {
                    confirm.active = true;
                    ClassButton cb = (ClassButton)button;
                    if(focusedButton != cb.index)
                    {
                        if(focusedButton != -1)
                            classButtons[focusedButton].setFocused(false);
                        focusedButton = cb.index;
                    }
                    cb.setFocused(true);
                    Util.debug(cb.rpgClass.name);
                    
                },index));
                classButtons[index].visible = false;
            }
        }
        confirm = this.addButton(new ButtonWidget(startX+50-22, yGrid(7)-1, 44, 16, "Confirm", b->{
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(Packets.CLIENT.addClass(classButtons[focusedButton].rpgClass));
            minecraft.openScreen(new ClassMenu());
        }));
        confirm.active = false;
    }

    @Override
    public void render(int mouseX, int mouseY, float float_1) 
    {
        if(pageIndex > 0)
        {
            arrowButtons[0].active = true;
        }
        this.renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - mouseX, (float)(int_4 + 75 - 50) - mouseY, this.minecraft.player);

        GUIhelper.drawString(this.font, "Classes", this.xMid()+7, yGrid(0), 0x000000);
        super.render(mouseX, mouseY, float_1);

        for(int i = 0; i < 8; i++)
        {
            int index = i + (8*pageIndex);
            RPGClass r = classReg.get(index);
            if(r != null)
            {
                classButtons[i].visible = true;
                classButtons[i].rpgClass = r;
            }
            else
            {
                break;
            }
            if(i == 7)
            {
                if(classReg.get(index+1) != null)
                {
                    arrowButtons[1].active = true;
                }
            }
            
        }
        for(ClassButton cb : classButtons)
        {
            if(!cb.visible)
            {
                break;
            }
            if(InitClassMenu.isPointOverClassButton(cb, mouseX, mouseY))
            {
                this.renderTooltip(cb.rpgClass.getToolTips(), mouseX, mouseY);
            }
        }
    }
    
}