package com.biom4st3r.minerpg.mixin;



import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.gui.GUIhelper;
import com.biom4st3r.minerpg.mixin_interfaces.InGameHudHelper;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;

import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GuiLighting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
    /**
Purpose
Provides the hotbar overriding for the Ability Bar
    and New exp bar


*/
public abstract class InGameHudMixin extends DrawableHelper implements InGameHudHelper 
{
    private Identifier BG_Texture = new Identifier(MineRPG.MODID, "textures/gui/widgets.png");
    
    private boolean abilityBar = false;
    
    @Shadow @Final private MinecraftClient client;

    @Shadow
    private PlayerEntity getCameraPlayer() 
    {
        return null;
    }

    @Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    @Shadow private ItemStack currentStack;
    @Shadow private int heldItemTooltipFade;

    public boolean isAbilityBarActive()
    {
        return abilityBar;
    }

    @Override
    public void toggleRenderAbilityBar() 
    {
        abilityBar = !abilityBar;
    }

    @Unique
    public int getSelectedSlot()
    {
        return this.getCameraPlayer().inventory.selectedSlot;
    }

    @Inject(at = @At(opcode = Opcodes.INVOKESTATIC, value = "INVOKE",
        target = "net/minecraft/client/gui/hud/InGameHud.fill(IIIII)V"),
    method = "renderHeldItemTooltip",locals = LocalCapture.CAPTURE_FAILHARD)
    public void renderAbilitybarToolTipsNonEmpty(CallbackInfo ci,Text itemName,String itemNameText, int xPos, int yPos, int fadeDuration)
    {
        if(this.abilityBar)
        {
            RPGPlayer pe = (RPGPlayer)this.client.player;
            RPGAbility selectedAbility = pe.getRPGAbilityComponent().abilityBar.get(pe.getPlayer().inventory.selectedSlot);
            if(selectedAbility != RpgAbilities.NONE)
            {
                Text name = new TranslatableText(selectedAbility.getTranslationKey());
                int xPos2 = (this.scaledWidth - this.client.textRenderer.getStringWidth(name.asFormattedString()))/2;
                int yPos2 = this.scaledHeight - 49;
                if(!this.client.interactionManager.hasStatusBars())
                {
                    yPos2+=14;
                }
                if(fadeDuration > 0)
                {
                    //this.client.textRenderer.getClass();
                    fill(xPos2-2,yPos2-2,xPos2+this.client.textRenderer.getStringWidth(name.asFormattedString())+2,
                    yPos2+9+2,this.client.options.getTextBackgroundColor(0));
                    this.client.textRenderer.drawWithShadow(name.asFormattedString(),(float) xPos2, (float)yPos2, 16777215 + (fadeDuration << 24));
                    
                }
            }
        }
    }

    @Unique
    int custom_heldItemTooltipFade = 0;

    @Unique
    int currentSelectedSlot = -1;

    @Inject(at = @At("HEAD"),method="tick")
    public void tick(CallbackInfo ci)
    {
        RPGPlayer pe = (RPGPlayer)this.client.player;
        if(pe != null && !pe.getRPGAbilityComponent().abilityBar.get(this.getSelectedSlot()).isNone())
        {
                if(this.custom_heldItemTooltipFade > 0)
                {
                    --this.custom_heldItemTooltipFade;
                }
                else if(this.currentSelectedSlot != this.getSelectedSlot())
                {
                    this.currentSelectedSlot = this.getSelectedSlot();
                    this.custom_heldItemTooltipFade = 40;
                }
        }
        else
        {
            //CraftingTableScreen
            //CraftingContainer
            //CraftingTableContainer
            //RecipeManager
            this.custom_heldItemTooltipFade = 0;
        }
    }

    @Inject(at = @At(value = "HEAD"),method = "renderHeldItemTooltip",cancellable = true)
    public void renderAbilitybarToolTipsEmpty(CallbackInfo ci)
    {
        if(this.abilityBar && this.client.player.inventory.getMainHandStack().isEmpty())
        {

            RPGPlayer pe = (RPGPlayer)this.client.player;
            if(!(pe.getRPGAbilityComponent().abilityBar.get(this.getSelectedSlot()).isNone()))
            {
                RPGAbility selectedAbility = pe.getRPGAbilityComponent().abilityBar.get(pe.getPlayer().inventory.selectedSlot);
                if(selectedAbility.isNone())
                {
                    ci.cancel();
                }
                Text name = new TranslatableText(selectedAbility.getTranslationKey());
                int xPos = (this.scaledWidth - this.client.textRenderer.getStringWidth(name.asFormattedString()))/2;
                int yPos = this.scaledHeight - 49;
                if (!this.client.interactionManager.hasStatusBars()) {
                    yPos += 14;
                }
                int int_3 = (int)((float)this.custom_heldItemTooltipFade * 256.0F / 10.0F);
                if (int_3 > 255) {
                   int_3 = 255;
                }
                if(int_3 > 0)
                {
                    GlStateManager.pushMatrix();
                    GlStateManager.enableBlend();
                    GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
                    
                    int var10002 = xPos + this.client.textRenderer.getStringWidth(name.asFormattedString()) + 2;
                    fill(xPos-2, yPos-2, var10002, yPos + 9 + 2, this.client.options.getTextBackgroundColor(0));
                    this.client.textRenderer.drawWithShadow(name.asFormattedString(), (float)xPos, (float)yPos, 16777215 + (int_3 << 24));
                    GlStateManager.disableBlend();
                    GlStateManager.popMatrix();
                }

            }
            ci.cancel();
        }
        
    }


