package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.entities.Fireball;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.packet.EntitySpawnS2CPacket;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin
{

    @Shadow
    private ClientWorld world;

    @Inject(at = @At(value="HEAD", shift = Shift.AFTER),method = "onEntitySpawn",cancellable = true)
    public void onEntitySpawn(EntitySpawnS2CPacket packet,CallbackInfo ci)
    { 
        Object entity_15;
        double x,y,z;
        x = packet.getX(); y = packet.getY(); z = packet.getZ();
        if(packet.getEntityTypeId() == MineRPG.FIREBALL)
        {
            entity_15 = new Fireball(this.world,x,y,z,packet.getVelocityX(),packet.getVelocityY(),packet.getVelocityz());
            Fireball f = ((Fireball)entity_15);
            f.posX = x; f.posY = y; f.posZ = z; 
            f.setVelocity(packet.getVelocityX(), packet.getVelocityY(), packet.getVelocityz());
            finishReg(f, packet);
            ci.cancel();
        }

    }

    public void finishReg(Entity e,EntitySpawnS2CPacket packet)
    {
        e.updateTrackedPosition(e.x, e.y, e.z);
        e.pitch = (packet.getPitch() * 360 ) / 256.0f;
        e.yaw = (packet.getYaw() * 360) / 256.0f;
        e.setEntityId(packet.getId());
        this.world.addEntity(packet.getId(), e);

    }
    
}