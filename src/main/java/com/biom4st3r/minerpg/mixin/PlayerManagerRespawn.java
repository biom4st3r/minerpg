package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.dimension.DimensionType;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerRespawn
{
    @Inject(at = @At("RETURN"),method = "respawnPlayer",cancellable = true)
    public void respawnPlayer(ServerPlayerEntity originalPlayerEntity, DimensionType dimensionType_1, boolean boolean_1,CallbackInfoReturnable<ServerPlayerEntity> ci)
    {
        ServerPlayerEntity playersNewEntity = ci.getReturnValue();
        ((RPGPlayer)playersNewEntity).respawn(originalPlayerEntity);
        ci.setReturnValue(playersNewEntity);
    }

    @Inject(at = @At("HEAD"),method = "method_14594")
    public void updateInventory(ServerPlayerEntity spe,CallbackInfo ci)
    {
        spe.openContainer(((RPGPlayer)spe).getComponentContainer());
        Util.debugV("updateInventory", 10);
    }
    // @Inject(at = @At("TAIL"),method = "createPlayer")
    // public void createPlayer(CallbackInfoReturnable<ServerPlayerEntity> ci)
    // {
    //     RPGPlayer player = ((RPGPlayer)ci.getReturnValue());
    //     ServerPlayNetworkHandler nh = ci.getReturnValue().networkHandler;
    //     nh.sendPacket(Packets.SERVER.sendRPGClassComponent(player));
    //     nh.sendPacket(Packets.SERVER.sendStats(player));
    // }
    // @Inject(at = @At("TAIL"),method="onPlayerConnect")
    // public void onPlayerConnect(final ClientConnection clientConnection_1, final ServerPlayerEntity serverPlayerEntity_1, CallbackInfo ci)
    // {
    //     RPGPlayer player = ((RPGPlayer)serverPlayerEntity_1);
    //     ServerPlayNetworkHandler nh = serverPlayerEntity_1.networkHandler;
    //     nh.sendPacket(Packets.SERVER.sendRPGClassComponent(player));
    //     nh.sendPacket(Packets.SERVER.sendStats(player));

    // }
}