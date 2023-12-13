/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.disabled720.dynamiclights.mixin;

import me.disabled720.dynamiclights.LambDynLights;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig.DynamicLightsQuality;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class CommonLevelRendererMixin {

	@Inject(
			method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I",
			at = @At("TAIL"),
			cancellable = true
	)
	private static void onGetLightmapCoordinates(BlockAndTintGetter world, BlockState j, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!world.getBlockState(pos).isSolidRender(world, pos) && EmbeddiumPlusConfig.dynQuality.get() != DynamicLightsQuality.OFF)
		{
			int vanilla = cir.getReturnValue();
			int value = LambDynLights.get().getLightmapWithDynamicLight(pos, vanilla);

			cir.setReturnValue(value);
		}

	}
}