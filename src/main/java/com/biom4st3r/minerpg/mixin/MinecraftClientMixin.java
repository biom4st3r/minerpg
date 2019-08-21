package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGAbility.Type;
import com.biom4st3r.minerpg.util.InGameHudHelper;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Hand;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin
{

    

    @Shadow
    public InGameHud inGameHud;

    @Shadow
    public ClientPlayerEntity player;

    RPGPlayer rpgPlayer = (RPGPlayer)player;

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

    // @Inject(
    //     at = @At(
    //             value= "INVOKE_ASSIGN",
    //             target = "net/minecraft/entity/LivingEntity.swingHand(Lnet/minecraft/util/Hand;)V",
    //             shift=Shift.BEFORE),
    //     method="doAttack",
    //     cancellable = true)

    // @Inject(at = @At("HEAD"),method="doAttack",cancellable = true)
    // private void doAttackk(CallbackInfo ci)
    // {
    //     if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() && this.attackCooldown <=0)
    //     {
            
    //         RPGAbility ability = rpgPlayer.getRPGAbilityComponent().abilityBar.get(player.inventory.selectedSlot).ability;
    //         if(ability.getType() == RPGAbility.Type.LEFT_CLICK)
    //         {
    //             player.swingHand(Hand.OFF_HAND);
    //             player.swingHand(Hand.MAIN_HAND);
    //             //ability.doAbility(rpgPlayer);
    //             Util.debug("Did ability");
    //             ci.cancel();
    //         }
    //         //player.networkHandler.sendPacket(Packets.CLIENT.useAbility(player.inventory.selectedSlot));
    //         //ci.cancel();
    //     }
    // }
    @Inject(at = @At(value = "INVOKE",target = "net/minecraft/client/MinecraftClient.doAttack()V"), method = "handleInputEvents", cancellable = true)
    private void handleInputAttack(CallbackInfo ci)
    {
        RPGAbility ability = rpgPlayer.getRPGAbilityComponent().abilityBar.get(player.inventory.selectedSlot).ability;
            
        if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() &&
         this.attackCooldown <= 0 &&
          (ability.getType() == Type.LEFT_CLICK || ability.getType() == Type.USE))
        {
            



            ci.cancel();
        }
        
    }

    @Inject(at = @At(value = "INVOKE",target = "net/minecraft/client/MinecraftClient.doItemUse()V"), method = "handleInputEvents", cancellable = true)
    private void handleInputUse(CallbackInfo ci)
    {
        RPGAbility ability = rpgPlayer.getRPGAbilityComponent().abilityBar.get(player.inventory.selectedSlot).ability;
         
        if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() &&
         this.attackCooldown <= 0 &&
          (ability.getType() == Type.RIGHT_CLICK || ability.getType() == Type.USE))
        {




            ci.cancel();    
        }
        
    }







}