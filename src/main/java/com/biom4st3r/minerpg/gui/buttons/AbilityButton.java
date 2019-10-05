package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.DefaultedObj;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class AbilityButton extends ButtonWidget
{
    public DefaultedObj<RPGAbility> ability = new DefaultedObj<RPGAbility>(null, RpgAbilities.NONE);

    private Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");
    
    public ButtonWidget.PressAction pressAction;

    public AbilityButton(int xPos, int yPos, RPGAbility ability, PressAction buttonWidget$PressAction_1) {
        super(xPos, yPos, 22, 22, "", buttonWidget$PressAction_1);
        this.ability.set(ability);
    }

    @Override
    public void onPress() {
        this.pressAction.onPress(this);
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
        if(ability.getValue() != RpgAbilities.NONE)
        {
            MinecraftClient.getInstance().getTextureManager().bindTexture(ability.getValue().getIcon());
            blit(this.x+3,this.y+3 ,16,16,16,16,16,16);//x y u v w h

        }
        if((this.isHovered || this.isFocused()) && this.active)
        {
            fill(this.x+3, this.y+3, this.x+3 + 16, this.y+3 + 16, 0x80FFFFFF);
        }
    }
    
}