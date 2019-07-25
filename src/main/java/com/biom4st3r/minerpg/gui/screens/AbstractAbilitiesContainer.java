package com.biom4st3r.minerpg.gui.screens;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.gui.buttons.AbilityButton;
import com.biom4st3r.minerpg.gui.buttons.AbilitySlotButton;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;

public abstract class AbstractAbilitiesContainer extends Screen {

    protected AbstractAbilitiesContainer() {
        super(new TranslatableText("string_1"));
        // TODO Auto-generated constructor stub
    }
    protected RPGAbility mouseSlot = RpgAbilities.NONE;
    protected ButtonWidget[] abilityDisplay;
    protected ButtonWidget[] abilitySlots;
    protected ButtonWidget[] arrowbuttons;
    protected RPGPlayer player;
    protected int top;
    protected int left;
    protected int abilityIndex;
    protected RPGAbility[] abilities;
    

    @Override
    protected void init() {
        this.left = (this.width - 176) / 2;
        this.top = (this.height - 166) / 2;
        this.player = ((RPGPlayer)this.minecraft.player);
        abilities = player.getRPGClassComponent().getAvalibleAbilities();
        abilityDisplay = GUIhelper.drawAbilities(17+this.left, 90+this.top);
        for(int i = 0; i < abilityDisplay.length; i++)//ButtonWidget b : abilityDisplay)
        {
            this.addButton(abilityDisplay[i]);
            ((AbilityButton)abilityDisplay[i]).pressAction = bu -> {
                this.mouseSlot = ((AbilityButton)bu).ability;

            };
            if(abilities.length <= i)
            {
                abilityDisplay[i].visible = false;
            }
            else
            {
                ((AbilityButton) abilityDisplay[i]).ability = abilities[i];
            }
        }
        arrowbuttons = GUIhelper.drawAbilityArrows(17+this.left, 90+this.top);
        for(ButtonWidget b : arrowbuttons)
        {
            this.addButton(b);
        }
        abilitySlots = GUIhelper.drawAbilitySlots(xMid()-80,yGrid(15)+1);
        for(ButtonWidget b : abilitySlots)
        {
            this.addButton(b);
            //RPGAbilityComponent ac = ((RPGPlayer)this.minecraft.player).getRPGAbilityComponent();
            ((AbilitySlotButton)b).pressAction = bu -> {
                AbilitySlotButton asb = ((AbilitySlotButton)bu);
                if(this.mouseSlot == RpgAbilities.NONE)
                {
                    asb.resetAbility();
                }
                else
                {
                    asb.setAbiliy(mouseSlot);
                    mouseSlot = RpgAbilities.NONE;
                }
            };
        }   
    }
    

    @Override
    public void render(int mouseX, int mouseY, float float_1) {
        {
            if(this.abilityIndex == 0)
            {
                this.arrowbuttons[0].visible = false;
            }
            else{
                this.arrowbuttons[0].visible = true;
            }
            if(this.abilities.length <= 12)
            {
                this.arrowbuttons[1].visible = false;
            }
            else
            {
                this.arrowbuttons[1].visible = false;
            }
        }
        super.render(mouseX, mouseY, float_1);
        for(ButtonWidget bw : abilityDisplay)
        {
            if(bw.visible && GUIhelper.isPointOverAbilityButton((AbilityButton)bw, mouseX, mouseY))
            {
                this.renderTooltip(((AbilityButton)bw).ability.getToolTips(), mouseX, mouseY);
            }
        }
        if(mouseSlot != RpgAbilities.NONE)
        {
            GUIhelper.drawString(this.font, mouseSlot.name.getPath(), mouseX, mouseY, 0xFFBB44);
        }
        
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
    

}