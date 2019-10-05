package com.biom4st3r.minerpg.components;

import java.util.List;
import java.util.Map;

import com.biom4st3r.minerpg.api.BufferSerializable;
import com.biom4st3r.minerpg.api.IComponent;
import com.biom4st3r.minerpg.api.NbtSerializable;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements IComponent,NbtSerializable,BufferSerializable {

    protected List<RPGAbility> abilities = Lists.newArrayList();
    public DefaultedList<RPGAbility> abilityBar;
    protected Map<Identifier,Integer> cooldowns; 

    //protected Map<Identifier,Integer> tokens;
    
    RPGPlayer owner;

    public List<RPGAbility> getAbilities()
    {
        return this.abilities;
    }

    @Override
    public <T extends IComponent> void clone(T origin) {
        RPGAbilityComponent original = (RPGAbilityComponent)origin;
        for(RPGAbility a : original.abilities)
        {
            this.abilities.add(a);
        }
        for(int i = 0; i < 9; i++)
        {
            this.abilityBar.set(i, original.abilityBar.get(i));
        }
        this.cooldowns = Maps.newHashMap();
        for(Identifier i : cooldowns.keySet())
        {
            this.cooldowns.put(i, original.cooldowns.get(i));
        }
        // this.tokens = Maps.newHashMap();
        // for(Identifier i : tokens.keySet())
        // {
        //     this.tokens.put(i, original.tokens.get(i));
        // }
    }

    public void tick()
    {
        if(this.cooldowns.size() != 0)
        {
            for(Identifier i : this.cooldowns.keySet())
            {
                this.cooldowns.replace(i, this.cooldowns.get(i)-1);
            }
        }
    }

    public RPGAbilityComponent(RPGPlayer player)
    {
        abilities = Lists.newArrayList();
        abilityBar = DefaultedList.ofSize(9, RpgAbilities.NONE);
        cooldowns = Maps.newHashMap();
        // tokens = Maps.newHashMap();

        this.owner = player;
    }
    public RPGAbilityComponent(RPGPlayer player, PacketByteBuf pbb)
    {
        this(player);
        this.deserializeBuffer(pbb);
    }

    // public boolean hasToken(RPGAbility ability)
    // {
    //     return hasToken(ability.id);    
    // }

    // public boolean hasToken(Identifier i)
    // {
    //     return tokens.containsKey(i);
    // }

    // public void putToken(RPGPlayer player, RPGAbility ability)
    // {
    //     putToken(player, ability.id,ability.getMaxTokens(player));
    // }

    // public void putToken(RPGPlayer player, Identifier i,int quantity)
    // {
    //     if(!hasToken(i))
    //     {
    //         tokens.put(i, quantity);
    //     }
    //     else
    //     {
    //         int temp = tokens.get(i).intValue()+quantity;
    //         tokens.replace(i, temp);
 
    //     }
    // }

    // public boolean subtractToken(RPGAbility ability, int quantity)
    // {
    //     return subtractToken(ability.id, quantity);
    // }

    // public boolean subtractToken(Identifier i, int quantity)
    // {
    //     if(hasToken(i) && tokens.get(i).intValue() >= quantity)
    //     {
    //         int temp = tokens.get(i).intValue()-quantity;
    //         tokens.replace(i, temp);
    //         return true;
    //     }
    //     return false;
    // }

    public void addCooldown(RPGAbility ability)
    {
        addCooldown(ability.id, ability.getCoolDown());
    }

    public void addCooldown(Identifier i, int coolDown)
    {
        cooldowns.put(i,coolDown);
    }

    public boolean isOnCooldown(Identifier i)
    {
        return cooldowns.containsKey(i);
    }

    public boolean isOnCooldown(RPGAbility rpga)
    {
        return isOnCooldown(rpga.id);
    }

    public static final String 
        ABILITY_LIST = "rpgAbilList",
        //ABILITY_LIST_SIZE = "rpgAbLSize",
        SLOT_ID = "slotid",
        ABILITY_ID = "abilid",
        ABILITY_BAR = "abilibar",
        TOKEN_LIST = "toknlist",
        TOKEN_NAME = "toknnm",
        TOKEN_AMOUNT = "toknamt",
        COOLDOWN_LIST = "cooldowns",
        COOLDOWN_NAME = "cdnm",
        COOLDOWN_DURATION = "cdtm";


    private static RPGAbility getAbility(Identifier i)
    {
        return RPG_Registry.ABILITY_REGISTRY.get(i);
    }

    @Override
    public void serializeNBT(CompoundTag tag) {
        ListTag lt;

        lt = new ListTag();
        for(int i = 0; i < abilities.size();i++)
        { // abilities
            lt.add(new StringTag(abilities.get(i).id.toString()));
        }
        tag.put(ABILITY_LIST, lt);

        lt = new ListTag();
        for(int i = 0; i < 9; i++)
        {// Ability Hotbar
            RPGAbility ability = abilityBar.get(i);
            lt.add(new StringTag(ability.id.toString()));   
        }
        tag.put(ABILITY_BAR, lt);
        //lt = new ListTag();
        // Tokens
        // for(Identifier i : tokens.keySet())
        // {
        //     CompoundTag ct = new CompoundTag();
        //     ct.putString(TOKEN_NAME, i.toString());
        //     ct.putInt(TOKEN_AMOUNT, tokens.get(i));
        //     lt.add(ct);
        // }
        // tag.put(TOKEN_LIST, lt);
        lt = new ListTag();
        //Cooldowns
        for(Identifier i : cooldowns.keySet())
        {
            CompoundTag ct = new CompoundTag();
            ct.putString(COOLDOWN_NAME,i.toString());
            ct.putInt(COOLDOWN_DURATION, cooldowns.get(i));
            lt.add(ct);
        }
        tag.put(COOLDOWN_LIST,lt);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void deserializeNBT(CompoundTag tag) {
        ListTag lt;
        int i;

        lt = tag.getList(ABILITY_LIST,10);
        for(i = 0; i < lt.size(); i++)
        {
            this.abilities.add(getAbility(new Identifier(lt.getString(i))));
        }
        lt = tag.getList(ABILITY_BAR, 10);
        for(i = 0; i < 9 && lt.size() > 0 ; i++)
        {
            RPGAbility ability = RPG_Registry.ABILITY_REGISTRY.get(new Identifier(lt.getString(i)));
            this.abilityBar.set(i, ability);
        }
        // lt = tag.getList(TOKEN_LIST, 10);
        // for(i = 0; i < lt.size();i++)
        // {
        //     CompoundTag ct = lt.getCompoundTag(i);
        //     tokens.put(new Identifier(ct.getString(TOKEN_NAME)), ct.getInt(TOKEN_AMOUNT));
        // }
        lt = tag.getList(COOLDOWN_LIST, 10);
        for(i = 0; i < lt.size();i++)
        {
            CompoundTag ct = lt.getCompoundTag(i);
            cooldowns.put(new Identifier(ct.getString(COOLDOWN_NAME)), ct.getInt(COOLDOWN_DURATION));
        }
    }

    @Override
    public void serializeBuffer(PacketByteBuf buf) {
        // buf.writeByte(specialAbilities.size());
        // for(RPGAbility a : specialAbilities)
        // {
        //     buf.writeString(a.id.toString());
        // }
        buf.writeShort(abilities.size());
        for(RPGAbility a : abilities)
        {
            buf.writeIdentifier(a.id);
        }
        for(RPGAbility ability : abilityBar)
        {
            buf.writeIdentifier(ability.id);
        }
        // buf.writeShort(tokens.size());
        // for(Identifier i : tokens.keySet())
        // {
        //     buf.writeIdentifier(i);
        //     buf.writeInt(tokens.get(i));
        // }
        buf.writeShort(cooldowns.size());
        for(Identifier i : cooldowns.keySet())
        {
            buf.writeIdentifier(i);
            buf.writeInt(cooldowns.get(i));
        }        
    }

    @Override
    public void deserializeBuffer(PacketByteBuf buf) {
        int size = buf.readShort();
        for(int i = 0; i< size; i++)
        {
            abilities.add(getAbility(buf.readIdentifier()));
        }
        for(int i = 0; i < 9; i++)
        {
            abilityBar.set(i, RPG_Registry.ABILITY_REGISTRY.get(buf.readIdentifier()));
        }
        // size = buf.readShort();
        // for(int i = 0; i < size; i++)
        // {
        //     tokens.put(buf.readIdentifier(), buf.readInt());
        // }
        size = buf.readShort();
        for(int i = 0; i < size; i++)
        {
            cooldowns.put(buf.readIdentifier(), buf.readInt());
        }
    }
    @SuppressWarnings("unchecked")
    @Override
    public RPGAbilityComponent getCopy() {
        RPGAbilityComponent newrac = new RPGAbilityComponent(this.owner);
        newrac.clone(this);
        return newrac;
    }

}