    @Inject(at = @At("HEAD"),method="renderHotbar",cancellable = true)
    protected void renderAbilityBar(float float_1,CallbackInfo ci)
    {
        PlayerEntity player = this.getCameraPlayer();
        if(abilityBar)
        {


            
            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.client.getTextureManager().bindTexture(BG_Texture);
            ItemStack offHandStack = player.getOffHandStack();
            Arm nonMainHand = player.getMainArm().getOpposite();
            int width = this.scaledWidth / 2;
            int savedBlitOffset = this.blitOffset;
            this.blitOffset = -90;

            this.blit(width - 91, this.scaledHeight - 22, 0, 0, 182, 22);
            this.blit(width - 91 - 1 + player.inventory.selectedSlot * 20, this.scaledHeight - 22 - 1, 0, 22, 24, 22);
            if (!offHandStack.isEmpty()) {
                if (nonMainHand == Arm.LEFT) {
                    this.blit(width - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                } else {
                    this.blit(width + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                }
            }

            this.blitOffset = savedBlitOffset;
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GuiLighting.enableForItems();

            //int i;
            int xPos;
            int yPos;
            for(int i = 0; i < 9; ++i) 
            {
                xPos = width - 90 + i * 20 + 2;
                yPos = this.scaledHeight - 16 - 3;
                this.renderAbilitySlot(xPos, yPos, ((RPGPlayer)player).getRPGAbilityComponent().abilityBar.get(i));
            }
            GuiLighting.disable();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableBlend();
            ci.cancel();

        }
    }

    @Inject(at = @At("HEAD"),method = "renderExperienceBar",cancellable = true)
    public void renderRPGExperienceBar(int xPos,CallbackInfo ci)
    {
        if(abilityBar)
        {
            RPGPlayer player = (RPGPlayer)this.getCameraPlayer();
            RPGClassComponent rcc = player.getRPGClassComponent();

            if(rcc.rpgClasses.size() > 0)
            {            
                this.client.getProfiler().push("expBar");
                this.client.getTextureManager().bindTexture(BG_Texture);

                int lvl = rcc.rpgClasses.get(rcc.getRpgClass(0));
                float currentExp = rcc.getExperience(0);
                float targetExp = rcc.getRpgClass(0).getExpRequiredForLvl(lvl+1);
                float prevTargetExp = rcc.getRpgClass(0).getExpRequiredForLvl(lvl);
                float expDeltaForLevelup = targetExp-prevTargetExp;
                float gainsThisLevel = currentExp-prevTargetExp;
                int progressWidth = //(int)(182*(currentExp/targetExp));
                (int)(182*(gainsThisLevel/expDeltaForLevelup));
                //Biow0rks.logger.debug("182 * (%s-%s)/(%s-%s)", targetExp,currentExp,targetExp,rcc.getRpgClass(0).getExpRequiredForLvl(lvl));
                
                //Biow0rks.logger.debug("currentPlayerLvl", lvl);
                //Biow0rks.logger.debug("current: %s\ntarget: %s\ndivid: %s\nresult: %s", currentExp,targetExp,(currentExp/targetExp),progressWidth);
                this.blit(xPos,this.scaledHeight - 32 + 3,0,46,182,5);
                if(progressWidth > 0)
                {
                    this.blit(xPos,this.scaledHeight - 32 +3,0,51,progressWidth,5);
                }
                this.client.getProfiler().pop();
                this.client.getProfiler().push("expLevel");
                TextRenderer tr = MinecraftClient.getInstance().textRenderer;
                String lvlString = "" + lvl;
                int stringWidth = (this.scaledWidth - tr.getStringWidth(lvlString))/2;
                int stringHeight = this.scaledHeight - 31 - 4;

                GUIhelper.drawLevel(tr, lvlString, stringWidth, stringHeight);

                this.client.getProfiler().pop();

            }

        
            ci.cancel();
            
        }
    }

    protected void renderAbilitySlot(int xPos, int yPos, RPGAbility rpgA)
    {
        if(rpgA != RpgAbilities.NONE)
        {
            GlStateManager.pushMatrix();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlphaTest();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
           


            this.client.getTextureManager().bindTexture(rpgA.getIcon());
            blit(xPos,yPos ,16,16,16,16,16,16);//x y u v w h

            GlStateManager.disableAlphaTest();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();

        }
    }
}