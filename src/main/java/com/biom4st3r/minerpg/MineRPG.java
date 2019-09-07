package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.items.ItemReg;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.MinerpgStatusEffect;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    //private static final String COMPONENT_BAG = "componentbag";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, "componentbag");

    @SuppressWarnings({"unchecked"})
    public static final EntityType<Fireball> FIREBALL = (EntityType)Registry.register(
        Registry.ENTITY_TYPE, new Identifier(MODID,"fireball"), 
        FabricEntityTypeBuilder.create(EntityCategory.MISC, (type,world) ->
        {
            return new Fireball(world,(EntityType)type);
        }).build());
    
    StatusEffect PAPER_SKIN = MinerpgStatusEffect.create(StatusEffectType.HARMFUL, 0x00FF00);
    @Override
    public void onInitialize() 
    {
        Util.debug(PAPER_SKIN.getColor());
        Registry.register(Registry.STATUS_EFFECT, new Identifier(MineRPG.MODID,"paper_skin"), PAPER_SKIN);
        //minerpg:textures/mob_effect/paper_skin.png
        //effect.minerpg.paper_skin
        RpgAbilities.init();
        RpgClasses.init();
        ItemReg.init();
        Packets.serverPacketReg();
        //DamageUtil
        //LivingEntity
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                buf.writeBlockPos(player.getBlockPos());
                return ((RPGPlayer)player).getComponentContainer();
            }
        );
    }

}