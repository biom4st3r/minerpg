package com.biom4st3r.minerpg.networking;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.components.RPGStatsComponent;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.biom4st3r.minerpg.registery.RpgClasses;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.RpgAbilityContext;
import com.biom4st3r.minerpg.util.RpgClassContext;
import com.biom4st3r.minerpg.util.Util;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class Packets
{
    public static final Identifier SEND_STAT_UPDATE = new Identifier(MineRPG.MODID,"sndstats");
    public static final Identifier SEND_RPG_CLASS_COMPONENT = new Identifier(MineRPG.MODID, "sndrpgcomp");
    public static final Identifier SEND_ABILITY_COMPONENT = new Identifier(MineRPG.MODID,"sndabicomp");

    public static final Identifier REQ_RPG_CLASS_COMPONENT = new Identifier(MineRPG.MODID, "reqrpgcomp");
    public static final Identifier REQ_CHANGE_STAT = new Identifier(MineRPG.MODID,"changestat");
    public static final Identifier REQ_ADD_CLASS = new Identifier(MineRPG.MODID,"addclass");
    public static final Identifier REQ_STAT_COMPONENT = new Identifier(MineRPG.MODID,"reqstatcomp");
    public static final Identifier REQ_ABILITY_COMPONENT = new Identifier(MineRPG.MODID,"reqabicomp");
    public static final Identifier REQ_ABILITY_BAR_CHANGE = new Identifier(MineRPG.MODID,"reqabarchng");
    public static final Identifier USE_ABILITY = new Identifier(MineRPG.MODID,"useabi");

    @Environment(EnvType.CLIENT)
    public static void clientPacketReg()
    {
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
            player.getRPGAbilityComponent().deserializeBuffer(buffer);
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
            ((RPGPlayer)context.getPlayer()).getStatsComponent().clientRequestChanges(rpgClientc);

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
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            int barIndex = buff.readByte();
            RpgAbilityContext rac = new RpgAbilityContext(buff);
            if(rac.ability == RpgAbilities.NONE)
            {
                player.getRPGAbilityComponent().abilityBar.set(barIndex, RpgAbilityContext.EMPTY);
                Util.debug(String.format("reset slot %s", barIndex));
                return;
            }
            if(rac.classContext.rpgclass == RpgClasses.NONE)
            {
                if(player.getRPGAbilityComponent().specialAbilities.contains(rac.ability))
                {
                    player.getRPGAbilityComponent().abilityBar.set(barIndex, rac);
                    Util.debug(String.format("specialSet slot %s to %s", barIndex,rac.ability.name.toString()));
                    return;
                }
            }
            else
            {
                RpgClassContext player_rcc = player.getRPGClassComponent().getRpgClassContext(rac.classContext.rpgclass);
                if(player_rcc != RpgClassContext.EMPTY && player_rcc.Lvl == rac.classContext.Lvl)
                {
                    if(rac.classContext.getAbilities()[rac.abilityIndexInClass] == rac.ability)
                    {
                        player.getRPGAbilityComponent().abilityBar.set(barIndex, rac);
                        Util.debug(String.format("set slot %s to %s", barIndex,rac.ability.name.toString()));
                        return;
                    }
                }
            }
            System.out.println(String.format("Warning: %s failed 1 or more checks while trying to add ability to bar", context.getPlayer().getDisplayName().asFormattedString()));
            
        });
        ServerSidePacketRegistry.INSTANCE.register(USE_ABILITY, (context,buff)->
        {
            RPGPlayer player = (RPGPlayer)context.getPlayer();
            player.getRPGAbilityComponent().abilityBar.get(buff.readByte()).ability.doAbility(player);
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
            pbb.writeIdentifier(rpgClass.name);
            return new CustomPayloadC2SPacket(REQ_ADD_CLASS,pbb);
        }

        public static CustomPayloadC2SPacket reqChangeAbilityBar(int barIndex,RpgAbilityContext rac)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeByte(barIndex);
            rac.serializeBuffer(pbb);
            return new CustomPayloadC2SPacket(REQ_ABILITY_BAR_CHANGE,pbb);
        }

        public static CustomPayloadC2SPacket useAbility(int barIndex)
        {
            PacketByteBuf pbb = new PacketByteBuf(Unpooled.buffer());
            pbb.writeByte(barIndex);
            return new CustomPayloadC2SPacket(USE_ABILITY,pbb);
        }

    }

    //@Environment(EnvType.SERVER)
    public static class SERVER
    {
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
    }

}