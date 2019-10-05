package com.biom4st3r.minerpg.mixin;

    import com.biom4st3r.biow0rks.Biow0rks;

/*
    Purpose
        allows for the custom controls




    */

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGAbility.Type;
import com.biom4st3r.minerpg.mixin_interfaces.InGameHudHelper;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{
    @Shadow
    public InGameHud inGameHud;

    @Shadow
    public ClientPlayerEntity player;


    @Shadow
    protected int attackCooldown;

    @Inject(
        at = 
            @At(
                value = "INVOKE",
                target = "net/minecraft/client/options/KeyBinding.wasPressed()Z",
                ordinal = 3),
        method="handleInputEvents"
        )
    private void handleInputEvents(CallbackInfo ci)
    {
        while(ClientInit.swapHotBar.wasPressed())
        {
            ((InGameHudHelper)inGameHud).toggleRenderAbilityBar();
        }
    }

    @Inject(at = @At(value = "INVOKE",target = "net/minecraft/client/MinecraftClient.doAttack()V"), method = "handleInputEvents", cancellable = true)
    private void handleInputAttack(CallbackInfo ci)
    {
        RPGPlayer rpgPlayer = (RPGPlayer)player;
        RPGAbility ability = rpgPlayer.getRPGAbilityComponent().abilityBar.get(player.inventory.selectedSlot);
            
        if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() &&
         this.attackCooldown <= 0 && ability != RpgAbilities.NONE &&
          (ability.getType() == Type.LEFT_CLICK || ability.getType() == Type.USE))
        {
            
            Biow0rks.debug(ability.id.getPath());
            ability.doAbility(rpgPlayer);
            rpgPlayer.getNetworkHandlerC().sendPacket(Packets.CLIENT.useAbility(player.inventory.selectedSlot));
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "INVOKE",target = "net/minecraft/client/MinecraftClient.doItemUse()V"), method = "handleInputEvents", cancellable = true)
    private void handleInputUse(CallbackInfo ci)
    {
        RPGPlayer rpgPlayer = (RPGPlayer)player;
        RPGAbility ability = rpgPlayer.getRPGAbilityComponent().abilityBar.get(player.inventory.selectedSlot);

        if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() &&
         this.attackCooldown <= 0 && ability != RpgAbilities.NONE &&
          (ability.getType() == Type.RIGHT_CLICK || ability.getType() == Type.USE))
        {
            Biow0rks.debug(ability.id.getPath());
            ability.doAbility(rpgPlayer);
            rpgPlayer.getNetworkHandlerC().sendPacket(Packets.CLIENT.useAbility(player.inventory.selectedSlot));
            
            ci.cancel();    
        }
    }







}