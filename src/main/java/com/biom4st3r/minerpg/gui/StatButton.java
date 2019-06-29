package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class StatButton extends ButtonWidget
{

    private static final long serialVersionUID = 682316677533249302L;

    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/rpgmenu.png");

    Stats stat;

    boolean add;

    public StatButton(int xPos, int yPos, PressAction action, boolean add, Stats stat) {
        super(xPos, yPos, 5, 5, "", action);
        this.add = add;
        this.stat = stat;

    }

    int u;
    int v;// = 255-17;

    @Override
    public void renderButton(int int_1, int int_2, float float_1) 
    {
        u = 0;
        if(add)
            v = 256-18;
        else
            v = 256-9;
        MinecraftClient.getInstance().getTextureManager().bindTexture(BG_Texture);
        if(this.isHovered)
        {
            u+=9;
        }
        blit(this.x,this.y,9,9,(float)(u),(float)(v),9,9,256,256);
        //this.blit((int)(this.x),(int)(this.y),u,v,this.width,this.height);

    }

    
}