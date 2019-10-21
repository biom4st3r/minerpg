// package com.biom4st3r.minerpg.api;

// import com.biom4st3r.minerpg.MineRPG;
// import com.biom4st3r.minerpg.mixin_interfaces.RPGPlayer;

// import net.minecraft.util.Identifier;

// public abstract class RPGTrait<T> 
// {
//     public final Identifier id;
//     public final TraitCatagory tc;

//     public static final RPGTrait<Void> NONE = new RPGTrait<Void>(new Identifier(MineRPG.MODID, "NONE"), null) {
//         @Override
//         public Void output(RPGPlayer player) {
//             return null;
//         }
//     };

//     protected RPGTrait(Identifier id, TraitCatagory tc)
//     {
//         this.id = id;
//         this.tc = tc;
//     }

//     public TraitCatagory getCatagory()
//     {
//         return this.tc;
//     }

//     public abstract T output(RPGPlayer player);


//     public boolean isNone()
//     {
//         return this == NONE;
//     }

// }