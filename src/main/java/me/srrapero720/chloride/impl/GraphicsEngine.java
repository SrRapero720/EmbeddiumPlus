package me.srrapero720.chloride.impl;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.opengl.GlBackend;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.IconSet;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextInputManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.shaders.GpuDebugOptions;
import com.mojang.blaze3d.systems.BackendCreationException;
import com.mojang.blaze3d.systems.DeviceInfo;
import com.mojang.blaze3d.systems.GpuBackend;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.TimerQuery;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vulkan.VulkanBackend;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.mixins.impl.HotswapMixins;
import me.srrapero720.chloride.mixins.impl.sodium.DrawBackendAccessor;
import me.srrapero720.chloride.mixins.impl.sodium.GlBufferArenaAccessor;
import me.srrapero720.chloride.mixins.impl.sodium.SodiumWorldRendererInvoker;
import net.caffeinemc.mods.sodium.client.render.SodiumWorldRenderer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.PreferredGraphicsApi;
import net.minecraft.client.ResourceLoadStateTracker;
import net.minecraft.client.renderer.CloudRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.ProjectionMatrixBuffer;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.WorldBorderRenderer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.util.List;
import java.util.OptionalInt;
import java.util.concurrent.CompletableFuture;

import static me.srrapero720.chloride.Chloride.LOGGER;

public class GraphicsEngine {
    public static final Marker IT = MarkerManager.getMarker("GraphicsEngine");
    private static final String AUTO_SWAP = System.getenv("CHLORIDE_HOTSWAP_AUTO");

    private static boolean irisSwapNotified;
    private static boolean fullscreenReconciled;
    private static long autoSwapAt = AUTO_SWAP != null && FabricLoader.getInstance().isDevelopmentEnvironment() ? 0 : -1;

    public static void onFrame() {
        final Minecraft mc = Minecraft.getInstance();
        if (mc.gui.overlay() != null) return;

        if (!fullscreenReconciled) {
            fullscreenReconciled = true;
            final Borderless.Mode mode = ChlorideConfig.fullscreen.mode;
            final boolean configFullscreen = mode != Borderless.Mode.WINDOWED;
            if (mc.options.fullscreen().get() != configFullscreen
                    || mc.options.exclusiveFullscreen().get() != (mode == Borderless.Mode.EXCLUSIVE_FULLSCREEN)
                    || mc.window.isFullscreen() != configFullscreen) {
                Borderless.setFullScreenMode(mode);
            }
        }

        if (autoSwapAt == 0) {
            final String[] parts = AUTO_SWAP.split(":");
            autoSwapAt = Util.getMillis() + (parts.length > 1 ? Long.parseLong(parts[1]) * 1000L : 10000L);
        } else if (autoSwapAt > 0 && Util.getMillis() >= autoSwapAt) {
            autoSwapAt = -1;
            mc.options.preferredGraphicsBackend().set(PreferredGraphicsApi.valueOf(AUTO_SWAP.split(":")[0].toUpperCase()));
            LOGGER.info(IT, "Auto hotswap trigger fired, requesting {}", mc.options.preferredGraphicsBackend().get());
        }

        if (!ChlorideConfig.hotswap) return;

        final PreferredGraphicsApi api = mc.options.preferredGraphicsBackend().get();
        final boolean isVulkan = mc.window.backend() instanceof VulkanBackend;
        final boolean wantVulkan = api == PreferredGraphicsApi.DEFAULT ? isVulkan : api == PreferredGraphicsApi.VULKAN;
        if (wantVulkan == isVulkan && mc.options.exclusiveFullscreen().get() == mc.window.exclusiveFullscreen) {
            irisSwapNotified = false;
            return;
        }

        if (Chloride.installed("iris") && !isVulkan) {
            if (!irisSwapNotified) {
                irisSwapNotified = true;
                LOGGER.warn(IT, "Iris hooks OpenGL internals and can't survive rebuilding an active OpenGL context; the settings are kept and apply on the next launch");
            }
            return;
        }

        try {
            swap(mc, wantVulkan);
        } catch (final Throwable t) {
            LOGGER.error(IT, "Graphics backend hotswap failed", t);
            throw t;
        }
    }

