package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.MineRPG;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class AcceptanceButton extends ButtonWidget
{
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/statsmenu.png");
    
    Boolean red;

    public AcceptanceButton(int x, int y, int width, int height, Boolean red,
            PressAction buttonWidget$PressAction_1) {
        super(x, y, width, height, "", buttonWidget$PressAction_1);
        this.red = red;
    }


    int u = 256;
    int v = 256;
    @Override
    public void renderButton(int int_1, int int_2, float float_1) 
    {
        MinecraftClient.getInstance().getTextureManager().bindTexture(BG_Texture);
        u = 256-15;
        v = 0;
        if(red)
        {
            u-=15;
        }
        if(this.isHovered())
        {
            v+=15;
        }
        blit(this.x,this.y,this.width,this.height,u,v,15,15,256,256);
        
    }

}