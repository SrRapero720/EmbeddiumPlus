package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.Tools;
import me.srrapero720.chloride.features.DistanceCullingFeature;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.srrapero720.chloride.Chloride.LOGGER;

public class EntityDistanceCullingMixin {

    @Mixin(EntityRenderDispatcher.class)
    public static class EntityDispatcherMixin {
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

            boolean isWhitelisted = ((DistanceCullingFeature) entity.getType()).chloride$whitelisted();
            if (!isWhitelisted && !Tools.isEntityInRange(entity, x, y, z, distY, distX)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Mixin(EntityType.class)
    @SuppressWarnings("deprecation")
    public abstract static class EntityTypeMixin implements DistanceCullingFeature {
        @Unique
        private static final Marker e$IT = MarkerManager.getMarker("EntityType");
        @Unique private boolean embPlus$checked = false;
        @Unique private boolean embPlus$whitelisted = false;

        @Shadow
        public abstract MobCategory getCategory();

        @Override
        @Unique
        public boolean chloride$whitelisted() {
            if (embPlus$checked) return embPlus$whitelisted;

            final var resource = embPlus$resourceLocation();
            if (resource == null) {
                LOGGER.warn(e$IT, "key for '{}' is null, some mod decides to broke itself, not whitelisting", this.getClass().getName());
                return false;
            }

            this.embPlus$whitelisted = Tools.isWhitelisted(resource, this.getCategory() == MobCategory.MONSTER ? ChlorideConfig.monsterWhitelist : ChlorideConfig.entityWhitelist);
            this.embPlus$checked = true;

            LOGGER.debug(e$IT,"Whitelist checked for {}", resource.toString());
            return embPlus$whitelisted;
        }

        @Unique
        public ResourceLocation embPlus$resourceLocation() {
            return BuiltInRegistries.ENTITY_TYPE.getKey(embPlus$cast());
        }

        @Unique
        private EntityType<?> embPlus$cast() {
            return (EntityType<?>) ((Object) this);
        }
    }

    @Mixin(BlockEntityRenderDispatcher.class)
    public static class TileDispatcherMixin {
        @Shadow public Camera camera;

        @Inject(at = @At("HEAD"), method = "render", cancellable = true)
        public <E extends BlockEntity> void render(E tile, float val, PoseStack matrix, MultiBufferSource bufferSource, CallbackInfo ci) {
            if (!ChlorideConfig.tileEntityDistanceCulling) return;

            boolean isWhitelisted = ((DistanceCullingFeature) tile.getType()).chloride$whitelisted();
            if (!isWhitelisted && !Tools.isEntityInRange(tile.getBlockPos(), camera.getPosition(),
                    ChlorideConfig.tileEntityCullingDistanceY,
                    ChlorideConfig.tileEntityCullingDistanceX)
            ) {
                ci.cancel();
            }
        }
    }

    @Mixin(BlockEntityType.class)
    public abstract static class TileEntityTypeMixin implements DistanceCullingFeature {
        @Unique private static final Marker e$IT = MarkerManager.getMarker("BlockEntityType");
        @Unique private boolean embPlus$checked = false;
        @Unique private boolean embPlus$whitelisted = false;

        @Override
        public boolean chloride$whitelisted() {
            if (embPlus$checked) return embPlus$whitelisted;
            ResourceLocation resource = BlockEntityType.getKey(embPlus$cast());
            if (resource == null) {
                LOGGER.warn(e$IT, "key for '{}' is null, some mod decides to broke itself, not whitelisting", this.getClass().getName());
                return false;
            }
            this.embPlus$whitelisted = Tools.isWhitelisted(resource, ChlorideConfig.tileEntityWhitelist);
            this.embPlus$checked = true;

            LOGGER.debug(e$IT,"Whitelist checked for {}", resource.toString());
            return embPlus$whitelisted;
        }

        @Unique
        private BlockEntityType<?> embPlus$cast() {
            return (BlockEntityType<?>) ((Object) this);
        }
    }
}
