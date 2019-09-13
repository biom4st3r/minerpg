package com.biom4st3r.minerpg;

import com.biom4st3r.minerpg.entities.Fireball;
import com.biom4st3r.minerpg.items.ItemReg;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.particles.RpgDamageEffect;
import com.biom4st3r.minerpg.registery.MinerpgStatusEffect;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.RPGPlayer;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class MineRPG implements ModInitializer
{
    public static final String MODID = "minerpg";
    public static final Identifier COMPONENT_BAG_ID = new Identifier(MODID, "componentbag");

    @SuppressWarnings({"unchecked"})
    public static final EntityType<Fireball> FIREBALL = (EntityType)Registry.register(
        Registry.ENTITY_TYPE, new Identifier(MODID,"fireball"), 
        FabricEntityTypeBuilder.create(EntityCategory.MISC, (type,world) ->
        {
            return new Fireball(world,(EntityType)type);
        }).build());


    //ParticleTypes
    //DustParticleEffect
    //MobSpawnerBlockEntity
    //ClientWorld
    //BlockStateParticleEffect
    //ParticleManager
    // NoteParticle.Factory

    @Override
    public void onInitialize() 
    {
        //ParticleTypes
        //NamespaceResourceManager
        //ReloadableResourceManagerImpl
        //ResourceTexture.TextureData
        //ParticleTypes
        //EnchantFactory
        //EnchantGlyphParticle
        //Blocks
        

        Registry.register(Registry.PARTICLE_TYPE, new Identifier(MineRPG.MODID,"rpg_damage"), RpgDamageEffect.TYPE);

        MinerpgStatusEffect.init();
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
    }
    //Stats
    //Stat
}
/*
DAMAGE_RESISTED

((ServerPlayerEntity)this).increaseStat(Stats.DAMAGE_BLOCKED_BY_SHIELD, Math.round(float_3 * 10.0F));

this.incrementStat(Stats.KILLED_BY.getOrCreateStat(livingEntity_1.getType()));

  public void onKilledOther(LivingEntity livingEntity_1) {
      this.incrementStat(Stats.KILLED.getOrCreateStat(livingEntity_1.getType()));
   }


   public void incrementStat(Identifier identifier_1) {
      this.incrementStat(Stats.CUSTOM.getOrCreateStat(identifier_1));
   }

   public void increaseStat(Identifier identifier_1, int int_1) {
      this.increaseStat(Stats.CUSTOM.getOrCreateStat(identifier_1), int_1);
   }

   public void incrementStat(Stat<?> stat_1) {
      this.increaseStat((Stat)stat_1, 1);
   }

   public void increaseStat(Stat<?> stat_1, int int_1) {
   }

   @Override
   public void increaseStat(Stat<?> stat_1, int int_1) {
      this.statHandler.increaseStat(this, stat_1, int_1);
      this.getScoreboard().forEachScore(stat_1, this.getEntityName(), (scoreboardPlayerScore_1) -> {
         scoreboardPlayerScore_1.incrementScore(int_1);
      });
   }
*/