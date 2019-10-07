package com.biom4st3r.minerpg.util;

import java.lang.reflect.Array;

import com.biom4st3r.biow0rks.Biow0rks;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class Util
{

    //public static Logger logger = LogManager.getLogger();

    public static double getAttackDamage(ItemStack stack){
        return stack.getItem().getModifiers(EquipmentSlot.MAINHAND)
            .get(EntityAttributes.ATTACK_DAMAGE.getId())
            .stream()
            .mapToDouble(EntityAttributeModifier::getAmount)
            .sum();
    }


    public static void sendAllComponents(RPGPlayer player)
    {
        ServerPlayNetworkHandler networkhandler = player.getNetworkHandlerS();
        networkhandler.sendPacket(Packets.SERVER.sendAbilityComponent(player));
        networkhandler.sendPacket(Packets.SERVER.sendRPGClassComponent(player));
        networkhandler.sendPacket(Packets.SERVER.sendStats(player));
        networkhandler.sendPacket(Packets.SERVER.sendComponentBag(player));
    }

    public static void requestAllComponents(ClientPlayNetworkHandler networkhandler)
    {
        Biow0rks.debug("All Components Requested");
        networkhandler.sendPacket(Packets.CLIENT.requestRpgClassComponent());
        networkhandler.sendPacket(Packets.CLIENT.requestStatComp());
        networkhandler.sendPacket(Packets.CLIENT.requestAbilityComp());
        networkhandler.sendPacket(Packets.CLIENT.requestComponentBag());
    }
    
    public static CompoundTag ShortItemStackToTag(ItemStack iS, CompoundTag tag) {
        Identifier identifier_1 = Registry.ITEM.getId(iS.getItem());
        tag.putString("id", identifier_1 == null ? "minecraft:air" : identifier_1.toString());
        tag.putShort("Count", (short)iS.getCount());

        if (iS.getTag() != null) {
            tag.put("tag", iS.getTag());
        }

        return tag;
    }

    public static void ShortitemStackToBuffer(ItemStack iS, PacketByteBuf pbb)
    {
        pbb.writeInt(Item.getRawId(iS.getItem()));
        pbb.writeShort(iS.getCount());
    }

    public static ItemStack BufferToShortItemStack(PacketByteBuf pbb)
    {
        return new ItemStack(Item.byRawId(pbb.readInt()),pbb.readShort());
    }

    public static ItemStack TagToShortItemStack(CompoundTag tag)
    {
        Item i = Registry.ITEM.get(new Identifier( tag.getString("id")));
        int count = 0;
        count = tag.getShort("Count");
        return count > 0 ? new ItemStack(i,count) : ItemStack.EMPTY;
    }
    static boolean  isTestBuild = true;
    // public static void debug(String string, Object... args)
    // {   
    //     if(isTestBuild)
    //     {
    //         logger.debug(String.format(string, args));
    //     }
    // }
    // public static void debugV(int depth, String string, Object... args)
    // {
    //     if(isTestBuild)
    //     {
    //         StackTraceElement[] ste = new Exception().getStackTrace();
    //         for(int i = 1; i <= depth; i++)
    //         {
    //             debug(ste[i].getClassName() + "." + ste[i].getMethodName());
    //         }
    //         debug(string, args);
    //     }
    // }

    // public static void errorMSG(String string,Object... args)
    // {
    //     logger.error(String.format(string, args));
    // }

    public static <T extends Object> T[] reverse(T[] array)
    {
        @SuppressWarnings("unchecked")
        T[] reversed = (T[])Array.newInstance(array[0].getClass(), array.length);
        for(int i = 0; i < array.length; i++)
        {
            reversed[i] = array[(array.length-1)-i];
        }
        return reversed;
    }

    public static RPGAbility[] reverse(RPGAbility[] array)// Required because ^ kept casting to a PotionAbility instead of ability
    {
        RPGAbility[] reversed = new RPGAbility[array.length];
        for(int i = 0; i < array.length; i++)
        {
            reversed[i] = array[(array.length-1)-i];
        }
        return reversed;
    }

    // public static <V> ListTag NBTSerializable_MapToListTag(Map<String,V> map)
    // {
    //     ListTag lt = new ListTag();
    //     CompoundTag ct;
    //     Boolean isSerializable = false;
    //     for(String key : map.keySet())
    //     {
    //         //Please ignore the ugly
    //         if(!isSerializable)
    //             isSerializable = map.get(key) instanceof NbtSerializable;
    //         if(!isSerializable)
    //             return null;
    //         ct = new CompoundTag();
    //         ((NbtSerializable)map.get(key)).serializeNBT(ct);
    //         lt.add(new CompoundTag().put(key, ct));
    //     }
    //     return lt;
    // }

    // public static <V> Map<String,V> NBTSerializable_ListTagToMap(ListTag lt)
    // {
    //     Map<String,V> map = Maps.newHashMap();
    //     for(int i = 0; i < lt.size(); i++)
        
    // }

    public static float dndLevelingAlgo(int lvl)
    {
        if(lvl == 1) return 0;
        return (float)((3.7825f*Math.pow(lvl, 4))-(134.59*Math.pow(lvl, 3))+(2572.6*Math.pow(lvl, 2))-(10699*lvl)+10703);
    }

    public static float plagurmonLevelingAlgoFast(int lvl)
    {
        if(lvl == 1) return 0;
        return (float)(Math.pow(lvl, 3)*4)/5;
    }

    public static float plagurmonLevelingAlgoMedFast(int lvl)
    {
        if(lvl == 1) return 0;
        return (float)Math.pow(lvl, 3);
    }

    public static float plagurmonLevelingAlgoMedSlow(int lvl)
    {
        if(lvl == 1) return 0;
        return (float)((6f/5f)*Math.pow(lvl, 3)-(15f*Math.pow(lvl, 2))+(100f*lvl)-140);
    }

    public static float plagurmonLevelingAlgoSlow(int lvl)
    {
        if(lvl == 1) return 0;
        return (float)(Math.pow(lvl, 3)*5)/4f;
    }
}