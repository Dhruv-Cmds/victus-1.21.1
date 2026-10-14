package com.glisco.victus.mixin;

import com.glisco.victus.Victus;
import com.glisco.victus.hearts.content.DraconicAspect;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(AreaEffectCloudEntity.class)
public abstract class AreaEffectCloudEntityMixin {

    @ModifyExpressionValue(
            method = "tick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"),
            require = 0
    )
    private List<LivingEntity> removeOwner(List<LivingEntity> entities) {
        if (!Victus.ENTITY_FLAGS.get(this).flagSet(DraconicAspect.IGNORE_OWNER_FLAG)) return entities;

        final var owner = ((AreaEffectCloudEntity) (Object) this).getOwner();
        if (owner != null) entities.remove(owner);

        return entities;
    }
}
