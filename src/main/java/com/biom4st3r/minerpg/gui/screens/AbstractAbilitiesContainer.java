package com.biom4st3r.minerpg.gui.screens;

import java.util.List;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.gui.buttons.AbilityButton;
import com.biom4st3r.minerpg.gui.buttons.AbilitySlotButton;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.util.DefaultedObj;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.text.TranslatableText;

public abstract class AbstractAbilitiesContainer extends Screen {

    protected AbstractAbilitiesContainer() {
        super(new TranslatableText("string_1"));
    }
    //protected RpgAbilityContext mouseSlot = RpgAbilityContext.EMPTY;
    protected DefaultedObj<RPGAbility> mouseSlot = new DefaultedObj<>(null, RpgAbilities.NONE);
    protected ButtonWidget[] abilityDisplay;
    protected ButtonWidget[] abilitySlots;
    protected ButtonWidget[] arrowbuttons;
    protected RPGPlayer player;
    protected int top;
    protected int left;
    protected int abilityIndex;
    protected List<RPGAbility> abilities;
    

    @Override
    protected void init() {
        this.left = (this.width - 176) / 2;
        this.top = (this.height - 166) / 2;
        this.player = ((RPGPlayer)this.minecraft.player);
        //RPGAbilityComponent
        abilities = player.getRPGAbilityComponent().getAbilities();
        abilityDisplay = GUIhelper.drawAbilities(17+this.left, 90+this.top);
        for(int i = 0; i < abilityDisplay.length; i++)//ButtonWidget b : abilityDisplay)
        {
            this.addButton(abilityDisplay[i]);
            ((AbilityButton)abilityDisplay[i]).pressAction = bu -> {
                RPGAbility ab = ((AbilityButton)bu).ability.getValue();
                // if(ab.ability.getType() != Type.PASSIVE || true)
                // {
                    this.mouseSlot.set(ab);
                    Biow0rks.debug(ab.toString());
                // }
                // else if(ab.ability.getType() == Type.PASSIVE)
                // {
                //     ab.ability.doAbility(player);
                //     player.getNetworkHandlerC().sendPacket(Packets.CLIENT.usePassiveAbility(ab.abilityIndexInClass));
                // }
                // else if(ab.ability.getType() == Type.PASSIVE_NAMED)
                // {
                    
                // }
            };
            if(abilities.size() <= i || abilities.get(i).isNone())
            {
                abilityDisplay[i].visible = false;
            }
            else
            {
                RPGClassComponent classComp = player.getRPGClassComponent();
                RPGClass rpgClass = classComp.getRpgClass(0);
                ((AbilityButton) abilityDisplay[i]).ability.set(abilities.get(i));// = new RpgAbilityContext(classComp.getRpgClassContext(rpgClass),i,abilities[i]);
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
                if(this.mouseSlot.isDefault() && asb.index == -1)
                {
                    asb.resetAbility();
                    player.getNetworkHandlerC().sendPacket(Packets.CLIENT.reqChangeAbilityBar(asb.index,RpgAbilities.NONE));
                }
                else
                {
                    asb.setAbiliy(mouseSlot.getValue());
                    player.getNetworkHandlerC().sendPacket(Packets.CLIENT.reqChangeAbilityBar(asb.index, mouseSlot.getValue()));
                    mouseSlot.set(null);
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
            if(this.abilities.size() <= 12)
            {
                this.arrowbuttons[1].visible = false;
            }
            else
            {
                this.arrowbuttons[1].visible = false;
            }
        }
        super.render(mouseX, mouseY, float_1);
        GuiLighting.disable();
        for(ButtonWidget bw : abilityDisplay)
        {
            if(bw.visible && bw.isHovered())//GUIhelper.isPointOverAbilityButton((AbilityButton)bw, mouseX, mouseY))
            {
                this.renderTooltip(((AbilityButton)bw).ability.getValue().getToolTips(), mouseX, mouseY);
            }
        }
        for(ButtonWidget bw : abilitySlots)
        {
            if(!(((AbilitySlotButton)bw).getAbility().isNone()) && bw.isHovered())//GUIhelper.isPointOverAbilityButton((AbilitySlotButton)bw, mouseX, mouseY))
            {
                this.renderTooltip(((AbilitySlotButton)bw).getAbility().getToolTips(), mouseX, mouseY);            
            }
        }
        if(!mouseSlot.isDefault())
        {
            //GUIhelper.drawString(this.font, mouseSlot.ability.name.getPath(), mouseX, mouseY, 0xFFBB44);
            MinecraftClient.getInstance().getTextureManager().bindTexture(mouseSlot.getValue().getIcon());
            blit(mouseX ,mouseY ,16,16,16,16,16,16);//x y u v w h
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
    public void renderStats(RPGStatsComponent rpgStatsComponent)
    {
        int modX = 28;
        //GUIhelper.drawString(this.font, "Stats", xMid()-22, yGrid(0), 0x000000);

        for(int i = 0; i < 6; i++)
        {

            Stat stat = Stat.values()[i];

            
            modX = -28;

            GUIhelper.drawString(this.font, stat.toString().substring(0, 3), xMid()+modX, yGrid(i), 0x000000);
            modX = -10;

            GUIhelper.drawString(this.font, ":", xMid()+modX, yGrid(i), 0x000000);
            modX = 10;

            GUIhelper.drawCenteredString(this.font, "" + rpgStatsComponent.getStat(stat), xMid()+modX, yGrid(i), 0x000000);
            
        }

        if(rpgStatsComponent.remainingPoints != 0)
        {
            GUIhelper.drawString(this.font, "Points: ", xMid()-28, yGrid(7), 0x000000);
            GUIhelper.drawCenteredString(this.font, "" + rpgStatsComponent.remainingPoints, xMid()+12, yGrid(7), 0x000000);
        }
    }
    

}