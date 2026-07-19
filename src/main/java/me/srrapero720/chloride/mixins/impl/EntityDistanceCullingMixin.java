package me.srrapero720.chloride.mixins.impl;

import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.api.IRenderableEntity;
import me.srrapero720.chloride.impl.EntityCulling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

import static me.srrapero720.chloride.Chloride.LOGGER;

public class EntityDistanceCullingMixin {

    @Mixin(LevelRenderer.class)
    public static class LevelRendererEntityMixin {
        @Redirect(method = "extractVisibleEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;entitiesForRendering()Ljava/lang/Iterable;"))
        public Iterable<Entity> redirect$entitiesForRendering(final ClientLevel instance) {
            // UNLIMITED
            if (ChlorideConfig.culling.entityLimit >= 512) return instance.entitiesForRendering();
            // NONE
            if (ChlorideConfig.culling.entityLimit <= 0) return Collections.emptyList();

            // GET ALL ENTITIES
            final List<Entity> copy = new ArrayList<>();
            for (final Entity entity: instance.entitiesForRendering()) {
                copy.add(entity);
            }

            // SORTS TO THE NEAREST ONE
            copy.sort(EntityCulling.DISTANCE_COMPARATOR);

            // LIMIT ENTITIES
            final List<Entity> limited = new ArrayList<>(ChlorideConfig.culling.entityLimit);
            for (final Entity entity: copy) {
                limited.add(entity);
                if (limited.size() >= ChlorideConfig.culling.entityLimit) break;
            }

            return limited;
        }
    }

    @Mixin(EntityRenderDispatcher.class)
    public static class EntityDispatcherMixin {
        @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
        public <E extends Entity> void inject$shouldRender(final E entity, final Frustum clippingHelper, final double x, final double y, final double z, final CallbackInfoReturnable<Boolean> cir) {

            final int distY;
            final int distX;
            final MobCategory category = entity.getType().getCategory();
            if (category == MobCategory.MONSTER) {
                if (!ChlorideConfig.culling.monsters) return;
                distY = ChlorideConfig.culling.monsterDistanceY;
                distX = ChlorideConfig.culling.monsterDistanceX;
            } else {
                if (!ChlorideConfig.culling.entities) return;
                distY = ChlorideConfig.culling.entityDistanceY;
                distX = ChlorideConfig.culling.entityDistanceX;
            }

            final boolean isWhitelisted = ((IRenderableEntity) entity.getType()).chloride$whitelisted();
            // IF IT NOT WHITELISTED AND (DISTX AND DISTY IS ZERO OR THE ENTITY IS NOT IN RANGE)
            // CANCEL RENDERING
            if (!isWhitelisted && (distX + distY == 0 || !EntityCulling.isEntityInRange(entity, x, y, z, distY, distX))) {
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

            this.chloride$whitelisted = EntityCulling.isWhitelisted(resource, this.getCategory() == MobCategory.MONSTER ? ChlorideConfig.culling.monsterWhitelist : ChlorideConfig.culling.entityWhitelist);
            this.chloride$checked = true;

            LOGGER.debug(e$IT,"Computed Entity whitelist with type {} for {}-{}", this.getCategory().name(), resource.toString(), this.chloride$whitelisted);
            return this.chloride$whitelisted;
        }

        @Unique
        public Identifier embPlus$resourceLocation() {
            return BuiltInRegistries.ENTITY_TYPE.getKey(this.embPlus$cast());
        }

        @Unique
        private EntityType<?> embPlus$cast() {
            return (EntityType<?>) ((Object) this);
        }
    }

    @Mixin(BlockEntityRenderDispatcher.class)
    public static class TileDispatcherMixin {
        @Shadow private Vec3 cameraPos;

        @Inject(at = @At("HEAD"), method = "tryExtractRenderState(Lnet/minecraft/world/level/block/entity/BlockEntity;FLnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;Lnet/minecraft/client/renderer/culling/Frustum;)Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;", cancellable = true)
        public <E extends BlockEntity, S extends BlockEntityRenderState> void render(final E tile, final float partialTick, final ModelFeatureRenderer.CrumblingOverlay breakProgress, final Frustum frustum, final CallbackInfoReturnable<S> cir) {
            if (!ChlorideConfig.culling.tileEntities) return;

            final boolean isWhitelisted = ((IRenderableEntity) tile.getType()).chloride$whitelisted();
            // IF IT NOT WHITELISTED AND (DISTX AND DISTY IS ZERO OR THE TILE IS NOT IN RANGE)
            // CANCEL RENDERING
            if (!isWhitelisted && (ChlorideConfig.culling.tileEntityDistanceY + ChlorideConfig.culling.tileEntityDistanceX == 0 || !EntityCulling.isEntityInRange(tile, this.cameraPos,
                    ChlorideConfig.culling.tileEntityDistanceY,
                    ChlorideConfig.culling.tileEntityDistanceX)
            )) {
                cir.setReturnValue(null);
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
            final Identifier resource = BlockEntityType.getKey((BlockEntityType<?>) ((Object) this));
            if (resource == null) {
                LOGGER.warn(e$IT, "BlockEntity key for '{}' is null, not whitelisting a broken mod block", this.getClass().getName());
                return false;
            }
            this.chloride$whitelisted = EntityCulling.isWhitelisted(resource, ChlorideConfig.culling.tileEntityWhitelist);
            this.chloride$checked = true;

            LOGGER.debug(e$IT,"Computed BlockEntity whitelist for {}-{}", resource.toString(), this.chloride$whitelisted);
            return this.chloride$whitelisted;
        }
    }
}
