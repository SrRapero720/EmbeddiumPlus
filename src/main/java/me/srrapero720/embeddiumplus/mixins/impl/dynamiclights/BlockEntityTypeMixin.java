package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights;

import me.srrapero720.embeddiumplus.features.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.DynamicLightHolder;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHolder<T> {
	@Unique
	private DynLightsHandlers.Entry<T> lambdynlights$lightHandler;

	@Override
	public @Nullable DynLightsHandlers.Entry<T> lambdynlights$getDynamicLightHandler() {
		return this.lambdynlights$lightHandler;
	}

	@Override
	public void lambdynlights$setDynamicLightHandler(DynLightsHandlers.Entry<T> handler) {
		this.lambdynlights$lightHandler = handler;
	}
}
