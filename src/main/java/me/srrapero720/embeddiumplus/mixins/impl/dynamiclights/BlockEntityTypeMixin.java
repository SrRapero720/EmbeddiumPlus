package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights;

import me.srrapero720.embeddiumplus.features.dynamic_lights.DynamicLightHandlerHolder;
import me.srrapero720.dynamiclights.api.DynamicLightHandler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHandlerHolder<T> {
	@Unique
	private DynamicLightHandler<T> lambdynlights$lightHandler;

	@Override
	public @Nullable DynamicLightHandler<T> lambdynlights$getDynamicLightHandler() {
		return this.lambdynlights$lightHandler;
	}

	@Override
	public void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
		this.lambdynlights$lightHandler = handler;
	}
}
