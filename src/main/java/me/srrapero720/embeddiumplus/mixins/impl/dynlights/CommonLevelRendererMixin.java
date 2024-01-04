package me.srrapero720.embeddiumplus.mixins.impl.dynlights;

import me.srrapero720.embeddiumplus.foundation.dynlights.DynLightsPlus;
import me.srrapero720.embeddiumplus.EmbyConfig;
import me.srrapero720.embeddiumplus.EmbyConfig.DynLightsSpeed;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelRenderer.class, priority = 800)
public abstract class CommonLevelRendererMixin {
	@Inject(
			method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I",
			at = @At("TAIL"),
			cancellable = true
	)
	private static void onGetLightMapCoordinates(BlockAndTintGetter world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!world.getBlockState(pos).isSolidRender(world, pos) && EmbyConfig.dynLightSpeed.get() != DynLightsSpeed.OFF) {
			int vanilla = cir.getReturnValue();
			int value = DynLightsPlus.get().getLightmapWithDynamicLight(pos, vanilla);

			cir.setReturnValue(value);
		}

	}
}