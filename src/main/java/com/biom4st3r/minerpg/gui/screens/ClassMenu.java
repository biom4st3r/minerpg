package com.biom4st3r.minerpg.gui.screens;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.gui.buttons.ClassButton;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.util.Identifier;

public class ClassMenu extends AbstractAbilitiesContainer {
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/classmenu.png");

    protected int left;
    protected int top;
    protected int containerWidth = 176;
    protected int containerHeight = 166;
    RPGPlayer player;
    ClassButton cb;

    public ClassMenu() {
        super();
    }

    @Override
    public boolean isPauseScreen() {
        return true == false;
    }
    
    @Override
    protected void init() {
        super.init();
        this.player = ((RPGPlayer)MinecraftClient.getInstance().player);
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        System.out.println(player.getRPGClassComponent().getRpgClass(0));
        cb = this.addButton(new ClassButton(this.xMid()-29, this.yGrid(0), player.getRPGClassComponent().getRpgClass(0), button ->
        {
            //RPGClassComponent
        }, 0));
        cb.active = false;

        int yPos = this.yMid()-96;
        for(ButtonWidget button : GUIhelper.drawTabs(this.xMid()-70, yPos, this.minecraft, false,false,false,true))
        {
            this.addButton(button);
        }

        // abilityButtons = GUIhelper.drawAbilities(17+this.left, 90+this.top);
        // for(ButtonWidget b : abilityButtons)
        // {
        //     this.addButton(b);
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
        // }
        

    }


    @Override
    public void render(int mouseX, int mouseY, float float_1) {
        this.renderBackground();
        
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int left = this.left;
        int top = this.top;
        this.blit(left, top, 0, 0, 176, 166);
        super.render(mouseX, mouseY, float_1);
        InventoryScreen.drawEntity(left + 51-18, top + 75, 30, (float)(left + 51) - mouseX, (float)(top + 75 - 50) - mouseY, this.minecraft.player);
        super.render(mouseX, mouseY, float_1);
        GUIhelper.drawString(this.font, cb.rpgClass.getDisplayName().asFormattedString(), xMid()-(28-22), yGrid(1)+3, 0x000000);
        
        //renderStats(player.getStatsComponent());
        // for(ButtonWidget bw : abilityButtons)
        // {
        //     if(GUIhelper.isPointOverAbilityButton((AbilityButton)bw, mouseX, mouseY))
        //     {
        //         this.renderTooltip(((AbilityButton)bw).ability.getToolTips(), mouseX, mouseY);
        //     }
        // }
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