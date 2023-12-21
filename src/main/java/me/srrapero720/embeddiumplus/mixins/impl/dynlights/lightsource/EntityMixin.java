package me.srrapero720.embeddiumplus.mixins.impl.dynlights.lightsource;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import me.srrapero720.embeddiumplus.internal.EmbPlusConfig;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.DynamicLightSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements DynamicLightSource {
	@Shadow
	public Level level;

	@Shadow
	public abstract double getX();

	@Shadow
	public abstract double getEyeY();

	@Shadow
	public abstract double getZ();

	@Shadow
	public abstract double getY();

	@Shadow
	public abstract boolean isOnFire();

	@Shadow
	public abstract EntityType<?> getType();

	@Shadow
	public abstract boolean isRemoved();

	@Shadow public abstract BlockPos blockPosition();

	@Shadow private ChunkPos chunkPosition;

	@Unique
	protected int lambdynlights$luminance = 0;
	@Unique
	private int lambdynlights$lastLuminance = 0;
	@Unique
	private long lambdynlights$lastUpdate = 0;
	@Unique
	private double lambdynlights$prevX;
	@Unique
	private double lambdynlights$prevY;
	@Unique
	private double lambdynlights$prevZ;
	@Unique
	private LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

	@Inject(method = "tick", at = @At("TAIL"))
	public void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.level.isClientSide()) {
			if (this.isRemoved()) {
				this.tdv$setDynamicLightEnabled(false);
			} else {
				this.tdv$dynamicLightTick();
				if ((!EmbPlusConfig.tileEntityLighting.get() && this.getType() != EntityType.PLAYER)
						|| !DynLightsHandlers.canLightUp((Entity) (Object) this))
					this.lambdynlights$luminance = 0;
				DynLightsPlus.updateTracking(this);
			}
		}
	}

	@Inject(method = "remove", at = @At("TAIL"))
	public void onRemove(CallbackInfo ci) {
		if (this.level.isClientSide())
			this.tdv$setDynamicLightEnabled(false);
	}

	@Override
	public double tdv$getDynamicLightX() {
		return this.getX();
	}

	@Override
	public double tdv$getDynamicLightY() {
		return this.getEyeY();
	}

	@Override
	public double tdv$getDynamicLightZ() {
		return this.getZ();
	}

	@Override
	public Level tdv$getDynamicLightWorld() {
		return this.level;
	}

	@Override
	public void tdv$resetDynamicLight() {
		this.lambdynlights$lastLuminance = 0;
	}


	private static long lambdynlights_lastUpdate = 0;

	@Override
	// TODO: fuze with me.disabled720.dynamiclights.mixin.lightsource.BlockEntityMixin.tdv$shouldUpdateDynamicLight
	public boolean tdv$shouldUpdateDynamicLight() {
		long currentTime = System.currentTimeMillis();
		boolean shouldNot = switch (EmbPlusConfig.dynQuality.get()) {
			case OFF -> true;
			case SLOW -> currentTime < lambdynlights_lastUpdate + 500;
			case FAST -> currentTime < lambdynlights_lastUpdate + 200;
			case FASTEST -> currentTime < lambdynlights_lastUpdate + 100;
			default -> false;
		};
		if (shouldNot) return false;
		lambdynlights_lastUpdate = currentTime;
		return true;
	}

	@Override
	public void tdv$dynamicLightTick() {
		this.lambdynlights$luminance = this.isOnFire() ? 15 : 0;

		int luminance = DynLightsHandlers.getLuminanceFrom((Entity) (Object) this);
		if (luminance > this.lambdynlights$luminance)
			this.lambdynlights$luminance = luminance;
	}

	@Override
	public int tdv$getLuminance() {
		return this.lambdynlights$luminance;
	}

	@Override
	public boolean tdv$lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer) {
		if (!this.tdv$shouldUpdateDynamicLight())
			return false;
		double deltaX = this.getX() - this.lambdynlights$prevX;
		double deltaY = this.getY() - this.lambdynlights$prevY;
		double deltaZ = this.getZ() - this.lambdynlights$prevZ;

		int luminance = this.tdv$getLuminance();

		if (Math.abs(deltaX) > 0.1D || Math.abs(deltaY) > 0.1D || Math.abs(deltaZ) > 0.1D || luminance != this.lambdynlights$lastLuminance) {
			this.lambdynlights$prevX = this.getX();
			this.lambdynlights$prevY = this.getY();
			this.lambdynlights$prevZ = this.getZ();
			this.lambdynlights$lastLuminance = luminance;

			var newPos = new LongOpenHashSet();

			if (luminance > 0) {
				var entityChunkPos = this.chunkPosition;
				var chunkPos = new BlockPos.MutableBlockPos(entityChunkPos.x, SectionPos.posToSectionCoord(this.getEyeY()), entityChunkPos.z);

				DynLightsPlus.scheduleChunkRebuild(renderer, chunkPos);
				DynLightsPlus.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);

				var directionX = (this.blockPosition().getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
				var directionY = (Mth.floor(this.getEyeY()) & 15) >= 8 ? Direction.UP : Direction.DOWN;
				var directionZ = (this.blockPosition().getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

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
					DynLightsPlus.scheduleChunkRebuild(renderer, chunkPos);
					DynLightsPlus.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);
				}
			}

			// Schedules the rebuild of removed chunks.
			this.tdv$lambdynlights$scheduleTrackedChunksRebuild(renderer);
			// Update tracked lit chunks.
			this.lambdynlights$trackedLitChunkPos = newPos;
			return true;
		}
		return false;
	}

	@Override
	public void tdv$lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
		if (Minecraft.getInstance().level == this.level)
			for (long pos : this.lambdynlights$trackedLitChunkPos) {
				DynLightsPlus.scheduleChunkRebuild(renderer, pos);
			}
	}
}
