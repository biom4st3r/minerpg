package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.MineRPG;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class ArrowButton extends ButtonWidget
{
    private Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    private boolean left;
    public ArrowButton(int xPos, int yPos, boolean left,
            PressAction buttonWidget$PressAction_1) 
    {
        super(xPos, yPos, 8, 13, "", buttonWidget$PressAction_1);
        this.left = left;
    }

    private int u;// = 256-16;
    private int v;// = 0;


    @Override
    public void renderButton(int mouseX, int mouseY, float float_1) {
        u = 256-16;
        v = 0;
        MinecraftClient.getInstance().getTextureManager().bindTexture(BG_Texture);
        if(!this.active)
        {
            u-=16;
        }
        if(!left)
        {
            u+=8;
        }
        this.blit(this.x,this.y,u,v,width,height);
    }
    
}