package com.biom4st3r.minerpg.util;

import java.lang.reflect.Array;

import com.biom4st3r.minerpg.MineRPG;
import com.biom4st3r.minerpg.api.RPGAbility;
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
    public static void debug(Object o)
    {
        if(isTestBuild)
        {
            System.out.println(o.toString());
        }
    }
    public static void debugV(Object o, int depth)
    {
        if(isTestBuild)
        {
            StackTraceElement[] ste = new Exception().getStackTrace();
            for(int i = 1; i <= depth; i++)
            {
                debug(ste[i].getClassName() + "." + ste[i].getMethodName());
            }
            debug(o);
        }
    }

    public static void errorMSG(Object o)
    {
        System.out.println(MineRPG.MODID + ": " + o.toString());
    }

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
}