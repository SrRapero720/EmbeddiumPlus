package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights.lightsource;

import me.srrapero720.dynamiclights.DynamicLightSource;
import me.srrapero720.dynamiclights.LambDynLights;
import me.srrapero720.dynamiclights.api.DynamicLightHandlers;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Adds the tick method for dynamic light source tracking in minecart entities.
 *
 * @author LambdAurora
 * @version 2.0.2
 * @since 1.3.2
 */
@Mixin(AbstractMinecart.class)
public abstract class AbstractMinecartEntityMixin extends Entity implements DynamicLightSource {
	@Shadow
	public abstract BlockState getDisplayBlockState();

	@Unique
	private int lambdynlights$luminance;

	public AbstractMinecartEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("HEAD"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.level().isClientSide()) {
			if (this.isRemoved()) {
				this.tdv$setDynamicLightEnabled(false);
			} else {
				if (!EmbeddiumPlusConfig.tileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.lambdynlights$luminance = 0;
				else
					this.tdv$dynamicLightTick();
				LambDynLights.updateTracking(this);
			}
		}
	}

	@Override
	public void tdv$dynamicLightTick() {
		this.lambdynlights$luminance = Math.max(
				Math.max(
						this.isOnFire() ? 15 : 0,
						this.getDisplayBlockState().getLightEmission()
				),
				DynamicLightHandlers.getLuminanceFrom(this)
		);
	}

	@Override
	public int tdv$getLuminance() {
		return this.lambdynlights$luminance;
	}
}
