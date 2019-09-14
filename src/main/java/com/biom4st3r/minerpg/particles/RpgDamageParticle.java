package com.biom4st3r.minerpg.particles;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.util.ParticleRegHelper;
import com.biom4st3r.minerpg.util.Util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Environment(EnvType.CLIENT)
public class RpgDamageParticle extends SpriteBillboardParticle {
    //Particle
    float drag = .95f;

    public RpgDamageParticle(World worldIn, double xPos, double yPos, double zPos, double velocityX, double velocityY, double velocityZ, float red, float green, float blue, float value) {
        super(worldIn,xPos,yPos,zPos);
        //ServerPlayerEntity
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.x = xPos;
        this.y = yPos;
        this.z = zPos;
        this.setColor(red,green,blue);
        this.colorAlpha = 1f;
        this.scale = 0.35f;
        this.maxAge = 30;
        //Util.debug(value);
        
        this.setSprite(((ParticleRegHelper)MinecraftClient.getInstance().particleManager)
            .getAtlas().getSprite(new Identifier(MineRPG.MODID, "rpg_damage"+((int)Math.floor(value)) )));
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
        else 
        {
            
            //this.colorBlue += 0.01f;
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