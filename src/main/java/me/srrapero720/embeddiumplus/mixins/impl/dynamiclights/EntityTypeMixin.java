package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights;

import me.srrapero720.embeddiumplus.features.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.DynamicLightHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements DynamicLightHolder<T> {

	@Shadow private Component description;

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
