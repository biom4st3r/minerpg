
// package com.biom4st3r.minerpg.impl.rpgclass;

// import java.util.ArrayList;
// import java.util.List;

// import com.biom4st3r.minerpg.api.RPGClass;
// import com.biom4st3r.minerpg.interfaces.Reward;
// import com.biom4st3r.minerpg.registery.RpgAbilities;
// import com.biom4st3r.minerpg.util.DialogKeys;
// import com.biom4st3r.minerpg.util.Util;

// import net.minecraft.enchantment.Enchantments;
// import net.minecraft.entity.EntityType;
// import net.minecraft.entity.LivingEntity;
// import net.minecraft.item.ItemStack;
// import net.minecraft.item.Items;
// import net.minecraft.stat.Stat;
// import net.minecraft.stat.Stats;
// import net.minecraft.text.LiteralText;
// import net.minecraft.text.TranslatableText;
// import net.minecraft.util.Formatting;
// import net.minecraft.util.Identifier;

// public class BarbarianClass extends RPGClass {

//     public BarbarianClass(Identifier name) {
//         super(name);
//     }

//     Reward EMPTY = ((rpgplayer, list) -> {
//     });

//     @Override
//     public Reward givePlayerRewards(int level) {
//         switch (level) {
//         case 20:
//             return EMPTY;
//         case 19:
//             return EMPTY;
//         case 18:
//             return EMPTY;
//         case 17:
//             return EMPTY;
//         case 16:
//             return EMPTY;
//         case 15:
//             return EMPTY;
//         case 14:
//             return EMPTY;
//         case 13:
//             return EMPTY;
//         case 12:
//             return EMPTY;
//         case 11:
//             return EMPTY;
//         case 10:
//             return EMPTY;
//         case 9:
//             return EMPTY;
//         case 8:
//             return EMPTY;
//         case 7:
//             return EMPTY;
//         case 6:
//             return EMPTY;
//         case 5:
//             return EMPTY;
//         case 4:
//             return EMPTY;
//         case 3:
//             return ((rpgplayer, list) -> {
//                 rpgplayer.getRPGAbilityComponent().addAbility(RpgAbilities.EVOKER_FANGS);
//                 rpgplayer.getRPGAbilityComponent().addAbility(RpgAbilities.EVOKER_AOE);
//                 list.add(new ItemStack(Items.WOLF_SPAWN_EGG));
//                 rpgplayer.getStatsComponent().remainingPoints += 5;

//                 rpgplayer.sendMessage(DialogKeys.createTranslated_LearnedAbility(RpgAbilities.EVOKER_FANGS));
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_LearnedAbility(RpgAbilities.EVOKER_AOE));
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_GivenItem(new ItemStack(Items.WOLF_SPAWN_EGG)));
//                 rpgplayer.sendMessage(new LiteralText(String.format(new TranslatableText(DialogKeys.Given_SkillPoint).asFormattedString(), 5)));
//             });
//         case 2:
//             return ((rpgplayer, list) -> {
//                 rpgplayer.getRPGAbilityComponent().addAbility(RpgAbilities.FIREBALL_ABILITY);
//                 rpgplayer.getStatsComponent().remainingPoints += 2;
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_LearnedAbility(RpgAbilities.FIREBALL_ABILITY));
//                 rpgplayer.sendMessage(new LiteralText(String.format(new TranslatableText(DialogKeys.Given_SkillPoint).asFormattedString(), 2)));

//             });
//         case 1:
//             return ((rpgplayer, list) -> {
//                 rpgplayer.getRPGAbilityComponent().addAbility(RpgAbilities.RAGE_ABILITY);
//                 rpgplayer.getRPGAbilityComponent().addAbility(RpgAbilities.RECKLESS_ATK);
//                 // rpgplayer.getStatsComponent().remainingPoints+=2;
//                 ItemStack iS = new ItemStack(Items.IRON_AXE, 1);
//                 iS.addEnchantment(Enchantments.SHARPNESS, 2);
//                 iS.getTag().putBoolean("Unbreakable", true);
//                 iS.setCustomName(new LiteralText(Formatting.RESET + "" + Formatting.GOLD + "Ramguard"));
//                 list.add(iS);
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_LearnedAbility(RpgAbilities.RAGE_ABILITY));
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_LearnedAbility(RpgAbilities.RECKLESS_ATK));
//                 rpgplayer.sendMessage(DialogKeys.createTranslated_GivenItem(iS));
//             });
//         default:
//             return ((player, list) -> {
//             });
//         }

//     }

//     // @Override
//     // public RPGAbility[] abilitysAvalibleAtLevel(int Lvl) {
//     // List<RPGAbility> abilities = new ArrayList<RPGAbility>(2);
//     // switch (Lvl) {
//     // case 20:
//     // case 19:
//     // case 18:
//     // case 17:
//     // case 16:
//     // case 15:
//     // case 14:
//     // case 13:
//     // case 12:
//     // case 11:
//     // case 10:
//     // case 9:
//     // case 8:
//     // case 7:
//     // case 6:
//     // case 5:
//     // case 4:
//     // case 3:
//     // case 2:
//     // case 1:
//     // default:
//     // abilities.add(RpgAbilities.EVOKER_AOE);
//     // abilities.add(RpgAbilities.EVOKER_FANGS);
//     // abilities.add(RpgAbilities.RECKLESS_ATK);
//     // abilities.add(RpgAbilities.FIREBALL_ABILITY);
//     // //abilities.add(RpgAbilities.UNARMORED_DEFENCE);
//     // abilities.add(RpgAbilities.RAGE_ABILITY);
//     // break;
//     // }
//     // return Util.reverse(abilities.toArray(new RPGAbility[0]));
//     // }

//     @Override
//     public List<String> getToolTips() {
//         List<String> tips = new ArrayList<>(3);
//         tips.add(this.getDisplayName().asFormattedString());
//         return tips;
//     }

//     @Override
//     public ExpType getExpType() {
//         return ExpType.VANILLA_STAT;
//     }

//     @Override
//     public float getStatWorthAtLevel(Stat<?> stat, int amount, int Lvl) {
//         if (stat.getType() == Stats.KILLED) 
//         {
//             EntityType<?> et = (EntityType<?>) stat.getValue();
//             if(et.getCategory().isAnimal()) return 0.5f;
//             else if(et.getCategory().isPeaceful()) return 0.1f;
//             return (((LivingEntity)et.create(null)).getHealthMaximum()/3);
//         }
//         return 0;
//     }

//     @Override
//     public float getExpRequiredForLvl(int lvl) {
        
//         return Util.plagurmonLevelingAlgoSlow(lvl);
//     }
// }