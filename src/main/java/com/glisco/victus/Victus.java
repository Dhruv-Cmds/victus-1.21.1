package com.glisco.victus;

import com.glisco.victus.hearts.HeartAspectComponent;
import com.glisco.victus.hearts.HeartAspectRegistry;
import com.glisco.victus.item.EdibleItem;
import com.glisco.victus.item.VictusItems;
import com.glisco.victus.network.VictusPackets;
import com.glisco.victus.network.VictusParticleEvents;
import com.glisco.victus.util.EntityFlagComponent;
import com.glisco.victus.util.VictusPotions;
import com.glisco.victus.util.VictusStatusEffects;
import io.wispforest.owo.itemgroup.Icon;
import io.wispforest.owo.itemgroup.OwoItemGroup;
import io.wispforest.owo.itemgroup.gui.ItemGroupButton;
import io.wispforest.owo.registration.reflect.FieldRegistrationHandler;
import io.wispforest.owo.util.TagInjector;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetComponentsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class Victus implements ModInitializer, EntityComponentInitializer {

    private static final Logger LOGGER = LogManager.getLogger("Victus");

    public static final String MOD_ID = "victus";

    public static final ComponentKey<HeartAspectComponent> ASPECTS = ComponentRegistry.getOrCreate(id("aspects"), HeartAspectComponent.class);
    public static final ComponentKey<EntityFlagComponent> ENTITY_FLAGS = ComponentRegistry.getOrCreate(id("flags"), EntityFlagComponent.class);

    public static final OwoItemGroup VICTUS_GROUP = OwoItemGroup
            .builder(Victus.id("victus"), () -> Icon.of(VictusItems.VOID_HEART_ASPECT))
            .initializer(group -> {
                group.addButton(ItemGroupButton.curseforge(group, "https://www.curseforge.com/minecraft/mc-mods/victus"));
                group.addButton(ItemGroupButton.modrinth(group, "https://modrinth.com/mod/victus"));
                group.addButton(ItemGroupButton.discord(group, "https://discord.gg/xrwHKktV2d"));
                group.addButton(ItemGroupButton.github(group, "https://github.com/glisco03/victus"));

                group.addButton(new ItemGroupButton(group, Icon.of(VictusItems.BLANK_HEART_ASPECT), "remove_first_heart", VictusPackets::requestAspectRemoval));
            })
            .build();

    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void onInitialize() {
        VictusPackets.registerPayloads();

        HeartAspectRegistry.registerDefaults();

        // effects must be registered before potions, since potions reference them
        VictusStatusEffects.register();
        VictusPotions.register();

        FieldRegistrationHandler.register(VictusItems.class, MOD_ID, false);

        FieldRegistrationHandler.process(VictusItems.class, (item, s, field) -> {
            if (!(item instanceof EdibleItem)) return;
            TagInjector.inject(Registries.ITEM, Identifier.of("origins", "ignore_diet"), item);
        }, false);

        VICTUS_GROUP.initialize();

        VictusPackets.registerServerListeners();
        VictusParticleEvents.initialize();

        registerLoot();
    }

    private static void registerLoot() {
        LootTableEvents.MODIFY.register((key, tableBuilder, source, registries) -> {
            if (!source.isBuiltin()) return;

            if (key.equals(LootTables.BURIED_TREASURE_CHEST)) {
                tableBuilder.pool(simplePool(VictusItems.BROKEN_HEART, .75f));
            }

            if (key.equals(LootTables.SIMPLE_DUNGEON_CHEST) || key.equals(LootTables.ABANDONED_MINESHAFT_CHEST)) {
                tableBuilder.pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .conditionally(RandomChanceLootCondition.builder(.6f))
                                .with(ItemEntry.builder(Items.SPLASH_POTION)
                                        .apply(SetComponentsLootFunction.builder(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(VictusPotions.HEARTBLEED))))
                );
            }
        });
    }

    private static LootPool.Builder simplePool(Item item, float chance) {
        return LootPool.builder()
                .rolls(ConstantLootNumberProvider.create(1))
                .conditionally(RandomChanceLootCondition.builder(chance))
                .with(ItemEntry.builder(item));
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ASPECTS, HeartAspectComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerFor(Entity.class, ENTITY_FLAGS, entity -> new EntityFlagComponent());
    }
}
