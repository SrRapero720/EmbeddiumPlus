package vice.magnesium_extras.mixins.FadeInChunks;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;
import me.jellysquid.mods.sodium.common.config.SodiumConfig;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ShaderLoader.class, remap = false)
public abstract class ShaderLoaderMixin {
    @Shadow
    private static String getShaderPath(ResourceLocation name) {
        return null;
    }

    @Inject(method = "getShaderSource", at = @At("RETURN"), cancellable = true)
    private static void modifyShaderForFadeInEffect(String path, CallbackInfoReturnable<String> cir) {
        if (!SodiumClientMod.options().advanced.useChunkMultidraw || !MultidrawChunkRenderBackend.isSupported(false))
        {
            return;
        }

        boolean isVertexShader = path.equals(getShaderPath(new ResourceLocation("sodium", "chunk_gl20.v.glsl")));
        boolean isFragmentShader = path.equals(getShaderPath(new ResourceLocation("sodium", "chunk_gl20.f.glsl")));
        if (!isVertexShader && !isFragmentShader) {
            return;
        }

        StringBuilder source = new StringBuilder(cir.getReturnValue());
        prepend(source, "varying", "varying float v_FadeInProgress;");
        if (isVertexShader) {
            prepend(source, "v_Color = ", "v_FadeInProgress = d_ModelOffset.w;");
        }
        if (isFragmentShader) {
            // 0 is max fog, 1 is no fog
            replace(source, "(getFogFactor(),", "(min(v_FadeInProgress, getFogFactor()),");
        }
        cir.setReturnValue(source.toString());
    }

    private static void replace(StringBuilder buffer, String search, String str) {
        int idx = buffer.indexOf(search);
        buffer.replace(idx, idx + search.length(), str);
    }

    private static void prepend(StringBuilder buffer, String search, String str) {
        replace(buffer, search, str + search);
    }
}
