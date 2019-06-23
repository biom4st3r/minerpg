package com.biom4st3r.minerpg.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

@Mixin(ServerPlayerEntity.class)
public abstract class ChangeDimUpdateBag
{
    @Inject(at = @At("RETURN"), method = "changeDimension")
    public void changeDimension(DimensionType dimensionType_1, CallbackInfoReturnable<Entity> ci)
    {
        //tell client the items in ComponentBag to fix desync
    }

    /*
`       PlayerManager.method_14594(this);
        PlayerManager

    public void method_14594(ServerPlayerEntity serverPlayerEntity_1) {
        serverPlayerEntity_1.openContainer(serverPlayerEntity_1.playerContainer);
        serverPlayerEntity_1.method_14217();
        serverPlayerEntity_1.networkHandler.sendPacket(new HeldItemChangeS2CPacket(serverPlayerEntity_1.inventory.selectedSlot));
    }


    */
}
// nope
// @Mixin(PlayerManager.class)
// public abstract class ChangeDimUpdateBag
// {
//     @Inject(at = @At("HEAD"), method = "method_14594")
//     public void method_14594(ServerPlayerEntity sPE, CallbackInfo ci)
//     {
//         sPE.openContainer(MineRPG.toRPG(sPE).getComponentContainer());
//     }
// }