package me.srrapero720.embeddiumplus.mixins.impl.dynlights.lightsource;

import me.srrapero720.embeddiumplus.foundation.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.foundation.dynlights.accessors.DynamicLightSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractHurtingProjectile.class)
public abstract class ExplosiveProjectileEntityMixin extends Entity implements DynamicLightSource {
	public ExplosiveProjectileEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Override
	public void tdv$dynamicLightTick() {
		if (!this.tdv$isDynamicLightEnabled())
			this.tdv$setDynamicLightEnabled(true);
	}

	@Override
	public int tdv$getLuminance() {
		if (DynLightsHandlers.canLightUp(this))
			return 14;
		return 0;
	}
}
