package com.glisco.victus.item;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.HeartAspect;
import com.glisco.victus.network.VictusParticleEvents;
import io.wispforest.owo.itemgroup.OwoItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Language;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeartAspectItem extends EdibleItem {

    private static final Map<HeartAspect.Type, HeartAspectItem> HEART_ASPECT_ITEMS = new HashMap<>();

    private final HeartAspect.Type aspectType;

    public HeartAspectItem(HeartAspect.Type aspectType) {
        super(new OwoItemSettings().group(Victus.VICTUS_GROUP).food(new FoodComponent.Builder().alwaysEdible().nutrition(4).saturationModifier(.5f).build()).maxCount(1));
        this.aspectType = aspectType;

        HeartAspectItem.HEART_ASPECT_ITEMS.put(aspectType, this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        final var playerStack = user.getStackInHand(hand);
        final var aspectComponent = Victus.ASPECTS.get(user);

        if (!aspectComponent.acceptsNew()) return TypedActionResult.pass(playerStack);

        user.setCurrentHand(hand);
        return TypedActionResult.success(playerStack);
    }

    @Override
    void onEaten(ItemStack stack, World world, PlayerEntity eater) {
        final var aspectComponent = Victus.ASPECTS.get(eater);
        aspectComponent.addAspect(aspectType.factory().apply(eater));

        VictusParticleEvents.HEART_PARTICLES.spawn(world, eater.getPos(), false);
    }

    @Override
    public Text getName(ItemStack stack) {
        return VictusItems.coloredTranslationWithPrefix(getTranslationKey(stack), aspectType.color());
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        String desc = Language.getInstance().get(this.getTranslationKey(stack) + ".description");
        if (desc.length() > 30 && desc.contains(" ")) {
            int spaceIndex = StringUtils.ordinalIndexOf(desc, " ", StringUtils.countMatches(desc, " ") / 2 + 1);
            tooltip.add(Text.literal(desc.substring(0, spaceIndex)).formatted(Formatting.DARK_GRAY));
            tooltip.add(Text.literal(desc.substring(spaceIndex + 1)).formatted(Formatting.DARK_GRAY));
        } else {
            tooltip.add(Text.translatable(this.getTranslationKey(stack) + ".description").formatted(Formatting.DARK_GRAY));
        }

        tooltip.add(Text.of(" "));
        tooltip.add(Text.translatable("text.victus.recharge_duration", aspectType.standardRechargeDuration() / 20f).formatted(Formatting.BLUE));
    }

    public static HeartAspectItem getItem(HeartAspect.Type type) {
        return HeartAspectItem.HEART_ASPECT_ITEMS.get(type);
    }
}
