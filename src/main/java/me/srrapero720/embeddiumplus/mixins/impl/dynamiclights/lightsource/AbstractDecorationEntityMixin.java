package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights.lightsource;

import me.srrapero720.dynamiclights.DynamicLightSource;
import me.srrapero720.dynamiclights.LambDynLights;
import me.srrapero720.dynamiclights.api.DynamicLightHandlers;
import me.srrapero720.embeddiumplus.EmbPlusConfig;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HangingEntity.class)
public abstract class AbstractDecorationEntityMixin extends Entity implements DynamicLightSource {
	public AbstractDecorationEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.getCommandSenderWorld().isClientSide()) {
			if (this.isRemoved()) {
				this.tdv$setDynamicLightEnabled(false);
			} else {
				if (!EmbPlusConfig.tileEntityLighting.get() || !DynamicLightHandlers.canLightUp(this))
					this.tdv$resetDynamicLight();
				else
					this.tdv$dynamicLightTick();
				LambDynLights.updateTracking(this);
			}
		}
	}
}
