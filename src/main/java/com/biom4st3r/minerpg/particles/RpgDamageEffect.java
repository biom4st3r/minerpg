package com.biom4st3r.minerpg.particles;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class RpgDamageEffect implements ParticleEffect
{
    private static final Factory<RpgDamageEffect> FACTORY = new Factory<RpgDamageEffect>() {
        public RpgDamageEffect read(ParticleType<RpgDamageEffect> pt, StringReader str) throws CommandSyntaxException 
        {
            str.expect(' '); 
            return new RpgDamageEffect(10,0,0,0,0,0,0);
        }
        public RpgDamageEffect read(ParticleType<RpgDamageEffect> pt, PacketByteBuf buf) {
            return new RpgDamageEffect(10,0,0,0,0,0,0); 
        }
    };

    public static final ParticleType<RpgDamageEffect> TYPE =
        new ParticleType<RpgDamageEffect>(false, FACTORY) {};

    public RpgDamageEffect(float value, float red, float green, float blue, float xv, float yv, float zv) {
        this.value = value;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.xv = xv; this.yv = yv; this.zv = zv;
    }
    private final float value;
    public final float red,green,blue;
    public final float xv,yv,zv;

    public float getValue()
    {
        return value;
    }

    public ParticleType<RpgDamageEffect> getType() { 
        return TYPE;
    }

    public void write(PacketByteBuf buf) { 
        buf.writeFloat(value);
    }

    public String asString() { 
        return String.format("%s %s", Registry.PARTICLE_TYPE.getId(TYPE), this.value); 
    }
}

