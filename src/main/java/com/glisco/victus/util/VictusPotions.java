package com.glisco.victus.util;

import com.glisco.victus.Victus;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class VictusPotions {

    public static RegistryEntry<Potion> HEARTBLEED;
    public static RegistryEntry<Potion> RESURGENCE;
    public static RegistryEntry<Potion> LONG_RESURGENCE;
    public static RegistryEntry<Potion> STRONG_RESURGENCE;

    public static void register() {
        HEARTBLEED = Registry.registerReference(Registries.POTION, Victus.id("heartbleed"), new Potion(new StatusEffectInstance(VictusStatusEffects.HEARTBLEED, 400)));
        RESURGENCE = Registry.registerReference(Registries.POTION, Victus.id("resurgence"), new Potion(new StatusEffectInstance(VictusStatusEffects.RESURGENCE, 400)));
        LONG_RESURGENCE = Registry.registerReference(Registries.POTION, Victus.id("long_resurgence"), new Potion(new StatusEffectInstance(VictusStatusEffects.RESURGENCE, 800)));
        STRONG_RESURGENCE = Registry.registerReference(Registries.POTION, Victus.id("strong_resurgence"), new Potion(new StatusEffectInstance(VictusStatusEffects.RESURGENCE, 200, 1)));

        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder -> {
            builder.registerPotionRecipe(Potions.HARMING, Items.GLOW_LICHEN, HEARTBLEED);
            builder.registerPotionRecipe(Potions.REGENERATION, Items.MAGMA_CREAM, RESURGENCE);
            builder.registerPotionRecipe(RESURGENCE, Items.REDSTONE, LONG_RESURGENCE);
            builder.registerPotionRecipe(RESURGENCE, Items.GLOWSTONE, STRONG_RESURGENCE);
        });
    }
}
