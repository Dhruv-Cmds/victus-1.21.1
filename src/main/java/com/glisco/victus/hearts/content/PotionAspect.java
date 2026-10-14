package com.glisco.victus.hearts.content;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.HeartAspect;
import com.glisco.victus.hearts.OverlaySpriteProvider;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

import java.util.List;

public class PotionAspect extends HeartAspect implements OverlaySpriteProvider {

    public static final Type TYPE = new Type(Victus.id("potion"), 7, 20, 0xFFFFFF, PotionAspect::new);

    private RegistryEntry<Potion> potion;

    public PotionAspect(PlayerEntity player) {
        super(player, TYPE);
        this.potion = Potions.EMPTY;
    }

    public RegistryEntry<Potion> getPotion() {
        return potion;
    }

    public boolean isEmpty() {
        return potion.matches(Potions.EMPTY);
    }

    public void setPotion(RegistryEntry<Potion> potion) {
        this.potion = potion;
        Victus.ASPECTS.sync(player);
    }

    @Override
    public boolean handleBreak(DamageSource source, float damage, float originalHealth) {
        List<StatusEffectInstance> list = potion.value().getEffects();

        for (StatusEffectInstance instance : list) {
            if (instance.getEffectType().value().isInstant()) {
                instance.getEffectType().value().applyInstantEffect(player, player, player, instance.getAmplifier(), 1.0D);
            } else {
                player.addStatusEffect(new StatusEffectInstance(instance));
            }
        }

        this.setPotion(Potions.EMPTY);
        return false;
    }

    @Override
    protected void readCustomData(NbtCompound nbt) {
        final var id = Identifier.tryParse(nbt.getString("Potion"));
        this.potion = id == null
                ? Potions.EMPTY
                : Registries.POTION.getEntry(id).<RegistryEntry<Potion>>map(entry -> entry).orElse(Potions.EMPTY);
    }

    @Override
    protected void writeCustomData(NbtCompound nbt) {
        nbt.putString("Potion", potion.getKey().map(key -> key.getValue().toString()).orElse("minecraft:empty"));
    }

    @Override
    public int getOverlayIndex() {
        return 8;
    }

    @Override
    public int getOverlayTint() {
        return isEmpty() ? 0xFFFFFF : new PotionContentsComponent(potion).getColor();
    }
}
