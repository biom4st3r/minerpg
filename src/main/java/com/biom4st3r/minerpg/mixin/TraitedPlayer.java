package com.biom4st3r.minerpg.mixin;

import com.biom4st3r.biow0rks.Mxn;
import com.biom4st3r.minerpg.api.RPGTrait;
import com.biom4st3r.minerpg.api.TraitCatagory;
import com.biom4st3r.minerpg.components.RPGTraitComponent;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class TraitedPlayer extends LivingEntity {

    protected TraitedPlayer(EntityType<? extends LivingEntity> entityType_1, World world_1) {
        super(entityType_1, world_1);
    }

    RPGTraitComponent traitComponent;
    RPGPlayer rpgplayer;

    @Inject(at = @At(Mxn.At.BeforeReturn), method = "<init>")
    public void init(CallbackInfo ci)
    {
        this.rpgplayer = (RPGPlayer)this;
        this.traitComponent = rpgplayer.getRPGTraitComponent();
    }
    
    @Override
    public int getArmor() {
        RPGTrait<Integer> trait;
        if(!(trait = traitComponent.isUsingTrait(TraitCatagory.ARMOR_OVERRIDE)).isNone())
        {
            return trait.output(rpgplayer);
        }
        return super.getArmor();
    }
}