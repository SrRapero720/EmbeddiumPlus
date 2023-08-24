package vice.rubidium_extras.mixins.EntityDistance;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Ghast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.rubidium_extras.config.MagnesiumExtrasConfig;
import vice.rubidium_extras.util.DistanceUtility;

@Mixin(EntityRenderDispatcher.class)
public class MaxDistanceEntity
{
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, Frustum clippingHelper, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir)
    {
        if (!MagnesiumExtrasConfig.enableDistanceChecks.get())
            return;

        if (entity instanceof EnderDragon)
            return;

        if (entity instanceof Ghast)
            return;

        var name = entity.getClass().getName();
        if (name != null && name.startsWith("com.simibubi.create.content.contraptions"))
            return;

        if (!DistanceUtility.isEntityWithinDistance(
                entity,
                cameraX,
                cameraY,
                cameraZ,
                MagnesiumExtrasConfig.maxEntityRenderDistanceY.get(),
                MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get()
        ))
        {
            cir.cancel();
        }
    }
}

