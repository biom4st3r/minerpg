package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.ClientInit;
import com.biom4st3r.minerpg.util.InGameHudHelper;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
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
    @Inject(at = @At("HEAD"),method="doAttack",cancellable = true)
    private void doAttackk(CallbackInfo ci)
    {
        if(((InGameHudHelper)this.inGameHud).isAbilityBarActive() && this.attackCooldown <=0)
        {
            player.swingHand(Hand.OFF_HAND);
            player.swingHand(Hand.MAIN_HAND);
            Util.debug("hello");
            ci.cancel();
            //player.networkHandler.sendPacket(Packets.CLIENT.useAbility(player.inventory.selectedSlot));
            //ci.cancel();
        }
    }


}