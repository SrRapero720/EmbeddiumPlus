package me.srrapero720.embeddiumplus.mixins.entitydistance;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import me.srrapero720.embeddiumplus.util.DistanceUtility;

@Mixin(EntityRenderDispatcher.class)
public class MaxDistanceEntity {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
        if (!EmbeddiumPlusConfig.enableDistanceChecks.get()) return;

        if (entity instanceof EnderDragon) return;
        if (entity instanceof Ghast) return;

        String name = entity.getClass().getName().toLowerCase();
        if (name.startsWith("com.simibubi.create.content.contraptions")) return;
        if (name.startsWith("com.github.alexthe666.iceandfire.entity") && name.contains("dragon")) return;

        if (!embeddiumExtras$entityWhitelisted(entity.getType().getDefaultLootTable()) && !DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                EmbeddiumPlusConfig.maxEntityRenderDistanceY.get(),
                EmbeddiumPlusConfig.maxEntityRenderDistanceSquare.get()
        )) {
            cir.cancel();
        }
    }

    @Unique
    private boolean embeddiumExtras$entityWhitelisted(ResourceLocation s) {
        return s != null && EmbeddiumPlusConfig.entityWhitelist.get().stream().anyMatch(s.toString()::equals);
    }
}