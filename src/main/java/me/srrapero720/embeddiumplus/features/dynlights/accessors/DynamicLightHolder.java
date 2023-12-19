package me.srrapero720.embeddiumplus.features.dynlights.accessors;

import me.srrapero720.embeddiumplus.features.dynlights.DynLightsHandlers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@ApiStatus.NonExtendable
public interface DynamicLightHolder<T> {
	@Nullable DynLightsHandlers.Entry<T> lambdynlights$getDynamicLightHandler();

	void lambdynlights$setDynamicLightHandler(DynLightsHandlers.Entry<T> handler);

	@SuppressWarnings("unchecked")
	static <T extends Entity> DynamicLightHolder<T> cast(EntityType<T> entityType) {
		return (DynamicLightHolder<T>) entityType;
	}

	@SuppressWarnings("unchecked")
	static <T extends BlockEntity> DynamicLightHolder<T> cast(BlockEntityType<T> entityType) {
		return (DynamicLightHolder<T>) entityType;
	}
}