    private static void swap(final Minecraft mc, final boolean toVulkan) {
        if (toVulkan) {
            final BackendCreationException unavailable = VulkanBackend.checkBackendAvailable();
            if (unavailable != null) {
                LOGGER.error(IT, "Vulkan backend unavailable, keeping current backend", unavailable);
                revert(mc);
                return;
            }
        }
        disableIrisShaders();

        final long start = Util.getMillis();
        final Window oldWindow = mc.window;
        final LevelRenderer lr = mc.levelRenderer;
        final CloudRenderer clouds = lr.cloudRenderer();
        final int width = oldWindow.getWidth();
        final int height = oldWindow.getHeight();
        final boolean fullscreen = mc.options.fullscreen().get();
        final boolean grabbed = mc.mouseHandler.isMouseGrabbed();
        final int cloudUboSize = (int) clouds.ubo.currentBuffer().size();

        final SodiumWorldRenderer sodium = SodiumWorldRenderer.instanceNullable();
        if (sodium != null) ((SodiumWorldRendererInvoker) sodium).invokeDeleteRendererState();
        final GpuBuffer[] pooled = GlBufferArenaAccessor.freeBuffers();
        for (int i = 0; i < pooled.length; i++) {
            if (pooled[i] == null) continue;
            pooled[i].close();
            pooled[i] = null;
        }
        GlBufferArenaAccessor.setFreeBufferCount(0);

        lr.resetLevelRenderData();
        if (lr.skyRenderer != null) {
            lr.skyRenderer.close();
            lr.skyRenderer = null;
        }
        lr.chunkLayerSampler = null;
        lr.entityOutlineTarget.destroyBuffers();
        lr.worldBorderRenderer.close();
        clouds.ubo.close();
        if (clouds.utb != null) {
            clouds.utb.close();
            clouds.utb = null;
        }
        mc.gameRenderer.close();
        mc.getShaderManager().postChainProjectionMatrixBuffer.close();
        mc.timerQuery.close();
        for (final AbstractTexture texture : mc.getTextureManager().byPath.values()) texture.releaseTextures();

        mc.windowSurface.close();
        while (!HotswapMixins.RenderSystemAccessor.pendingFences().isEmpty()) RenderSystem.executePendingTasks();
        RenderSystem.shutdownRenderer();
        HotswapMixins.RenderSystemAccessor.setDevice(null);
        HotswapMixins.RenderSystemAccessor.setProjectionMatrixBuffer(null);
        HotswapMixins.RenderSystemAccessor.setSavedProjectionMatrixBuffer(null);
        HotswapMixins.RenderSystemAccessor.setShaderFog(null);
        HotswapMixins.RenderSystemAccessor.setShaderLightDirections(null);
        HotswapMixins.RenderSystemAccessor.setGlobalSettingsUniform(null);
        for (final RenderSystem.AutoStorageIndexBuffer shared : new RenderSystem.AutoStorageIndexBuffer[]{
                HotswapMixins.RenderSystemAccessor.sharedSequential(),
                HotswapMixins.RenderSystemAccessor.sharedSequentialQuad(),
                HotswapMixins.RenderSystemAccessor.sharedSequentialLines()}) {
            final HotswapMixins.AutoIndexBufferAccessor accessor = (HotswapMixins.AutoIndexBufferAccessor) (Object) shared;
            accessor.setBuffer(null);
            accessor.setIndexCount(0);
        }

        oldWindow.close();

        Window window = null;
        GpuDevice device = null;
        final boolean debugGl = false;
        final GpuDebugOptions debugOptions = new GpuDebugOptions(mc.options.glDebugVerbosity, debugGl, debugGl, false);
        final DisplayData displayData = new DisplayData(width, height, OptionalInt.empty(), OptionalInt.empty(), fullscreen);
        final GpuBackend[] candidates = toVulkan
                ? new GpuBackend[]{new VulkanBackend(), new GlBackend()}
                : new GpuBackend[]{new GlBackend()};

        for (final GpuBackend backend : candidates) {
            try {
                GLFW.glfwDefaultWindowHints();
                GLFW.glfwWindowHint(131088, GLX.glfwBool(!mc.options.exclusiveFullscreen().get()));
                window = new Window(mc, displayData, mc.options.fullscreenVideoModeString, mc.options.exclusiveFullscreen().get(), mc.createTitle(), mc.monitorManager, backend);
                device = backend.createDevice(window.handle(), (id, type) -> mc.getShaderManager().getShader(id, type), debugOptions, mc::loadCriticalShaders);
                break;
            } catch (final BackendCreationException e) {
                LOGGER.error(IT, "Failed to create {} backend during hotswap", backend.getName(), e);
                if (window != null) {
                    window.close();
                    window = null;
                }
            }
        }
        if (window == null || device == null) throw new IllegalStateException("Hotswap could not create any graphics backend");

        final DeviceInfo info = device.getDeviceInfo();
        final int maxTexture = info.limits().maxTextureSizeForFormat(GpuFormat.RGBA8_UNORM);
        GLFW.glfwSetWindowSizeLimits(window.handle(), -1, -1, maxTexture, maxTexture);
        try {
            RenderSystem.initRenderer(device);
        } catch (final Throwable t) {
            if (RenderSystem.tryGetDevice() != device) throw t;
            LOGGER.warn(IT, "A third-party initRenderer hook failed during hotswap, continuing", t);
        }
        RenderSystem.setupDefaultState();
        resetGlStateCaches();

        mc.window = window;
        mc.windowSurface = device.createSurface(window.handle());
        GLFW.glfwShowWindow(window.handle());
        mc.textInputManager = new TextInputManager(window);
        mc.windowSurfaceNeedsReconfiguring = true;
        mc.surfaceIsInvalid = false;
        mc.mouseHandler.setup(window);
        mc.keyboardHandler.setup(window);
        try {
            window.setIcon(mc.vanillaPackResources, SharedConstants.getCurrentVersion().stable() ? IconSet.RELEASE : IconSet.SNAPSHOT);
        } catch (final IOException e) {
            LOGGER.warn(IT, "Couldn't restore the window icon", e);
        }
        window.setGuiScale(window.calculateScale(mc.options.guiScale().get(), mc.isEnforceUnicode()));

        mc.timerQuery = new TimerQuery();
        mc.getShaderManager().postChainProjectionMatrixBuffer = new ProjectionMatrixBuffer("post");
        final GameRenderer oldRenderer = mc.gameRenderer;
        mc.gameRenderer = new GameRenderer(mc, mc.getEntityRenderDispatcher().getItemInHandRenderer(), mc.getModelManager());
        mc.gameRenderer.mainCamera = oldRenderer.mainCamera;
        mc.gameRenderer.registerPanoramaTextures(mc.getTextureManager());
        final GameRenderState state = mc.gameRenderer.gameRenderState();
        state.windowRenderState.width = window.getWidth();
        state.windowRenderState.height = window.getHeight();
        state.framerateLimit = mc.getFramerateLimitTracker().getFramerateLimit();
        mc.gameRenderer.globalSettingsUniform.update(window.getWidth(), window.getHeight(), 1.0, 0L, mc.getDeltaTracker(), 0, Vec3.ZERO, false);

        lr.gameRenderer = mc.gameRenderer;
        lr.renderBuffers = mc.gameRenderer.renderBuffers();
        lr.featureRenderDispatcher = mc.gameRenderer.featureRenderDispatcher();
        lr.levelRenderState = state.levelRenderState;
        lr.optionsRenderState = state.optionsRenderState;
        lr.worldBorderRenderer = new WorldBorderRenderer();
        lr.weatherEffectRenderer = new WeatherEffectRenderer();
        clouds.ubo = new MappableRingBuffer(() -> "Cloud UBO", 130, cloudUboSize);
        mc.levelExtractor.levelRenderState = state.levelRenderState;
        mc.gui.guiRenderState = state.guiRenderState;

        DrawBackendAccessor.setBackend(DrawBackendAccessor.invokeChooseBackend());

        for (final AbstractTexture texture : mc.getTextureManager().byPath.values()) {
            if (!(texture instanceof DynamicTexture dynamic) || dynamic.getPixels() == null) continue;
            final NativeImage pixels = dynamic.getPixels();
            dynamic.texture = device.createTexture(() -> "chloride hotswapped", 5, GpuFormat.RGBA8_UNORM, pixels.getWidth(), pixels.getHeight(), 1, 1);
            dynamic.sampler = RenderSystem.getSamplerCache().getRepeat(FilterMode.NEAREST);
            dynamic.textureView = device.createTextureView(dynamic.texture);
            dynamic.upload();
        }

        mc.gameRenderer.resize(window.getWidth(), window.getHeight());

        final List<PackResources> packs = mc.getResourcePackRepository().openAllSelected();
        mc.reloadStateTracker.startReload(ResourceLoadStateTracker.ReloadReason.MANUAL, packs);
        final ReloadInstance reload = ((ReloadableResourceManager) mc.getResourceManager())
                .createReload(Util.backgroundExecutor().forName("resourceLoad"), mc, CompletableFuture.completedFuture(Unit.INSTANCE), packs);
        mc.managedBlock(reload::isDone);
        try {
            reload.checkExceptions();
        } catch (final Throwable t) {
            LOGGER.error(IT, "Resource reload after hotswap reported errors", t);
        }
        mc.reloadStateTracker.finishReload();
        mc.levelExtractor.allChanged();
        if (grabbed) mc.mouseHandler.grabMouse();
        mc.options.preferredGraphicsBackendFromStartup = mc.options.preferredGraphicsBackend().get();
        mc.options.exclusiveFullscreenFromStartup = mc.options.exclusiveFullscreen().get();
        mc.options.save();
        LOGGER.info(IT, "Graphics backend hotswapped to {} ({}) in {}ms", info.backendName(), info.name(), Util.getMillis() - start);
    }

