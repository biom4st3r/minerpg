package com.biom4st3r.minerpg.mixin;



import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;

/**
Hook for listening to stats for Class levelup


*/
@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerStatWatch
{

    @Inject(at = @At("HEAD"),method="increaseStat",cancellable = false)
    public void increaseStat(Stat<?> stat, int i, CallbackInfo ci)
    {
        RPGPlayer player = ((RPGPlayer)this);
        player.getRPGClassComponent().processStat(stat,i);
    }

    
}