package com.biom4st3r.minerpg.gui;

import com.biom4st3r.minerpg.MineRPG;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractContainerScreen;
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


    public RPGMenu(ComponentContainer cc)
    {
        super(cc,cc.playerInv,new TextComponent(""));
    }

    @Override
    public void render(int int1,int int2, float float1)
    {
        this.renderBackground();
        this.drawBackground(float1, int1, int2);
        this.mouseX = (float)int1;
        this.mouseY = (float)int2;
        super.render(int1, int2, float1);
        this.drawMouseoverTooltip(int1, int2);
        this.drawString(this.font, "STR", (this.width/2)-30, (this.height/2)-75, 0x99eeee);
        this.drawString(this.font, "DEX", (this.width/2)-30, (this.height/2)-65, 0x99eeee);
        this.drawString(this.font, "CON", (this.width/2)-30, (this.height/2)-55, 0x99eeee);
        this.drawString(this.font, "WIS", (this.width/2)-30, (this.height/2)-45, 0x99eeee);
        this.drawString(this.font, "INT", (this.width/2)-30, (this.height/2)-35, 0x99eeee);
        this.drawString(this.font, "CHA", (this.width/2)-30, (this.height/2)-25, 0x99eeee);

        this.drawString(this.font, "20", (this.width/2)-6, (this.height/2)-75, 0x99eeee);
        this.drawString(this.font, "12", (this.width/2)-6, (this.height/2)-65, 0x99eeee);
        this.drawString(this.font, "10", (this.width/2)-6, (this.height/2)-55, 0x99eeee);
        this.drawString(this.font, "8", (this.width/2)-6, (this.height/2)-45, 0x99eeee);
        this.drawString(this.font, "17", (this.width/2)-6, (this.height/2)-35, 0x99eeee);
        this.drawString(this.font, "8", (this.width/2)-6, (this.height/2)-25, 0x99eeee);

        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-75, 0x99eeee);
        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-65, 0x99eeee);
        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-55, 0x99eeee);
        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-45, 0x99eeee);
        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-35, 0x99eeee);
        this.drawString(this.font, ":", (this.width/2)-10, (this.height/2)-25, 0x99eeee);
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

    @Override
    protected void init()
    {
        super.init();
        int bWidth = 26;
        //int bHeight = 14;

        this.addButton(new InventoryTab((this.width/2)-(bWidth-1), (this.height/2)-96, bWidth, "Main", button ->
        {
            this.minecraft.openScreen(new InventoryScreen(this.minecraft.player));
        }, false, 0));

        this.addButton(new InventoryTab((this.width/2), (this.height/2)-96, bWidth, "RPG", (ButtonWidget) ->
        {


        },true,0));

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