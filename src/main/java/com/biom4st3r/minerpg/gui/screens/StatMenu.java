package com.biom4st3r.minerpg.gui.screens;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.gui.buttons.StatButton;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class StatMenu extends AbstractAbilitiesContainer {

    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/statsmenu.png");
    private StatButton[] statButtons;
    ButtonWidget confirmButton;
    //private int modY[];

    private RPGStatsComponent rpgComponent;
    private RPGStatsComponent backupComponent;

    protected int containerWidth = 176;
    protected int containerHeight = 166;
    protected int left;
    protected int top;

    // ButtonWidget[] abilityButtons;
    // ButtonWidget[] arrowbuttons;
    // ButtonWidget[] abilitySlots;

    //RPGAbility mouseSlot = RpgAbilities.NONE;

    public StatMenu() {
        super();
    }

    @Override
    public void tick() {
        super.tick();
        if(this.rpgComponent.remainingPoints > 0 && !this.statButtons[0].visible)
        {
            for(StatButton button : statButtons)
            {
                button.visible = true;
            }
        }
        if(backupComponent.getStat(Stats.CONSTITUTION) == -1)
        {
            this.backupComponent = this.rpgComponent.copyOfStats();
        }
        // else if(this.rpgComponent.remainingPoints <= 0 && this.statButtons[0].visible)
        // {

        // }
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

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        this.rpgComponent = ((RPGPlayer)this.minecraft.player).getStatsComponent();
        this.backupComponent = this.rpgComponent.copyOfStats();
        statButtons =  new StatButton[12];

        for(int i = 0, j = 0; i < 6; i++, j+=2)
        {
            int xMod = xMid()-7;
            statButtons[j] = this.addButton(new StatButton(xMod, yGrid(i+1)-1, (button) ->
            { // minus
                Stats s = ((StatButton)button).stat;
                Util.debug(s + " " + this.backupComponent.getStat(s));
                if(this.rpgComponent.getStat(s) > this.backupComponent.getStat(s))
                {
                    this.confirmButton.active = true;
                    this.rpgComponent.decreaseStatUnProtected(s);
                }
            }, false,Stats.values()[i]));


            statButtons[j+1] = this.addButton(new StatButton(xMod+25 , yGrid(i+1)-1, (button) ->
            { //plus
                this.confirmButton.active = true;
                this.rpgComponent.increaseStatProtected(((StatButton)button).stat);
            }, true,Stats.values()[i]));

            statButtons[j].visible = false;
            statButtons[j+1].visible = false;
        }
        //int buttonWidth = 26+5;
        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,false,true,false))
        {
            this.addButton(button);
        }
        confirmButton = this.addButton(new ButtonWidget(xMid()+31, yMid()-50, 50, 20, "Confirm", button -> {
            for(StatButton statbuttons : statButtons)
            {
                statbuttons.active = false;
            }
            backupComponent = this.rpgComponent.copyOfStats();
            MinecraftClient
                .getInstance()
                .getNetworkHandler()
                .sendPacket(Packets.CLIENT.statChange(rpgComponent));
            Util.debug("Checking rpgComponent 1");
            button.visible = false;
        }));
        if(this.rpgComponent.remainingPoints > 0)
            this.confirmButton.active = false;
        else
            this.confirmButton.visible = false;


        // abilityButtons = GUIhelper.drawAbilities(17+this.left, 90+this.top);
        // for(ButtonWidget b : abilityButtons)
        // {
        //     this.addButton(b);
        //     ((AbilityButton)b).pressAction = bu -> {
        //         this.mouseSlot = ((AbilityButton)bu).ability;

        //     };
        // }
        // arrowbuttons = GUIhelper.drawAbilityArrows(17+this.left, 90+this.top);
        // for(ButtonWidget b : arrowbuttons)
        // {
        //     this.addButton(b);
        // }
        // abilitySlots = GUIhelper.drawAbilitySlots(xMid()-80,yGrid(15)+1);
        // for(ButtonWidget b : abilitySlots)
        // {
        //     this.addButton(b);
        //     RPGAbilityComponent ac = ((RPGPlayer)this.minecraft.player).getRPGAbilityComponent();
        //     ((AbilitySlotButton)b).pressAction = bu -> {
        //         AbilitySlotButton asb = ((AbilitySlotButton)bu);
        //         if(this.mouseSlot == RpgAbilities.NONE)
        //         {
        //             asb.resetAbility();
        //         }
        //         else
        //         {
        //             asb.setAbiliy(mouseSlot);
        //             mouseSlot = RpgAbilities.NONE;
        //         }
        //     };
        // }
        
    }

    // @Override
    // public boolean mouseClicked(double double_1, double double_2, int int_1) {
    //     for(int i  = 0; i < abilityButtons.length && mouseSlot != RpgAbilities.NONE;i++)
    //     {
    //         if(abilityButtons[i].active && abilityButtons[i].visible && GUIhelper.isPointOverAbilityButton(((AbilityButton)abilityButtons[i]),double_1,double_2))
    //         {
    //             mouseSlot = ((AbilityButton)abilityButtons[i]).ability;
    //         }
    //     }
    //     return true;
    // }

    @Override
    public void render(int mouseX, int mouseY, float float_1) {
        this.renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        super.render(mouseX, mouseY, float_1);
        renderStats();
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - mouseX, (float)(int_4 + 75 - 50) - mouseY, this.minecraft.player);
        //float scale = 0.91f;
        

    }

    public void renderStats()
    {
        int modX = 28;
        GUIhelper.drawString(this.font, "Stats", xMid()-22, yGrid(0), 0x000000);

        for(int i = 0; i < 6; i++)
        {

            Stats stat = Stats.values()[i];

            
            modX = -28;

            GUIhelper.drawString(this.font, stat.toString().substring(0, 3), xMid()+modX, yGrid(i+1), 0x000000);
            modX = -10;

            GUIhelper.drawString(this.font, ":", xMid()+modX, yGrid(i+1), 0x000000);
            modX = 10;

            GUIhelper.drawCenteredString(this.font, "" + this.rpgComponent.getStat(stat), xMid()+modX, yGrid(i+1), 0x000000);
            
        }

        if(this.rpgComponent.remainingPoints != 0)
        {
            GUIhelper.drawString(this.font, "Points: ", xMid()-28, yGrid(7), 0x000000);
            GUIhelper.drawCenteredString(this.font, "" + this.rpgComponent.remainingPoints, xMid()+12, yGrid(7), 0x000000);
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
    
}