// package com.biom4st3r.minerpg.mixin;

// import com.biom4st3r.minerpg.api.Stat.Stats;
// import com.biom4st3r.minerpg.components.RPGStatsComponent;
// import com.biom4st3r.minerpg.util.EnchContainerHelper;
// import com.biom4st3r.minerpg.util.RPGPlayer;
// import com.biom4st3r.minerpg.util.Util;

// import org.objectweb.asm.Opcodes;
// import org.spongepowered.asm.mixin.Final;
// import org.spongepowered.asm.mixin.Mixin;
// import org.spongepowered.asm.mixin.Shadow;
// import org.spongepowered.asm.mixin.injection.At;
// import org.spongepowered.asm.mixin.injection.Inject;
// import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// import net.minecraft.container.BlockContext;
// import net.minecraft.container.EnchantingTableContainer;
// import net.minecraft.entity.player.PlayerEntity;
// import net.minecraft.entity.player.PlayerInventory;
// import net.minecraft.item.ItemStack;
// import net.minecraft.util.math.BlockPos;
// import net.minecraft.world.World;


// @Mixin(EnchantingTableContainer.class)
// public abstract class EnchantContainerContexterMixin implements EnchContainerHelper {

    
//     PlayerEntity player;
//     Boolean locked = false;
    
//     @Shadow @Final public int[] enchantmentPower;
//     @Shadow @Final public int[] enchantmentId;
//     @Shadow @Final public int[] enchantmentLevel;

//     @Inject(at = @At("RETURN"), method = "<init>*")
//     public void construct(int synid, PlayerInventory playerInv, BlockContext blockContext_1, CallbackInfo ci) {
//         this.player = playerInv.player;
//     }
//     // modifier * score / 10

//     @Inject(at = @At("HEAD"),method = "onContentChanged")
//     public void unlockOnNewInstance(CallbackInfo ci)
//     {
//         locked = false;
//     }

//     @Inject(at = @At(value = "JUMP",opcode = Opcodes.IF_ICMPGE,ordinal = 2),
//     method = "method_17411")
//     private void onContentChangedContextRun(ItemStack iS, World world, BlockPos pos, CallbackInfo ci)
//     {
//         if(!locked)
//         {
//             locked = true;
//             Biow0rks.logger.debug(String.format("Pre-Power\n%s %s %s", enchantmentPower[0],enchantmentPower[1],enchantmentPower[2]));
            
//             RPGStatsComponent stats = ((RPGPlayer)this.getPlayer()).getStatsComponent();
//             int modifier = stats.getModifier(Stats.WISDOW);
//             int score = stats.getStat(Stats.WISDOW);
//             int addon = Math.round((score * modifier)/8);
//             Biow0rks.logger.debug(String.format("%s %s %s", modifier,score,addon));
//             for(int i = 0; i < enchantmentPower.length && enchantmentPower.length > 0; i++)
//             {
//                 enchantmentPower[i] +=addon;
//             }
//             Biow0rks.logger.debug(String.format("Post-Power\n%s %s %s\n", enchantmentPower[0],enchantmentPower[1],enchantmentPower[2]));
//         }
//     }

//     @Override
//     public PlayerEntity getPlayer() {
//         return player;
//     }
// }