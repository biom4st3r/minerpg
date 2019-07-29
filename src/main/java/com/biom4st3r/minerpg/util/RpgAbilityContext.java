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
    //public static final String CONTEXT = "cntxt";

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
    public RpgAbilityContext(PacketByteBuf pbb)
    {
        this.classContext = new RpgClassContext(RPG_Registry.CLASS_REGISTRY.get(pbb.readIdentifier()), pbb.readByte());
        this.abilityIndexInClass = pbb.readByte();
        this.ability = RPG_Registry.ABILITY_REGISTRY.get(pbb.readIdentifier());

    }

    public void serializeNbt(CompoundTag tag)
    {
        tag.putString(RPG_CLASS, classContext.rpgclass.name.toString());
        tag.putByte(RPG_CLASS_LVL, (byte)classContext.Lvl);
        tag.putByte(ABILITY_INDEX, (byte)abilityIndexInClass);
        tag.putString(RPG_ABILITY, ability.name.toString());
        //tag.put(CONTEXT, ct);

    }
    public void deserializeNbt(CompoundTag tag)
    {
        this.classContext = new RpgClassContext(RPG_Registry.CLASS_REGISTRY.get(new Identifier(tag.getString(RPG_CLASS))), tag.getByte(RPG_CLASS_LVL));
        this.abilityIndexInClass = tag.getByte(ABILITY_INDEX);
        this.ability = RPG_Registry.ABILITY_REGISTRY.get(new Identifier(tag.getString(RPG_ABILITY)));
    }
    public void serializeBuffer(PacketByteBuf pbb)
    {
        pbb.writeIdentifier(classContext.rpgclass.name);
        pbb.writeByte((byte)classContext.Lvl);
        pbb.writeByte((byte)abilityIndexInClass);
        pbb.writeIdentifier(ability.name);
    }
    public void deserializebuffer(PacketByteBuf pbb)
    {
        this.classContext = new RpgClassContext(RPG_Registry.CLASS_REGISTRY.get( pbb.readIdentifier()),pbb.readByte());
        this.abilityIndexInClass = pbb.readByte();
        this.ability = RPG_Registry.ABILITY_REGISTRY.get(pbb.readIdentifier());
    }
}