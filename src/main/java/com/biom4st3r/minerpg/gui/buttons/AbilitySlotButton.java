package com.biom4st3r.minerpg.gui.buttons;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.networking.Packets;
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
        // TODO Auto-generated constructor stub
        this.index = index;
        this.abilitiesC = abilitiesC;
    }
    
    @Override
    public void renderButton(int int_1, int int_2, float float_1) {
        if(this.isHovered())
        {
            fill(this.x, this.y, this.x + 16, this.y + 16, 0x80FFFFFF);
        }
        if(abilitiesC.abilityBar.get(index).ability != RpgAbilities.NONE)
        {
            GUIhelper.drawString(
                MinecraftClient.getInstance().textRenderer,
                abilitiesC.abilityBar.get(index).ability.name.getPath().substring(0,1), 
                this.x+6, this.y+4, 0xFFBB44);
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