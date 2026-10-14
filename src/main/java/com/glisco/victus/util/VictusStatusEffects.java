package com.glisco.victus.util;

import com.glisco.victus.Victus;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class VictusStatusEffects {

    // Since 1.20.5 status effects are referenced through registry entries,
    // these are populated by {@link #register()}
    public static RegistryEntry<StatusEffect> HEARTBLEED;
    public static RegistryEntry<StatusEffect> RESURGENCE;
    public static RegistryEntry<StatusEffect> TRUE_DAMAGE;

    public static void register() {
        HEARTBLEED = Registry.registerReference(Registries.STATUS_EFFECT, Victus.id("heartbleed"), new VictusStatusEffect(StatusEffectCategory.NEUTRAL, 0xAE0000));
        RESURGENCE = Registry.registerReference(Registries.STATUS_EFFECT, Victus.id("resurgence"), new VictusStatusEffect(StatusEffectCategory.BENEFICIAL, 0xAE7733));
        TRUE_DAMAGE = Registry.registerReference(Registries.STATUS_EFFECT, Victus.id("true_damage"), new TrueDamageStatusEffect());
    }
}
