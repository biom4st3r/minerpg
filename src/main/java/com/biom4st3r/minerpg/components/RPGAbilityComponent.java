package com.biom4st3r.minerpg.components;

import java.util.List;
import java.util.Map;

import com.biom4st3r.minerpg.api.BufferSerializable;
import com.biom4st3r.minerpg.api.IComponent;
import com.biom4st3r.minerpg.api.NbtSerializable;
import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;
import com.biom4st3r.minerpg.networking.Packets;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.registery.RpgAbilities;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements IComponent,NbtSerializable,BufferSerializable {

    protected List<RPGAbility> abilities;
    public DefaultedList<RPGAbility> abilityBar;
    protected Map<Identifier,Integer> cooldowns; 
    public boolean isDirty_abilities = false;

    //protected Map<Identifier,Integer> tokens;
    
    RPGPlayer owner;

    /**
     * 
     * @return list of abilities that the player currently has.
     */
    public List<RPGAbility> getAbilities()
    {
        return this.abilities;
    }

    /**
     * 
     * @param ability gives the player a new ability. Commonly used for {@link com.biom4st3r.minerpg.interfaces.Reward}
     */
    public void addAbility(RPGAbility ability)
    {
        this.isDirty_abilities = true;
        this.abilities.add(ability);
    }

    @Override
    public <T extends IComponent> void clone(T newData) {
        RPGAbilityComponent source = (RPGAbilityComponent)newData;
        this.init();
        for(RPGAbility a : source.abilities)
        {
            this.abilities.add(a);
        }
        for(int i = 0; i < 9; i++)
        {
            this.abilityBar.set(i, source.abilityBar.get(i));
        }
        this.cooldowns = Maps.newHashMap();
        for(Identifier i : cooldowns.keySet())
        {
            this.cooldowns.put(i, source.cooldowns.get(i));
        }
        // this.tokens = Maps.newHashMap();
        // for(Identifier i : tokens.keySet())
        // {
        //     this.tokens.put(i, original.tokens.get(i));
        // }
    }


    /** ticked with the {@link RPGPlayer#tick()} */
    @Override
    public void tick()
    {
        if(this.cooldowns.size() != 0)
        {
            for(Identifier i : this.cooldowns.keySet())
            {
                this.cooldowns.replace(i, this.cooldowns.get(i)-1);
                if(this.cooldowns.get(i) < 1){
                    this.cooldowns.remove(i);
                    break;
                }
            }
        }
        if(!this.owner.getPlayer().world.isClient)
        {
            if(isDirty_abilities)
            {
                isDirty_abilities = false;
                this.owner.getNetworkHandlerS().sendPacket(Packets.SERVER.sendAbilityComponent(owner));
            }
        }
    }

    public RPGAbilityComponent(RPGPlayer player)
    {
        this.init();
        // tokens = Maps.newHashMap();

        this.owner = player;
    }

    /** resets an {@link RPGAbilityComponent} to empty */
    public void init()
    {
        abilities = Lists.newArrayList();
        abilityBar = DefaultedList.ofSize(9, RpgAbilities.NONE);
        cooldowns = Maps.newHashMap();
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

    /**
     * 
     * @param ability ability to be added to {@link RPGAbilityComponent#cooldowns}
     */
    public void addCooldown(RPGAbility ability)
    {
        addCooldown(ability.id, ability.getCoolDownDuration());
    }

    /**
     * 
     * @param i identifier of ability to be aded to {@link RPGAbilityComponent#cooldowns}
     * @param coolDown duration of cooldown. Usually via {@link RPGAbility#getCoolDownDuration()}
     */
    public void addCooldown(Identifier i, int coolDown)
    {
        cooldowns.put(i,coolDown);
    }

    /**
     * 
     * @param id identifier of Ability to be checked
     * @return true if id in {@link RPGAbilityComponent#cooldowns}  
     */
    public boolean isOnCooldown(Identifier id)
    {
        return cooldowns.containsKey(id);
    }

    /**
     * 
     * @param rpga RPGAbility to be added to {@link RPGAbilityComponent#cooldowns}
     * @return true is rpga is in {@link RPGAbilityComponent#cooldowns}
     */
    public boolean isOnCooldown(RPGAbility rpga)
    {
        return isOnCooldown(rpga.id);
    }

    public static final String 
        ABILITY_COMPONENT = "abicomp",
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
        CompoundTag ct = new CompoundTag();

        ListTag lt;

        lt = new ListTag();
        for(int i = 0; i < abilities.size();i++)
        { // abilities
            lt.add(new StringTag(abilities.get(i).id.toString()));
        }
        ct.put(ABILITY_LIST, lt);

        lt = new ListTag();
        for(int i = 0; i < 9; i++)
        {// Ability Hotbar
            RPGAbility ability = abilityBar.get(i);
            lt.add(new StringTag(ability.id.toString()));   
        }
        ct.put(ABILITY_BAR, lt);
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
            CompoundTag set = new CompoundTag();
            set.putString(COOLDOWN_NAME,i.toString());
            set.putInt(COOLDOWN_DURATION, cooldowns.get(i));
            lt.add(ct);
        }
        ct.put(COOLDOWN_LIST,lt);

        tag.put(ABILITY_COMPONENT, ct);
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        this.init();
        CompoundTag ct = tag.getCompound(ABILITY_COMPONENT);
        ListTag lt;
        int i;


        lt = (ListTag)ct.getTag(ABILITY_LIST);
        for(i = 0; lt.size() > i; i++)
        {
            String id = lt.getString(i);
            this.abilities.add(getAbility( new Identifier(id) ));
        }
        lt = (ListTag)ct.getTag(ABILITY_BAR);
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
        lt = (ListTag)ct.getTag(COOLDOWN_LIST);
        for(i = 0; i < lt.size();i++)
        {
            CompoundTag set = lt.getCompoundTag(i);
            cooldowns.put(new Identifier(set.getString(COOLDOWN_NAME)), set.getInt(COOLDOWN_DURATION));
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
            this.abilities.add(getAbility(buf.readIdentifier()));
        }
        for(int i = 0; i < 9; i++)
        {
            this.abilityBar.set(i, RPG_Registry.ABILITY_REGISTRY.get(buf.readIdentifier()));
        }
        // size = buf.readShort();
        // for(int i = 0; i < size; i++)
        // {
        //     tokens.put(buf.readIdentifier(), buf.readInt());
        // }
        size = buf.readShort();
        for(int i = 0; i < size; i++)
        {
            this.cooldowns.put(buf.readIdentifier(), buf.readInt());
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