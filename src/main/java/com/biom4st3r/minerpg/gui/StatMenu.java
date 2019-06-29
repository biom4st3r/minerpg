package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.RPGComponent;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class StatMenu extends Screen {

    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/statsmenu.png");
    private StatButton[] statButtons;
    private int modY[];

    private RPGComponent rpgComponent;
    private RPGComponent backupComponent;

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
        else if(this.rpgComponent.remainingPoints <= 0 && this.statButtons[0].visible)
        {
            for(StatButton button : statButtons)
            {
                button.visible = false;
            }
            MinecraftClient.getInstance()
            .getNetworkHandler()
            .sendPacket(ClientInit.statChange(rpgComponent));
            System.out.println("Checking rpgComponent 1");
        }
    }

    @Override
    protected void init() {
        super.init();
        this.left = (this.width - this.containerWidth) / 2;
        this.top = (this.height - this.containerHeight) / 2;
        this.rpgComponent = ((RPGPlayer)this.minecraft.player).getRPGComponent();
        this.backupComponent = this.rpgComponent.copy();
        //System.out.println("test");
        statButtons =  new StatButton[12];

        this.modY = new int[6];
        {
            int ySpacing = 8;
            modY[0] = yMid()-57;
            modY[1] = modY[0]+ySpacing;
            modY[2] = modY[1]+ySpacing;
            modY[3] = modY[2]+ySpacing;
            modY[4] = modY[3]+ySpacing;
            modY[5] = modY[4]+ySpacing;
        }

        for(int i = 0, j = 0; i < 6; i++, j+=2)
        {
            //System.out.println(j + " " + i);
            //System.out.println(j+1 + " " + i);
            statButtons[j] = this.addButton(new StatButton(xMid()-11, modY[i], (button) ->
            { // minus
                Stats s = ((StatButton)button).stat;
                if(this.rpgComponent.getStat(s) > this.backupComponent.getStat(s))
                {
                    this.rpgComponent.decreaseStatUnProtected(s);
                }
            }, false,Stats.values()[i]));
            statButtons[j+1] = this.addButton(new StatButton(xMid()+3 , modY[i], (button) ->
            { //plus
                this.rpgComponent.increaseStatProtected(((StatButton)button).stat);
            }, true,Stats.values()[i]));

            statButtons[j].visible = false;
            statButtons[j+1].visible = false;
        }
        int buttonWidth = 26+5;
        int yPos = this.yMid()-96;
        this.addButton(new InventoryTab(this.xMid()-(buttonWidth-1), yPos, buttonWidth, "Main", button ->
        {
            this.minecraft.openScreen(new InventoryScreen(this.minecraft.player));
        }, false, 0));

        this.addButton(new InventoryTab(this.xMid(), yPos, buttonWidth, "Bag", (ButtonWidget) ->
        {
            this.minecraft.getNetworkHandler().sendPacket(ClientInit.openRpgMenu());

        }, false, 1));
        this.addButton(new InventoryTab(this.xMid()+(buttonWidth-1), yPos, buttonWidth, "Stats", b ->
        {
            this.minecraft.openScreen(new StatMenu());

        }, true, 2));
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

        float scale = 0.91f;
        int modX = 28;
        
        GUIhelper.drawString(this.font, "Stats", xMid()-22, yMid()-68, 0x000000);

        //GlStateManager.popMatrix();
        scale = 1f;
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale,scale,scale);

        for(int i = 0; i < 6; i++)
        {
            int xPos = (int)((xMid()-modX)/scale);
            int yPos = (int)(modY[i]/scale);
            Stats stat = Stats.values()[i];

            //System.out.println(player == null);
            //System.out.println(player.getStats() == null);
            //System.out.println(player.getStat(stat));
            
            modX = 28;
            xPos = (int)((xMid()-modX)/scale);
            GUIhelper.drawString(this.font, stat.toString().substring(0, 3), xPos, yPos, 0x000000);
            modX = 15;
            xPos = (int)((xMid()-modX)/scale);
            GUIhelper.drawString(this.font, ":", xPos, yPos, 0x000000);
            modX = 1;
            xPos = (int)((xMid()-modX)/scale);
            GUIhelper.drawCenteredString(this.font, "" + this.rpgComponent.getStat(stat), xPos, yPos, 0x000000);

            
        }
        if(this.rpgComponent.remainingPoints != 0)
        {
            GUIhelper.drawString(this.font, "Points: ", (int)((xMid()-28)/scale), (int)(yMid()/scale)-11, 0x000000);
            GUIhelper.drawCenteredString(this.font, "" + this.rpgComponent.remainingPoints, (int)((xMid()-1)/scale), (int)(yMid()/scale)-11, 0x000000);
        }

        GlStateManager.scalef(1,1,1);
        GlStateManager.popMatrix();
        //GlStateManager.pushMatrix();
        if(this.rpgComponent.remainingPoints != 0)
        {
            Screen.fill(xMid()-29, yMid()-11, xMid()+2, yMid()-10, 0xff000000);
        }
        //GlStateManager.popMatrix();
        
        
        
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