    private static void resetGlStateCaches() {
        GlStateManager.readFbo = 0;
        GlStateManager.writeFbo = 0;
        GlStateManager.activeTexture = 0;
        java.util.Arrays.fill(GlStateManager.COLOR_MASK, 0);
        for (final GlStateManager.TextureState texture : GlStateManager.TEXTURES) texture.binding = 0;
        for (final GlStateManager.BlendState blend : GlStateManager.BLEND) {
            if (blend == null) continue;
            blend.mode.enabled = false;
            blend.srcRgb = 1;
            blend.dstRgb = 0;
            blend.modeRgb = 32774;
            blend.srcAlpha = 1;
            blend.dstAlpha = 0;
            blend.modeAlpha = 32774;
        }
        GlStateManager.DEPTH.mode.enabled = false;
        GlStateManager.DEPTH.mask = true;
        GlStateManager.DEPTH.func = 513;
        GlStateManager.CULL.enable.enabled = false;
        GlStateManager.POLY_OFFSET.fill.enabled = false;
        GlStateManager.POLY_OFFSET.factor = 0.0F;
        GlStateManager.POLY_OFFSET.units = 0.0F;
        GlStateManager.COLOR_LOGIC.enable.enabled = false;
        GlStateManager.COLOR_LOGIC.op = 5379;
        GlStateManager.SCISSOR.mode.enabled = false;
    }

    private static void revert(final Minecraft mc) {
        mc.options.preferredGraphicsBackend().set(mc.window.backend() instanceof VulkanBackend ? PreferredGraphicsApi.VULKAN : PreferredGraphicsApi.OPENGL);
        mc.options.save();
    }

    private static void disableIrisShaders() {
        if (!Chloride.installed("iris")) return;
        try {
            final Class<?> api = Class.forName("net.irisshaders.iris.api.v0.IrisApi");
            final Object config = api.getMethod("getConfig").invoke(api.getMethod("getInstance").invoke(null));
            final Class<?> cfg = Class.forName("net.irisshaders.iris.api.v0.IrisApiConfig");
            if ((Boolean) cfg.getMethod("areShadersEnabled").invoke(config)) {
                cfg.getMethod("setShadersEnabledAndApply", boolean.class).invoke(config, false);
                LOGGER.info(IT, "Iris shaders disabled before swapping to Vulkan");
            }
        } catch (final Throwable t) {
            LOGGER.warn(IT, "Couldn't disable Iris shaders before the swap", t);
        }
    }
}
