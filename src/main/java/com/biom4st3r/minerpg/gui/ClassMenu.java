package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.RPGComponent;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ClassMenu extends Screen
{
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    protected int containerWidth = 176;
    protected int containerHeight = 166;
    protected int left;
    protected int top;
    private ButtonWidget confirm;
    private ClassButton[] classButtons;
    private int focusedButton = -1;
    int pageIndex = 0;
    Registry<RPGClass> classReg = RPG_Registry.CLASS_REGISTRY;
    RPGPlayer player;

    public ClassMenu()
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

    private boolean isPointOverClassButton(ClassButton cb, double mouseX, double mouseY) {
        return this.isPointWithinBounds(cb.x, cb.y, 22, 22, mouseX, mouseY);
    }
  
    protected boolean isPointWithinBounds(int xPos, int yPos, int width, int height, double mouseX, double mouseY) {
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
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,false,false,true))
        {
            this.addButton(button);
        }
        int startX = xMid()-24;
        int startY = yGrid(1)+4;
        int buttonSize = 22;
        int spacing = 4;
        int index;
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
                    System.out.println(cb.rpgClass.name);
                },index));
                classButtons[index].visible = true;
            }
        }
        confirm = this.addButton(new ButtonWidget(startX+50-22, yGrid(7)-1, 44, 16, "Confirm", b->{
            MinecraftClient.getInstance().getNetworkHandler().sendPacket(Packets.CLIENT.addClass(classButtons[focusedButton].rpgClass));
        }));
        confirm.active = false;
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
        
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - mouseX, (float)(int_4 + 75 - 50) - mouseY, this.minecraft.player);

        GUIhelper.drawString(this.font, "Classes", this.xMid()+5, yGrid(0), 0x000000);
        super.render(mouseX, mouseY, float_1);
        


        // for(int i = 0; i < 8; i++)
        // {
        //     RPGClass r = classReg.get(i + (8*pageIndex));
        //     if(r != null)
        //     {
        //         classButtons[i].visible = true;
        //         classButtons[i].rpgClass = r;
        //     }
        //     else
        //     {
        //         //System.out.println(i);
        //         break;
        //     }
            
        // }
        for(ClassButton cb : classButtons)
        {
            if(!cb.visible)
            {
                break;
            }
            if(this.isPointOverClassButton(cb, mouseX, mouseY))
            {
                this.renderTooltip(cb.rpgClass.getToolTips(), mouseX, mouseY);
            }
        }
    }
    
}