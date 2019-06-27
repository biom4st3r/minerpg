package com.biom4st3r.minerpg.gui;

import java.util.Random;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.Stat;
import com.biom4st3r.minerpg.api.Stat.Stats;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import org.lwjgl.opengl.GL;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
import net.minecraft.client.gui.screen.ingame.EnchantingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RPGMenu extends AbstractContainerScreen<ComponentContainer> {
    public Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/rpgmenu.png");
    private float mouseX;
    private float mouseY;
    private StatButton[] statButtons;
    private RPGPlayer player;

    public RPGMenu(ComponentContainer cc)
    {
        super(cc,cc.playerInv,new TextComponent(""));
        this.modY = new int[] {yMid()-75,yMid()-65,yMid()-55,yMid()-45,yMid()-35,yMid()-25};
    }
    private int xMid()
    {
        return this.width/2;
    }
    private int yMid()
    {
        return this.height/2;
    }


    public void drawString(String string_1, int xPos, int yPos, int color) {
        this.font.drawStringBounded(string_1, xPos, yPos, 160,color);
        // textRenderer.drawWithShadow(string_1, (float)(int_1 -
        // textRenderer_1.getStringWidth(string_1) / 2), (float)int_2, int_3);
    }

    public void drawCenteredString(String string_1, int xCenter, int yPos, int color) {
        this.font.drawStringBounded(string_1, (xCenter - this.font.getStringWidth(string_1) / 2), yPos, 160,
                color);
        // textRenderer.drawWithShadow(string_1, (float)(int_1 -
        // textRenderer_1.getStringWidth(string_1) / 2), (float)int_2, int_3);
    }

    @Override
    protected void init()
    {
        this.player = (RPGPlayer)this.minecraft.player;
        statButtons =  new StatButton[12];
        super.init();
        int bWidth = 26;
        //int bHeight = 14;
        this.modY = new int[] {yMid()-75,yMid()-65,yMid()-55,yMid()-45,yMid()-35,yMid()-25};

    
        
        this.addButton(new InventoryTab((this.width/2)-(bWidth-1), (this.height/2)-96, bWidth, "Main", button ->
        {
            this.minecraft.openScreen(new InventoryScreen(this.minecraft.player));
        }, false, 0));

        this.addButton(new InventoryTab((this.width/2), (this.height/2)-96, bWidth, "RPG", (ButtonWidget) ->
        {


        },true,0));
        for(int i = 0, j = 0; i < 6; i++, j+=2)
        {
            //System.out.println(j + " " + i);
            //System.out.println(j+1 + " " + i);
            statButtons[j] = this.addButton(new StatButton(xMid()-11,modY[i], null, true));
            statButtons[j+1] = this.addButton(new StatButton(xMid()+3, modY[i], null, false));
        }

        

    }
    int modY[] = new int[] {yMid()-75,yMid()-65,yMid()-55,yMid()-45,yMid()-35,yMid()-25};
        

    @Override
    public void render(int int1,int int2, float float1)
    {
        this.renderBackground();
        this.drawBackground(float1, int1, int2);
        this.mouseX = (float)int1;
        this.mouseY = (float)int2;
        super.render(int1, int2, float1);
        this.drawMouseoverTooltip(int1, int2);
        //EnchantingScreen
        float scale = 0.70f;

        int modX = 28;
        //int modY[] = new int[] {yMid()-75,yMid()-65,yMid()-55,yMid()-45,yMid()-35,yMid()-25};
        //this.addButton(new StatButton(10, 10, null, true));
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
            this.drawString(stat.toString().substring(0, 3), xPos, yPos, 0x000000);
            modX = 15;
            xPos = (int)((xMid()-modX)/scale);
            this.drawString(":", xPos, yPos, 0x000000);
            modX = 1;
            xPos = (int)((xMid()-modX)/scale);
            this.drawCenteredString("" + this.player.getStat(stat), xPos, yPos, 0x000000);
            
        }
        GlStateManager.scalef(1,1,1);
    }

    @Override
    protected void drawBackground(float arg0, int arg1, int arg2) 
    {
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(BG_Texture);
        int int_3 = this.left;
        int int_4 = this.top;
        this.blit(int_3, int_4, 0, 0, this.containerWidth, this.containerHeight);
        drawEntity(int_3 + 51-18, int_4 + 75, 30, (float)(int_3 + 51) - this.mouseX, (float)(int_4 + 75 - 50) - this.mouseY, this.minecraft.player);
    }

    public static void drawEntity(int int_1, int int_2, int int_3, float float_1, float float_2, LivingEntity livingEntity_1) {
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef((float)int_1, (float)int_2, 50.0F);
        GlStateManager.scalef((float)(-int_3), (float)int_3, (float)int_3);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float float_3 = livingEntity_1.field_6283;
        float float_4 = livingEntity_1.yaw;
        float float_5 = livingEntity_1.pitch;
        float float_6 = livingEntity_1.prevHeadYaw;
        float float_7 = livingEntity_1.headYaw;
        GlStateManager.rotatef(135.0F, 0.0F, 1.0F, 0.0F);
        GuiLighting.enable();
        GlStateManager.rotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(-((float)Math.atan((double)(float_2 / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        livingEntity_1.field_6283 = (float)Math.atan((double)(float_1 / 40.0F)) * 20.0F;
        livingEntity_1.yaw = (float)Math.atan((double)(float_1 / 40.0F)) * 40.0F;
        livingEntity_1.pitch = -((float)Math.atan((double)(float_2 / 40.0F))) * 20.0F;
        livingEntity_1.headYaw = livingEntity_1.yaw;
        livingEntity_1.prevHeadYaw = livingEntity_1.yaw;
        GlStateManager.translatef(0.0F, 0.0F, 0.0F);
        EntityRenderDispatcher entityRenderDispatcher_1 = MinecraftClient.getInstance().getEntityRenderManager();
        entityRenderDispatcher_1.method_3945(180.0F);
        entityRenderDispatcher_1.setRenderShadows(false);
        entityRenderDispatcher_1.render(livingEntity_1, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        entityRenderDispatcher_1.setRenderShadows(true);
        livingEntity_1.field_6283 = float_3;
        livingEntity_1.yaw = float_4;
        livingEntity_1.pitch = float_5;
        livingEntity_1.prevHeadYaw = float_6;
        livingEntity_1.headYaw = float_7;
        GlStateManager.popMatrix();
        GuiLighting.disable();
        GlStateManager.disableRescaleNormal();
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
     }



    
}