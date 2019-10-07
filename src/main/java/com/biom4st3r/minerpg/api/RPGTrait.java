// package com.biom4st3r.minerpg.api;

// import java.util.Optional;

// import net.minecraft.util.Identifier;

// public abstract class RPGTrait<T>
// {
//     public final Identifier id;
//     public final TraitCatagory tc;

//     protected RPGTrait(Identifier id, TraitCatagory tc)
//     {
//         this.id = id;
//         this.tc = tc;
//     }

//     public TraitCatagory getCatagory()
//     {
//         return this.tc;
//     }

//     public void tick(){}

//     public abstract Optional<T> getEffect();

// }