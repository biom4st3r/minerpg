package com.biom4st3r.minerpg.registery;

import com.biom4st3r.biow0rks.Biow0rks;

import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;


public class ParticleRegistery {
    /*
     * Register on spriteAtlas in client(Identifier) Register Factory in
     * ParticleManager
     * 
     * 
     * 
     */
    
    public <T extends ParticleEffect> void registerParticle(Identifier id,ParticleType<T> type, ParticleFactory<T> factory) 
    {
        if(id != null)
        {
            Biow0rks.error("You must register texture ids seperatly");
            ClientSpriteRegistryCallback.event(SpriteAtlasTexture.BLOCK_ATLAS_TEX).register((atlasTexture,registry)->
            {
                registry.register(id);
            });
        }
        

    }

    public void registerExtraTextures(Identifier... i)
    {

    }

    public void registerTexture()
    {

    }

    public static void init()
    {

    }
}