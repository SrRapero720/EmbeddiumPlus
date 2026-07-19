package me.srrapero720.chloride.mixins.impl.darkness;

import com.mojang.blaze3d.vertex.PoseStack;
import me.srrapero720.chloride.impl.Darkness;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final public LightTexture lightTexture;
    @Shadow @Final public Minecraft minecraft;

    @Inject(method = "renderLevel", at = @At(value = "HEAD"))
    private void inject$renderLevel(final float tickDelta, final long nanos, final PoseStack matrixStack, final CallbackInfo ci) {
        if (this.lightTexture.updateLightTexture) {
            this.minecraft.getProfiler().push("darkenLightTexture");
            Darkness.updateLuminance(tickDelta, this.minecraft, (GameRenderer) (Object) this, this.lightTexture.blockLightRedFlicker);
            this.minecraft.getProfiler().pop();
        }
    }
}