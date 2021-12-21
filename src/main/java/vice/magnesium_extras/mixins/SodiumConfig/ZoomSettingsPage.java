package vice.magnesium_extras.mixins.SodiumConfig;


import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import me.jellysquid.mods.sodium.client.gui.SodiumOptionsGUI;
import me.jellysquid.mods.sodium.client.gui.options.*;
import me.jellysquid.mods.sodium.client.gui.options.control.CyclingControl;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.gui.options.storage.SodiumOptionsStorage;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Mixin(SodiumOptionsGUI.class)
public abstract class ZoomSettingsPage
{

    @Shadow
    @Final
    private List<OptionPage> pages;
    private static final SodiumOptionsStorage sodiumOpts = new SodiumOptionsStorage();


    @Inject(method = "<init>", at = @At("RETURN"))
    private void DynamicLights(Screen prevScreen, CallbackInfo ci)
    {
        List<OptionGroup> groups = new ArrayList();

        //OptionImpl<SodiumGameOptions, Boolean> lowerSensitivity = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
        //        .setName("Lower Zoom Sensitivity")
        //        .setTooltip("Lowers your sensitivity when zooming to make it feel more consistent.")
        //        .setControl(TickBoxControl::new)
        //        .setBinding(
        //                (options, value) -> MagnesiumExtrasConfig.lowerZoomSensitivity.set(value),
        //                (options) -> MagnesiumExtrasConfig.lowerZoomSensitivity.get())
        //        .setImpact(OptionImpact.LOW)
        //        .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomScrolling = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("Zoom Scrolling")
                .setTooltip("Allows using scroll wheel to adjust zoom amount.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.zoomScrolling.set(value),
                        (options) -> MagnesiumExtrasConfig.zoomScrolling.get())
                .setImpact(OptionImpact.LOW)
                .build();

        OptionImpl<SodiumGameOptions, Boolean> zoomOverlay = OptionImpl.createBuilder(Boolean.class, sodiumOpts)
                .setName("Zoom Overlay")
                .setTooltip("Renders a vignette overlay when zooming.")
                .setControl(TickBoxControl::new)
                .setBinding(
                        (options, value) -> MagnesiumExtrasConfig.zoomOverlay.set(value),
                        (options) -> MagnesiumExtrasConfig.zoomOverlay.get())
                .setImpact(OptionImpact.LOW)
                .build();

        groups.add(OptionGroup
                .createBuilder()
                //.add(lowerSensitivity)
                .add(zoomScrolling)
                .add(zoomOverlay)
                .build()
        );



        Option<MagnesiumExtrasConfig.ZoomTransitionOptions> zoomTransition =  OptionImpl.createBuilder(MagnesiumExtrasConfig.ZoomTransitionOptions.class, sodiumOpts)
                .setName("Zoom Transition Mode")
                .setTooltip("Controls how the game changes from normal to zoomed. Off will be an instant transition.")
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.ZoomTransitionOptions.class, new String[] { "Off", "Smooth" }))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.zoomTransition.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.ZoomTransitionOptions.valueOf(MagnesiumExtrasConfig.zoomTransition.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        Option<MagnesiumExtrasConfig.ZoomModes> zoomMode =  OptionImpl.createBuilder(MagnesiumExtrasConfig.ZoomModes.class, sodiumOpts)
                .setName("Zoom Keybind Mode")
                .setTooltip("Hold - Zoom only while the key is down.\nToggle - Lock zoom until you press the key again\nPersistent - Always zoom, if you want that, for some reason.")
                .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.ZoomModes.class, new String[] { "Hold", "Toggle", "Persistent"}))
                .setBinding(
                        (opts, value) -> MagnesiumExtrasConfig.zoomMode.set(value.toString()),
                        (opts) -> MagnesiumExtrasConfig.ZoomModes.valueOf(MagnesiumExtrasConfig.zoomMode.get()))
                .setImpact(OptionImpact.LOW)
                .build();

        //Option<MagnesiumExtrasConfig.CinematicCameraOptions> cinematicCameraMode =  OptionImpl.createBuilder(MagnesiumExtrasConfig.CinematicCameraOptions.class, sodiumOpts)
        //        .setName("Cinematic Camera Options")
        //        .setTooltip("Cinematic Camera Mode")
        //        .setControl((option) -> new CyclingControl<>(option, MagnesiumExtrasConfig.CinematicCameraOptions.class, new String[] { "Off", "Vanilla", "Multiplied"}))
        //        .setBinding(
        //                (opts, value) -> MagnesiumExtrasConfig.cinematicCameraMode.set(value.toString()),
        //                (opts) -> MagnesiumExtrasConfig.CinematicCameraOptions.valueOf(MagnesiumExtrasConfig.cinematicCameraMode.get()))
        //        .setImpact(OptionImpact.LOW)
        //        .build();

        groups.add(OptionGroup
                .createBuilder()
                .add(zoomTransition)
                .add(zoomMode)
                //.add(cinematicCameraMode)
                .build()
        );


        pages.add(new OptionPage("Zoom", ImmutableList.copyOf(groups)));
    }
}