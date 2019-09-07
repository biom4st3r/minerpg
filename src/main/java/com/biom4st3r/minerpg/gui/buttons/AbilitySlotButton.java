package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.RpgAbilityContext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;

public class AbilitySlotButton extends ButtonWidget
{
    public int index;
    //public RPGAbility ability = RpgAbilities.NONE;
    RPGAbilityComponent abilitiesC;
    public ButtonWidget.PressAction pressAction;
    public AbilitySlotButton(int int_1, int int_2, int index, RPGAbilityComponent abilitiesC,
            PressAction buttonWidget$PressAction_1) {
        super(int_1, int_2, 16, 16,"", buttonWidget$PressAction_1);
        this.index = index;
        this.abilitiesC = abilitiesC;
    }

    public RPGAbility getAbility()
    {  
        return this.abilitiesC.abilityBar.get(index).ability;
    }
    
    @Override
    public void renderButton(int int_1, int int_2, float float_1) {
        if(abilitiesC.abilityBar.get(index).ability != RpgAbilities.NONE)
        {
            //
            MinecraftClient.getInstance().getTextureManager().bindTexture(abilitiesC.abilityBar.get(index).ability.getIcon());
            blit(this.x,this.y ,16,16,16,16,16,16);//x y u v w h
        
        }
        if(this.isHovered())
        {
            fill(this.x, this.y, this.x + 16, this.y + 16, 0x80FFFFFF);
        }
    }

    public void setAbiliy(RpgAbilityContext a)
    {
        this.abilitiesC.abilityBar.set(index, a);
        //MinecraftClient.getInstance().getNetworkHandler().sendPacket(Packets.CLIENT.reqChangeAbilityBar(this.index, a, sourceClass, currentLvl, abilityAtLvlIndex));
    }
    public void resetAbility()
    {
        this.abilitiesC.abilityBar.set(index, RpgAbilityContext.EMPTY);
        //MinecraftClient.getInstance().getNetworkHandler().sendPacket(Packets.CLIENT.reqChangeAbilityBar(this.index, RpgAbilities.NONE, null, -1, -1));
    }

    @Override
    public void onPress() {
        this.pressAction.onPress(this);
    }
}