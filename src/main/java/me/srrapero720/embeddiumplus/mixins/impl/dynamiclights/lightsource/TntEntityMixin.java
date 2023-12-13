package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights.lightsource;

import me.srrapero720.dynamiclights.DynamicLightSource;
import me.srrapero720.dynamiclights.LambDynLights;
import me.srrapero720.dynamiclights.api.DynamicLightHandlers;
import me.srrapero720.embeddiumplus.config.EmbeddiumPlusConfig;
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
			if (!LambDynLights.isEnabled())
				return;

			if (this.isRemoved()) {
				this.tdv$setDynamicLightEnabled(false);
			} else {
				if (!EmbeddiumPlusConfig.tileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.tdv$resetDynamicLight();
				else
					this.tdv$dynamicLightTick();
				LambDynLights.updateTracking(this);
			}
		}
	}

	@Override
	public void tdv$dynamicLightTick() {
		if (this.isOnFire()) {
			this.lambdynlights$luminance = 15;
		} else {
			if (LambDynLights.isEnabled()) {
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
