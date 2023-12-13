package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights.lightsource;

import me.srrapero720.dynamiclights.DynamicLightSource;
import me.srrapero720.dynamiclights.LambDynLights;
import me.srrapero720.dynamiclights.api.DynamicLightHandlers;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DynamicLightSource {
	@Final
	@Shadow
	protected BlockPos worldPosition;

	@Shadow
	@Nullable
	protected Level level;

	@Shadow
	protected boolean remove;

	@Unique
	private int luminance = 0;
	@Unique
	private int lastLuminance = 0;
	@Unique
	private long lastUpdate = 0;
	@Unique
	private final LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

	@Override
	public double tdv$getDynamicLightX() {
		return this.worldPosition.getX() + 0.5;
	}

	@Override
	public double tdv$getDynamicLightY() {
		return this.worldPosition.getY() + 0.5;
	}

	@Override
	public double tdv$getDynamicLightZ() {
		return this.worldPosition.getZ() + 0.5;
	}

	@Override
	public Level tdv$getDynamicLightWorld() {
		return this.level;
	}

	@Inject(method = "setRemoved", at = @At("TAIL"))
	private void onRemoved(CallbackInfo ci) {
		this.tdv$setDynamicLightEnabled(false);
	}

	@Override
	public void tdv$resetDynamicLight() {
		this.lastLuminance = 0;
	}

	@Override
	public void tdv$dynamicLightTick() {
		// We do not want to update the entity on the server.
		if (this.level == null || !this.level.isClientSide())
			return;
		if (!this.remove) {
			this.luminance = DynamicLightHandlers.getLuminanceFrom((BlockEntity) (Object) this);
			LambDynLights.updateTracking(this);

			if (!this.tdv$isDynamicLightEnabled()) {
				this.lastLuminance = 0;
			}
		}
	}

	@Override
	public int tdv$getLuminance() {
		return this.luminance;
	}

	@Unique
	private static long lambdynlights_lastUpdate = 0;

	@Override
	// TODO: fuze with me.disabled720.dynamiclights.mixin.lightsource.EntityMixin.tdv$shouldUpdateDynamicLight
	public boolean tdv$shouldUpdateDynamicLight() {
		long currentTime = System.currentTimeMillis();
		return switch (EmbeddiumPlusConfig.dynQuality.get()) {
			case OFF -> false;
			case SLOW -> currentTime < lambdynlights_lastUpdate + 500;
			case FAST -> currentTime < lambdynlights_lastUpdate + 200;
			case FASTEST -> currentTime < lambdynlights_lastUpdate + 100;
			default -> {
				lambdynlights_lastUpdate = currentTime;
				yield true;
			}
		};
	}

	@Override
	public boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer) {
		if (!this.tdv$shouldUpdateDynamicLight())
			return false;

		int luminance = this.tdv$getLuminance();

		if (luminance != this.lastLuminance) {
			this.lastLuminance = luminance;

			if (this.lambdynlights$trackedLitChunkPos.isEmpty()) {
				var chunkPos = new BlockPos.MutableBlockPos(Mth.floorDiv(this.worldPosition.getX(), 16),
						Mth.floorDiv(this.worldPosition.getY(), 16),
						Mth.floorDiv(this.worldPosition.getZ(), 16));

				LambDynLights.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);

				var directionX = (this.worldPosition.getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
				var directionY = (this.worldPosition.getY() & 15) >= 8 ? Direction.UP : Direction.DOWN;
				var directionZ = (this.worldPosition.getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

				for (int i = 0; i < 7; i++) {
					if (i % 4 == 0) {
						chunkPos.move(directionX); // X
					} else if (i % 4 == 1) {
						chunkPos.move(directionZ); // XZ
					} else if (i % 4 == 2) {
						chunkPos.move(directionX.getOpposite()); // Z
					} else {
						chunkPos.move(directionZ.getOpposite()); // origin
						chunkPos.move(directionY); // Y
					}
					LambDynLights.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);
				}
			}

			// Schedules the rebuild of chunks.
			this.tdv$lambdynlights$scheduleTrackedChunksRebuild(renderer);
			return true;
		}
		return false;
	}

	@Override
	public void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
		if (this.level == Minecraft.getInstance().level)
			for (long pos : this.lambdynlights$trackedLitChunkPos) {
				LambDynLights.scheduleChunkRebuild(renderer, pos);
			}
	}
}
