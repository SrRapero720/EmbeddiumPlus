package me.srrapero720.chloride.mixins.impl;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.IRenderableEntity;
import me.srrapero720.chloride.impl.EntityCulling;
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
        public <E extends Entity> void inject$shouldRender(final E entity, final Frustum clippingHelper, final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> cir) {

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

            final boolean isWhitelisted = ((IRenderableEntity) entity.getType()).chloride$whitelisted();
            if (!isWhitelisted && !EntityCulling.isEntityInRange(entity, x, y, z, distY, distX)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Mixin(EntityType.class)
    public abstract static class EntityTypeMixin implements IRenderableEntity {
        @Unique private static final Marker e$IT = MarkerManager.getMarker("EntityCulling");
        @Unique private boolean chloride$checked = false;
        @Unique private boolean chloride$whitelisted = false;

        @Shadow
        public abstract MobCategory getCategory();

        @Override
        @Unique
        public boolean chloride$whitelisted() {
            if (this.chloride$checked) return this.chloride$whitelisted;

            final var resource = this.embPlus$resourceLocation();
            if (resource == null) {
                LOGGER.warn(e$IT, "Entity Key '{}' is null, not whitelisting a broken mod entity", this.getClass().getName());
                return false;
            }

            this.chloride$whitelisted = EntityCulling.isWhitelisted(resource, this.getCategory() == MobCategory.MONSTER ? ChlorideConfig.monsterWhitelist : ChlorideConfig.entityWhitelist);
            this.chloride$checked = true;

            LOGGER.debug(e$IT,"Computed Entity whitelist for {}", resource.toString());
            return this.chloride$whitelisted;
        }

        @Unique
        public ResourceLocation embPlus$resourceLocation() {
            return BuiltInRegistries.ENTITY_TYPE.getKey(this.embPlus$cast());
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
        public <E extends BlockEntity> void render(final E tile, final float val, final PoseStack matrix, final MultiBufferSource bufferSource, final CallbackInfo ci) {
            if (!ChlorideConfig.tileEntityDistanceCulling) return;

            final boolean isWhitelisted = ((IRenderableEntity) tile.getType()).chloride$whitelisted();
            if (!isWhitelisted && !EntityCulling.isEntityInRange(tile, this.camera.getPosition(),
                    ChlorideConfig.tileEntityCullingDistanceY,
                    ChlorideConfig.tileEntityCullingDistanceX)
            ) {
                ci.cancel();
            }
        }
    }

    @Mixin(BlockEntityType.class)
    public abstract static class TileEntityTypeMixin implements IRenderableEntity {
        @Unique private static final Marker e$IT = MarkerManager.getMarker("BlockEntityCulling");
        @Unique private boolean chloride$checked = false;
        @Unique private boolean chloride$whitelisted = false;

        @Override
        public boolean chloride$whitelisted() {
            if (this.chloride$checked) return this.chloride$whitelisted;
            final ResourceLocation resource = BlockEntityType.getKey((BlockEntityType<?>) ((Object) this));
            if (resource == null) {
                LOGGER.warn(e$IT, "BlockEntity key for '{}' is null, not whitelisting a broken mod block", this.getClass().getName());
                return false;
            }
            this.chloride$whitelisted = EntityCulling.isWhitelisted(resource, ChlorideConfig.tileEntityWhitelist);
            this.chloride$checked = true;

            LOGGER.debug(e$IT,"Computed BlockEntity whitelist for {}", resource.toString());
            return this.chloride$whitelisted;
        }
    }
}
