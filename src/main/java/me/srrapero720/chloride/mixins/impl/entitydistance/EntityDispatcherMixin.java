package me.srrapero720.chloride.mixins.impl.entitydistance;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.Tools;
import me.srrapero720.chloride.foundation.entitydistance.IWhitelistCheck;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityDispatcherMixin {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void inject$shouldRender(E entity, Frustum clippingHelper, double x, double y, double z, CallbackInfoReturnable<Boolean> cir) {

        final int distY;
        final int distX;
        final MobCategory category = entity.getType().getCategory();
        if (category == MobCategory.MONSTER) {
            if (!ChlorideConfig.monsterDistanceCulling) return;
            distY = ChlorideConfig.monsterCullingDistanceY;
            distX = ChlorideConfig.monsterCullingDistanceX;
        } else {
            if (!ChlorideConfig.entityDistanceCulling) return;
            distY = ChlorideConfig.entityCullingDistanceY;
            distX = ChlorideConfig.entityCullingDistanceX;
        }

        boolean isWhitelisted = ((IWhitelistCheck) entity.getType()).embPlus$isWhitelisted();
        if (!isWhitelisted && !Tools.isEntityInRange(entity, x, y, z, distY, distX)) {
            cir.setReturnValue(false);
        }
    }
}