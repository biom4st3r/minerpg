package com.biom4st3r.minerpg;

import com.biom4st3r.biow0rks.PotionEffectHelper;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGClassComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.items.ItemReg;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.particles.RpgDamageEffect;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.Util;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.impl.registry.CommandRegistryImpl;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, "componentbag");

    public static final StatusEffect PAPER_SKIN = PotionEffectHelper.createAndRegisterEffect(new Identifier(MODID,"paper_skin"), StatusEffectType.HARMFUL, 0xFFFFFF);

    @SuppressWarnings({"unchecked"})
    public static final EntityType<Fireball> FIREBALL = (EntityType<Fireball>)(Object)Registry.register(
        Registry.ENTITY_TYPE, new Identifier(MODID,"fireball"), 
        FabricEntityTypeBuilder.create(EntityCategory.MISC, (type,world) ->
        {
            return new Fireball((EntityType<Fireball>)(Object)type,world);
        }).build());

    @Override
    public void onInitialize() 
    {
        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MineRPG.MODID,"rpg_damage"), RpgDamageEffect.TYPE);
        RpgAbilities.init();
        RpgClasses.init();
        ItemReg.init();
        Packets.serverPacketReg();
        
        ContainerProviderRegistry.INSTANCE.registerFactory(
            COMPONENT_BAG_ID,
            (int syncId, Identifier identifier, PlayerEntity player, PacketByteBuf buf) ->
            {
                buf.writeBlockPos(player.getBlockPos());
                return ((RPGPlayer)player).getComponentContainer();
            }
        );
        CommandRegistryImpl.INSTANCE.register(false, serverCommandSourceCommandDispatcher ->
            serverCommandSourceCommandDispatcher.register(
                CommandManager.literal("rpgreset").executes(command ->
                {
                    RPGPlayer player = ((RPGPlayer)command.getSource().getPlayer());
                    player.getRPGAbilityComponent().clone(new RPGAbilityComponent(player));
                    player.getRPGClassComponent().clone(new RPGClassComponent(player));
                    player.getStatsComponent().clone(new RPGStatsComponent());;
                    Util.sendAllComponents(player);
                    return 0;
                })
            )
        );
        CommandRegistryImpl.INSTANCE.register(false, serverCommandSource -> serverCommandSource.register(
            CommandManager.literal("list_ability").executes(command->
            {
                PlayerEntity pe = command.getSource().getPlayer();
                RPG_Registry.ABILITY_REGISTRY.stream().forEach((ability)->
                {
                    pe.sendMessage(new LiteralText(ability.toString()));
                });
                return 0;

            })
        ));
        CommandRegistryImpl.INSTANCE.register(false, serverCommandSource -> serverCommandSource.register(
            CommandManager.literal("list_classes").executes(command ->
        {
            PlayerEntity pe = command.getSource().getPlayer();
            RPG_Registry.CLASS_REGISTRY.stream().forEach(rpgclass ->
            {
                pe.sendMessage(new LiteralText(rpgclass.toString()));
            });
            return 0;
        })));
        //SetBlockCommand
    }
}
