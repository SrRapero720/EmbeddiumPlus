package me.srrapero720.embeddiumplus.mixins.impl.dynlights;

import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.foundation.dynlights.accessors.DynamicLightSource;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(Level.class)
public abstract class LevelMixin {
	@Shadow
	public abstract boolean isClientSide();

	@Shadow
	public abstract @Nullable BlockEntity getBlockEntity(BlockPos pos);

	@Inject(
			method = "tickBlockEntities",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/TickingBlockEntity;tick()V", shift = At.Shift.BEFORE),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	private void onBlockEntityTick(CallbackInfo ci, ProfilerFiller profiler, Iterator<BlockEntity> iterator, TickingBlockEntity blockEntityTickInvoker) {
		if (this.isClientSide() && EmbyConfig.dynLightsOnTileEntitiesCache) {
			var blockEntity = this.getBlockEntity(blockEntityTickInvoker.getPos());
			if (blockEntity != null)
				((DynamicLightSource) blockEntity).tdv$dynamicLightTick();
		}
	}
}