package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class ClassButton extends ButtonWidget {

    public RPGClass rpgClass;
    private Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    public final int index;

    @Override
    public boolean changeFocus(boolean boolean_1) {
        return super.changeFocus(boolean_1);
    }

    public ClassButton(int xPos, int yPos, RPGClass rpgclass,
            PressAction buttonWidget$PressAction_1, int index) {
        super(xPos, yPos, 22, 22, "", buttonWidget$PressAction_1);
        this.rpgClass = rpgclass;
        this.index = index;
    }



    int frameU = 256-22;
    int frameV = 0+(16*3); 
    int bg_lightU = 256-16;
    int bg_lightV = 0+(16*1);

    @Override
    public void renderButton(int int_1, int int_2, float float_1) 
    {
        MinecraftClient.getInstance().getTextureManager().bindTexture(BG_Texture);
        blit(this.x,this.y,frameU,frameV,width,height);
        if((!this.isHovered && !this.isFocused() )|| !this.active)
        {
            blit(this.x+3,this.y+3,bg_lightU,bg_lightV,16,16);
        }
        else if((this.isHovered || this.isFocused()) && this.active)
        {
            blit(this.x+3,this.y+3,bg_lightU,bg_lightV+16,16,16);
        }
        MinecraftClient.getInstance().getTextureManager().bindTexture(rpgClass.getIcon());
        blit(this.x+3,this.y+3 ,16,16,16,16,16,16);//x y u v w h
        //GUIhelper.drawString(MinecraftClient.getInstance().textRenderer, 
        //rpgClass.name.getPath().substring(0,1).toUpperCase(),
        //this.x+9, this.y+7, 0x000000);
        //MinecraftClient.getInstance().getTextureManager().bindTexture(ICON);
        //blit(this.x+3,this.y+3,this.width,this.height,0,0,128,128,256,256);

        //super.renderButton(int_1, int_2, float_1);
    }
    
    @Override
    public void setFocused(boolean boolean_1) 
    {
        super.setFocused(boolean_1);
    }

    
}