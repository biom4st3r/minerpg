package com.biom4st3r.minerpg.particles;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.util.ParticleRegHelper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class RpgDamageParticle extends SpriteBillboardParticle {
    float drag = 1f;

    public RpgDamageParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ) {
        super(worldIn,xPos,yPos,zPos);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;
        this.setColor(1f, 0.5f, 1f);
        this.colorAlpha = 1f;
        this.scale = 2f;
        this.maxAge = 200;
        this.setSprite(((ParticleRegHelper)MinecraftClient.getInstance().particleManager)
            .getAtlas().getSprite(new Identifier(MineRPG.MODID, "rpg_damage")));
    }

    public ParticleTextureSheet getType() { 
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE; 
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().offset(x, y, z));
        this.repositionFromBoundingBox();
    }

    @Override
    public void tick() {
        if(this.age++ >= this.maxAge)
        {
            this.markDead();
        }
        else {
            this.prevPosX = this.x;
            this.prevPosY = this.y;
            this.prevPosZ = this.z;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= drag;
            this.velocityY *= drag;
            this.velocityZ *= drag;
            if(this.onGround) {
                this.velocityX *= drag/2;
                this.velocityZ *= drag/2;
            }
        }
    }

}