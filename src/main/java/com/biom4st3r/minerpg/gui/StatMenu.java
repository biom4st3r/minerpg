package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.components.StatsComponent;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class StatMenu extends Screen {

    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/statsmenu.png");
    private StatButton[] statButtons;
    ButtonWidget confirmButton;
    //private int modY[];

    private StatsComponent rpgComponent;
    private StatsComponent backupComponent;

    protected int containerWidth = 176;
    protected int containerHeight = 166;
    protected int left;
    protected int top;

    public StatMenu() {
        super(new TranslatableText(""));
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
                statbuttons.visible = false;
            }
            backupComponent = this.rpgComponent.copyOfStats();
            MinecraftClient
                .getInstance()
                .getNetworkHandler()
                .sendPacket(Packets.CLIENT.statChange(rpgComponent));
            Util.debug("Checking rpgComponent 1");
            button.visible = false;
        }));
        this.confirmButton.active = false;

    }

    @Override
    public void render(int mouseX, int mouseY, float float_1) {
        this.renderBackground();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        super.render(mouseX, mouseY, float_1);
        InventoryScreen.drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - mouseX, (float)(int_4 + 75 - 50) - mouseY, this.minecraft.player);

        //float scale = 0.91f;
        renderStats();
        
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