package me.srrapero720.embeddiumplus.mixins.impl.dynlights.lightsource;

import me.srrapero720.embeddiumplus.EmbPlusConfig;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsHandlers;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.DynamicLightSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {
	@Shadow
	public abstract int getFuse();

	@Unique
	private int embeddiumPlus$startFuseTimer = 80;
	@Unique
	private int lambdynlights$luminance;

	public TntEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
	private void onNew(EntityType<? extends PrimedTnt> entityType, Level world, CallbackInfo ci) {
		this.embeddiumPlus$startFuseTimer = this.getFuse();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getCommandSenderWorld().isClientSide()) {
			if (!DynLightsPlus.isEnabled())
				return;

			if (this.isRemoved()) {
				this.tdv$setDynamicLightEnabled(false);
			} else {
				if (!EmbPlusConfig.tileEntityLighting.get() || !DynLightsHandlers.canLightUp(this))
					this.tdv$resetDynamicLight();
				else
					this.tdv$dynamicLightTick();
				DynLightsPlus.updateTracking(this);
			}
		}
	}

	@Override
	public void tdv$dynamicLightTick() {
		if (this.isOnFire()) {
			this.lambdynlights$luminance = 15;
		} else {
			if (DynLightsPlus.isEnabled()) {
				var fuse = this.getFuse() / this.embeddiumPlus$startFuseTimer;
				this.lambdynlights$luminance = (int) (-(fuse * fuse) * 10.0) + 10;
			} else {
				this.lambdynlights$luminance = 10;
			}
		}
	}

	@Override
	public int tdv$getLuminance() {
		return this.lambdynlights$luminance;
	}
}
