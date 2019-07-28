package com.biom4st3r.minerpg.util;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.api.RPGClass;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RpgAbilityContext
{
    public static final String CONTEXT = "cntxt";

    public static final String RPG_CLASS = "rpgclss";
    public static final String RPG_CLASS_LVL = "clsslvl";
    public static final String ABILITY_INDEX = "indx";
    public static final String RPG_ABILITY = "abli";
    public static final RpgAbilityContext EMPTY = new RpgAbilityContext(RpgClassContext.EMPTY,-1,RpgAbilities.NONE);
    //RPGClass sourceClass;
    //int classLvl;
    public RpgClassContext classContext;
    public int abilityIndexInClass;
    public RPGAbility ability;

    public RpgAbilityContext(RpgClassContext classContext, int abilityIndexInClass, RPGAbility ability)
    {
        this.classContext = classContext;
        this.abilityIndexInClass = abilityIndexInClass;
        this.ability = ability;
    }
    public RpgAbilityContext(RPGClass rpgClass, int classLvl,int abilityIndexInClass, RPGAbility ability)
    {
        this.classContext = new RpgClassContext(rpgClass, classLvl);
        this.abilityIndexInClass = abilityIndexInClass;
        this.ability = ability;
    }
    public void serializeNbt(CompoundTag tag)
    {
        CompoundTag ct = new CompoundTag();
        ct.putString(RPG_CLASS, classContext.rpgclass.name.toString());
        ct.putByte(RPG_CLASS_LVL, (byte)classContext.Lvl);
        ct.putByte(ABILITY_INDEX, (byte)abilityIndexInClass);
        ct.putString(RPG_ABILITY, ability.name.toString());
        tag.put(CONTEXT, ct);

    }
    public void deserializeNbt(CompoundTag tag)
    {
        CompoundTag ct = tag.getCompound(CONTEXT);
        this.classContext = new RpgClassContext(RPG_Registry.CLASS_REGISTRY.get(new Identifier(ct.getString(RPG_CLASS))), ct.getByte(RPG_CLASS_LVL));
        this.abilityIndexInClass = ct.getByte(ABILITY_INDEX);
        this.ability = RPG_Registry.ABILITY_REGISTRY.get(new Identifier(ct.getString(RPG_ABILITY)));
    }
    public void serializeBuffer(PacketByteBuf pbb)
    {

    }
    public void deserializebuffer(PacketByteBuf pbb)
    {

    }
}