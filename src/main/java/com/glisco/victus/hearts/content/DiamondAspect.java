package com.glisco.victus.hearts.content;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.HeartAspect;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;

public class DiamondAspect extends HeartAspect {

    public static final Type TYPE = new Type(Victus.id("diamond"), 1, 50, 0x00D4C9, ALWAYS_UPDATE, DiamondAspect::new);

    // Since 1.21 attribute modifiers are identified by an Identifier instead of a random UUID,
    // which means they no longer need to be persisted
    private final Multimap<RegistryEntry<EntityAttribute>, EntityAttributeModifier> modifiers;

    public DiamondAspect(PlayerEntity player) {
        super(player, TYPE);

        modifiers = HashMultimap.create(2, 1);
        modifiers.put(EntityAttributes.GENERIC_ARMOR, new EntityAttributeModifier(Victus.id("diamond_heart_aspect_armor"), 4, EntityAttributeModifier.Operation.ADD_VALUE));
        modifiers.put(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, new EntityAttributeModifier(Victus.id("diamond_heart_aspect_armor_toughness"), 1, EntityAttributeModifier.Operation.ADD_VALUE));
    }

    @Override
    public void update() {
        player.getAttributes().addTemporaryModifiers(modifiers);
    }

    @Override
    protected boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        player.getAttributes().removeModifiers(modifiers);
        return false;
    }
}
