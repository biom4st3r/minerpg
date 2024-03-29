package com.biom4st3r.minerpg.networking;

import java.util.Random;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.RPGAbilityComponent;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.mixin_interfaces.BasicInventoryHelper;
import com.biom4st3r.minerpg.mixin_interfaces.HudExpDisplayer;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.particles.RpgDamageEffect;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.inventory.BasicInventory;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public final class Packets
{
    public static final Identifier SEND_STAT_UPDATE = new Identifier(MineRPG.MODID,"sndstats");
    public static final Identifier SEND_RPG_CLASS_COMPONENT = new Identifier(MineRPG.MODID, "sndrpgcomp");
    public static final Identifier SEND_ABILITY_COMPONENT = new Identifier(MineRPG.MODID,"sndabicomp");
    public static final Identifier SEND_COMPONENT_BAG = new Identifier(MineRPG.MODID,"sndcompbag");
    public static final Identifier SEND_DAMAGE_PARTICLE = new Identifier(MineRPG.MODID,"dmgprticl");
    public static final Identifier SEND_TOTAL_EXPEREINCE = new Identifier(MineRPG.MODID,"sndexp");

    public static final Identifier REQ_RPG_CLASS_COMPONENT = new Identifier(MineRPG.MODID, "reqrpgcomp");
    public static final Identifier REQ_CHANGE_STAT = new Identifier(MineRPG.MODID,"changestat");
    public static final Identifier REQ_ADD_CLASS = new Identifier(MineRPG.MODID,"addclass");
    public static final Identifier REQ_STAT_COMPONENT = new Identifier(MineRPG.MODID,"reqstatcomp");
    public static final Identifier REQ_ABILITY_COMPONENT = new Identifier(MineRPG.MODID,"reqabicomp");
    public static final Identifier REQ_ABILITY_BAR_CHANGE = new Identifier(MineRPG.MODID,"reqabarchng");
    public static final Identifier USE_ABILITY = new Identifier(MineRPG.MODID,"useabi");
    public static final Identifier USE_PASSIVE_ABILITY = new Identifier(MineRPG.MODID,"usepaabi");
    public static final Identifier REQ_COMP_BAG = new Identifier(MineRPG.MODID, "reqbag");

    @Environment(EnvType.CLIENT)
    public static void clientPacketReg()
    {
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_TOTAL_EXPEREINCE,(context,buffer)->
        {
            float newExp = buffer.readFloat();
            context.getTaskQueue().execute(()->
            {
                float oldValue = ((RPGPlayer)context.getPlayer()).getRPGClassComponent().getExperience(0);
                ((HudExpDisplayer)MinecraftClient.getInstance().inGameHud).displayExp(newExp-oldValue);
                ((RPGPlayer)context.getPlayer()).getRPGClassComponent().setExperience(0, newExp);
            });
            
        });
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_RPG_CLASS_COMPONENT, (context,buffer) ->
        {
            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            player.getRPGClassComponent().deserializeBuffer(buffer);

        });
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_STAT_UPDATE, (context,buffer) ->
        {
            RPGPlayer player = ((RPGPlayer)MinecraftClient.getInstance().player);
            player.getStatsComponent().deserializeBuffer(buffer);
        });
        ClientSidePacketRegistry.INSTANCE.register(Packets.SEND_ABILITY_COMPONENT, (context,buffer)->
        {
            RPGPlayer player = ((RPGPlayer)context.getPlayer());
            player.getRPGAbilityComponent().clone(new RPGAbilityComponent(player, buffer));
        });
        ClientSidePacketRegistry.INSTANCE.register(SEND_COMPONENT_BAG, (context,buffer)->
        {
            ((BasicInventoryHelper)((RPGPlayer)context.getPlayer()).getComponentContainer().bag).deserializeBuffer(buffer);
        });
        ClientSidePacketRegistry.INSTANCE.register(SEND_DAMAGE_PARTICLE, (context,buff)->
        {
            BlockPos pos = buff.readBlockPos();
            float damage = buff.readFloat();
            String s = ""+(int)Math.floor(damage);
            //Biow0rks.debug("Damage: " + damage);
            float red,green,blue;
            Random rand = context.getPlayer().getRand(); 
            float velocityX = (rand.nextFloat()-0.5f)*0.1f; 
            float velocityY = 0.054f;
            float velocityZ = (rand.nextFloat()-0.5f)*.1f;
            red = rand.nextFloat(); green = rand.nextFloat(); blue = rand.nextFloat();

            for(int i = 0; i < s.length(); i++)
            {
                context.getPlayer().getEntityWorld().addParticle(new RpgDamageEffect(new Integer(s.charAt(i)+""),red,green,blue,velocityX,velocityY,velocityZ), true, 
                pos.getX(),// + (this.random.nextFloat()-0.5f), 
                pos.getY()+(0.38f*(s.length()-i)),//this.y+this.getStandingEyeHeight() + this.random.nextFloat(), 
                pos.getZ(),//this.z + (this.random.nextFloat()-0.5f),
                0,0,0);
            }
        });
    }

    public static void serverPacketReg()
    {
        ServerSidePacketRegistry.INSTANCE.register(REQ_ABILITY_COMPONENT, (context,buffer) ->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getNetworkHandlerS().sendPacket(SERVER.sendAbilityComponent(player));
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_RPG_CLASS_COMPONENT, (context,buffer) ->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getNetworkHandlerS().sendPacket(SERVER.sendRPGClassComponent(player));
        });
        ServerSidePacketRegistry.INSTANCE.register(MineRPG.COMPONENT_BAG_ID, (context,buffer) ->
        {
            //((ServerPlayerEntity)context.getPlayer()).openContainer(((RPGPlayer)context.getPlayer()).getComponentContainer());
            ContainerProviderRegistry.INSTANCE.openContainer(MineRPG.COMPONENT_BAG_ID, context.getPlayer(), (blockpos)->{});
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_CHANGE_STAT, (context,buffer) ->
        {
            RPGStatsComponent rpgClientc = new RPGStatsComponent();
            rpgClientc.deserializeBuffer(buffer);
            context.getTaskQueue().execute(()->
            {
                ((RPGPlayer)context.getPlayer()).getStatsComponent().clientRequestChanges(rpgClientc);
            });
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_ADD_CLASS, (context,buffer) ->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getRPGClassComponent().addRpgClass(RPG_Registry.CLASS_REGISTRY.get(buffer.readIdentifier()));
            player.getNetworkHandlerS().sendPacket(SERVER.sendRPGClassComponent(player));
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_STAT_COMPONENT, (context,buffer) ->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getNetworkHandlerS().sendPacket(SERVER.sendStats(player));
        });
        ServerSidePacketRegistry.INSTANCE.register(REQ_ABILITY_BAR_CHANGE, (context,buff)->
        {
            //NetworkThreadUtils.forceMainThread(null, ((RPGPlayer)context.getPlayer()).getNetworkHandlerS(), ((ServerPlayerEntity)context.getPlayer()).getServerWorld());
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            int barIndex = buff.readByte();
            Identifier id = buff.readIdentifier();
            RPGAbility rac = RPG_Registry.ABILITY_REGISTRY.get(id);
            context.getTaskQueue().execute(()->
            {
                System.out.println(rac.toString());
                if(rac.isNone())
                {
                    player.getRPGAbilityComponent().abilityBar.set(barIndex, RpgAbilities.NONE);
                    Biow0rks.debug(String.format("reset slot %s", barIndex));
                    return;
                }
                else
                {
                    if(player.getRPGAbilityComponent().getAbilities().contains(rac))
                    {
                        player.getRPGAbilityComponent().abilityBar.set(barIndex, rac);
                        Biow0rks.debug(String.format("set slot %s to %s", barIndex,rac.id.toString()));
                        return;
                    }
                }
                System.out.println(String.format("Warning: %s failed 1 or more checks while trying to add ability to bar", context.getPlayer().getDisplayName().asFormattedString()));
                player.getNetworkHandlerS().sendPacket(Packets.SERVER.sendAbilityComponent(player));
            });
        });
        ServerSidePacketRegistry.INSTANCE.register(USE_ABILITY, (context,buff)->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            int barIndex = buff.readByte();
            context.getTaskQueue().execute(()->
            {
                RPGAbility ability = player.getRPGAbilityComponent().abilityBar.get(barIndex);
                if(!ability.isNone())
                {
                    ability.doAbility(player);
                }
            });
        });
        // ServerSidePacketRegistry.INSTANCE.register(USE_PASSIVE_ABILITY, (context,buff) ->
        // {
        //     int classIndex = buff.readByte();
        //     RPGPlayer player = (RPGPlayer)context.getPlayer();
        //     RPGAbility ability = player.getRPGClassComponent().getAvalibleAbilities()[classIndex];
        //     if(ability.getType() == Type.PASSIVE)
        //     {
        //         Biow0rks.debug("success");
        //         ability.doAbility(player);
        //     }
        // });
        ServerSidePacketRegistry.INSTANCE.register(REQ_COMP_BAG, (context,buff)->
        {
            RPGPlayer player = ((RPGPlayer)context.getPlayer());
            player.getNetworkHandlerS().sendPacket(SERVER.sendComponentBag(player));
        });
    }
    
    @Environment(EnvType.CLIENT)
    public static class CLIENT
    {
        public static CustomPayloadC2SPacket openComponentBag()
        {
            return new CustomPayloadC2SPacket(MineRPG.COMPONENT_BAG_ID,new PacketByteBuf(Unpooled.buffer()));
        }

        public static CustomPayloadC2SPacket requestRpgClassComponent()
        {
            return new CustomPayloadC2SPacket(REQ_RPG_CLASS_COMPONENT,new PacketByteBuf(Unpooled.buffer()));
        }

        public static CustomPayloadC2SPacket requestStatComp()
        {
            return new CustomPayloadC2SPacket(REQ_STAT_COMPONENT,new PacketByteBuf(Unpooled.buffer()));
        }

        public static CustomPayloadC2SPacket requestAbilityComp()
        {
            return new CustomPayloadC2SPacket(REQ_ABILITY_COMPONENT,new PacketByteBuf(Unpooled.buffer()));
        }
    
        public static CustomPayloadC2SPacket statChange(RPGStatsComponent statC)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            statC.serializeBuffer(pbb);
            return new CustomPayloadC2SPacket(REQ_CHANGE_STAT, pbb);
        }
    
        public static CustomPayloadC2SPacket addClass(RPGClass rpgClass)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeIdentifier(rpgClass.id);
            return new CustomPayloadC2SPacket(REQ_ADD_CLASS,pbb);
        }

        public static CustomPayloadC2SPacket reqChangeAbilityBar(int barIndex,RPGAbility ability)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeByte(barIndex);
            pbb.writeIdentifier(ability.id);
            return new CustomPayloadC2SPacket(REQ_ABILITY_BAR_CHANGE,pbb);
        }

        public static CustomPayloadC2SPacket useAbility(int barIndex)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeByte(barIndex);
            return new CustomPayloadC2SPacket(USE_ABILITY,pbb);
        }

        public static CustomPayloadC2SPacket requestComponentBag()
        {
            return new CustomPayloadC2SPacket(REQ_COMP_BAG,new PacketByteBuf(Unpooled.buffer()));
        }
    }

    //@Environment(EnvType.SERVER)
    public static class SERVER
    {
        public static CustomPayloadS2CPacket sendExperience(float currentTotal)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeFloat(currentTotal);
            return new CustomPayloadS2CPacket(SEND_TOTAL_EXPEREINCE, pbb);
        }

        public static CustomPayloadS2CPacket sendDamageParticle(BlockPos pos, float damage)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeBlockPos(pos);
            pbb.writeFloat(damage);
            return new CustomPayloadS2CPacket(SEND_DAMAGE_PARTICLE, pbb);
        }

        public static CustomPayloadS2CPacket sendStats(RPGPlayer player)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            player.getStatsComponent().serializeBuffer(pbb);
        
            return new CustomPayloadS2CPacket(Packets.SEND_STAT_UPDATE,pbb);
        }
    
        public static CustomPayloadS2CPacket sendRPGClassComponent(RPGPlayer player)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            player.getRPGClassComponent().serializeBuffer(pbb);
            return new CustomPayloadS2CPacket(Packets.SEND_RPG_CLASS_COMPONENT,pbb);
        }
        public static CustomPayloadS2CPacket sendAbilityComponent(RPGPlayer player)
        {
            PacketByteBuf pbb =  new PacketByteBuf(Unpooled.buffer());
            player.getRPGAbilityComponent().serializeBuffer(pbb);
            return new CustomPayloadS2CPacket(Packets.SEND_ABILITY_COMPONENT, pbb);
        }
        public static CustomPayloadS2CPacket sendComponentBag(RPGPlayer player)
        {
            PacketByteBuf pbb =  new PacketByteBuf(Unpooled.buffer());
            BasicInventory bag = player.getComponentContainer().bag;
            ((BasicInventoryHelper)bag).serializeBuffer(pbb);
            return new CustomPayloadS2CPacket(SEND_COMPONENT_BAG, pbb);
        }
    }
}