package com.biom4st3r.minerpg.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.biom4st3r.minerpg.api.RPGAbility;
import com.biom4st3r.minerpg.registery.RPG_Registry;
import com.biom4st3r.minerpg.util.RPGPlayer;
import com.biom4st3r.minerpg.util.RpgAbilityContext;
import com.biom4st3r.minerpg.util.Util;
import com.google.common.collect.Maps;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

public class RPGAbilityComponent implements AbstractComponent {

    public List<RPGAbility> specialAbilities;

    public DefaultedList<RpgAbilityContext> abilityBar;

    public Map<Identifier,Integer> cooldowns; //TODO: lower visiblity

    protected Map<Identifier,Integer> tokens;

    //public RpgAbilityContext armorOverride;
    
    private Map<String,RpgAbilityContext> namedAbilityBar;
    public static String SLOT_ARMOROVERRIDE = "armoroverride";
    //public NNObj<RpgAbilityContext> armorOverride2;

    @Override
    public <T extends AbstractComponent> void clone(T origin) {
        RPGAbilityComponent original = (RPGAbilityComponent)origin;
        for(RPGAbility a : original.specialAbilities)
        {
            this.specialAbilities.add(a);
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
        this.tokens = Maps.newHashMap();
        for(Identifier i : tokens.keySet())
        {
            this.tokens.put(i, original.tokens.get(i));
        }
    }

    public RPGAbilityComponent()
    {
        specialAbilities = new ArrayList<RPGAbility>();
        abilityBar = DefaultedList.ofSize(9, RpgAbilityContext.EMPTY);
        cooldowns = Maps.newHashMap();
        tokens = Maps.newHashMap();
        //armorOverride = RpgAbilityContext.EMPTY;
        namedAbilityBar = Maps.newHashMap();
    }

    public RpgAbilityContext getNamedAbilitySlot(String s)
    {
        RpgAbilityContext a = namedAbilityBar.get(s);
        return a == null ? RpgAbilityContext.EMPTY : a;
    }

    public void setNamedAbilitySlot(String s, RpgAbilityContext rac)
    {
        namedAbilityBar.put(s,rac);
    }

    public boolean hasToken(RPGAbility ability)
    {
        return hasToken(ability.id);
    }

    public boolean hasToken(Identifier i)
    {
        return tokens.containsKey(i);
    }

    public void putToken(RPGPlayer player, RPGAbility ability)
    {
        putToken(player, ability.id,ability.getMaxTokens(player));
    }

    public void putToken(RPGPlayer player, Identifier i,int quantity)
    {
        if(!hasToken(i))
        {
            tokens.put(i, quantity);
        }
        else
        {
            int temp = tokens.get(i).intValue()+quantity;
            tokens.remove(i);
            tokens.put(i, temp);
        }
    }

    public boolean subtractToken(RPGAbility ability, int quantity)
    {
        return subtractToken(ability.id, quantity);
    }

    public boolean subtractToken(Identifier i, int quantity)
    {
        if(hasToken(i) && tokens.get(i).intValue() >= quantity)
        {
            int temp = tokens.get(i).intValue()-quantity;
            tokens.remove(i);
            tokens.put(i,temp);
            return true;
        }
        return false;
    }

    public void addCooldown(RPGAbility ability)
    {
        //cooldowns.put(ability.id, ability.getCoolDown());
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

    public static final String ABILITY_LIST = "rpgAbilList";
    public static final String ABILITY_LIST_SIZE = "rpgAbLSize";
    public static final String SLOT_ID = "slotid";
    public static final String ABILITY_ID = "abilid";
    public static final String ABILITY_BAR = "abilibar";


    //public Hashtable<RPGAbility, Object> abilityResults;

    @Override
    public void serializeNBT(CompoundTag tag) {
        ListTag lt = new ListTag();
        for(RPGAbility a : specialAbilities)
        {
            lt.add(new StringTag(a.id.toString()));
        }
        tag.put(ABILITY_LIST, lt);
        lt = new ListTag();
        //CompoundTag ct;
        for(int i = 0; i < 9; i++)
        {
            RpgAbilityContext rac = abilityBar.get(i);
            CompoundTag ct = new CompoundTag();
            rac.serializeNbt(ct);
            lt.add(ct);
        }
        tag.put(ABILITY_BAR, lt);
        //TODO: add tokens, cooldowns, and armorOverride
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        //Util.debug(tag);
        ListTag lt = tag.getList(ABILITY_LIST,10);
        int i;
        for(i = 0; i < lt.size(); i++)
        {
            specialAbilities.add( getAbility(new Identifier(lt.getString(i))));
        }
        lt = tag.getList(ABILITY_BAR, 10);
        Util.debug(lt.size());
        //Util.debug(lt);
        for(i = 0; i < 9 && lt.size() > 0 ; i++)
        {
            RpgAbilityContext rac = new RpgAbilityContext(null, -1, null);
            rac.deserializeNbt(lt.getCompoundTag(i));
            this.abilityBar.set(i, rac);
        }
        //TODO: add tokens, cooldowns, and armorOverride
    }

    private static RPGAbility getAbility(Identifier i)
    {
        return RPG_Registry.ABILITY_REGISTRY.get(i);
    }

    @Override
    public void serializeBuffer(PacketByteBuf buf) {
        buf.writeByte(specialAbilities.size());
        for(RPGAbility a : specialAbilities)
        {
            buf.writeString(a.id.toString());
        }
        for(RpgAbilityContext rac : abilityBar)
        {
            rac.serializeBuffer(buf);
        }
        //TODO: add tokens, cooldowns, and armorOverride
    }

    @Override
    public void deserializeBuffer(PacketByteBuf buf) {
        int size = buf.readByte();
        for(int i = 0; i< size; i++)
        {
            specialAbilities.add(getAbility(new Identifier(buf.readString(32767))));
        }
        for(int i = 0; i < 9; i++)
        {
            abilityBar.set(i, new RpgAbilityContext(buf));
        }
        //TODO: add tokens, cooldowns, and armorOverride
    }

    @Override
    public <T extends AbstractComponent> T getCopy() {
        return null;
    }

}