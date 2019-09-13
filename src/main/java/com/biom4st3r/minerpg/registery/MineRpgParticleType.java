// package com.biom4st3r.minerpg.registery;

// import java.lang.reflect.Constructor;
// import java.lang.reflect.Field;
// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Type;

// import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
// import net.fabricmc.api.EnvType;
// import net.fabricmc.api.Environment;
// import net.minecraft.client.MinecraftClient;
// import net.minecraft.client.particle.ParticleFactory;
// import net.minecraft.client.particle.ParticleManager;
// import net.minecraft.client.texture.SpriteAtlasTexture;
// import net.minecraft.particle.DefaultParticleType;
// import net.minecraft.particle.ParticleType;
// import net.minecraft.util.registry.Registry;

// public class MineRpgParticleType {
//     public static DefaultParticleType getDefaultParticleType(boolean alwaysVisible) {
//         Class<?> defaultParticleType = DefaultParticleType.class;
//         Constructor<?> dptConstruct = defaultParticleType.getConstructors()[0];
//         DefaultParticleType t = null;
//         try {
//             dptConstruct.setAccessible(true);
//             t = (DefaultParticleType) dptConstruct.newInstance(alwaysVisible);

//         } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
//                 | InvocationTargetException e) {
//             e.printStackTrace();
//         }
//         return t;
//     }

//     @Environment(EnvType.CLIENT)
//     @SuppressWarnings("unchecked")
//     public static void registerFactory(ParticleType<?> type, ParticleFactory<?> factory) {
//         ParticleManager pm = MinecraftClient.getInstance().particleManager;
//         Field factoriesField = pm.getClass().getDeclaredFields()[6];
//         factoriesField.setAccessible(true);
//         Int2ObjectMap<ParticleFactory<?>> factories = null;
//         try {
//             factories = (Int2ObjectMap<ParticleFactory<?>>) factoriesField.get(pm);
//         } catch (IllegalArgumentException | IllegalAccessException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         factories.put(Registry.PARTICLE_TYPE.getRawId(type), factory);
//         // ParticleManager
//     }

//     @Environment(EnvType.CLIENT)
//     public static SpriteAtlasTexture getAtlas() {
//         ParticleManager pm = MinecraftClient.getInstance().particleManager;
//         Field atlasField = pm.getClass().getDeclaredFields()[9];
//         atlasField.setAccessible(true);
//         try {
//             return (SpriteAtlasTexture) atlasField.get(pm);
//         } catch (IllegalArgumentException | IllegalAccessException e) {
//             // TODO Auto-generated catch block
//             e.printStackTrace();
//         }
//         return null;
//     } 

// }