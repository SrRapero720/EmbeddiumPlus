package vice.magnesium_extras.features.Zoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vice.magnesium_extras.MagnesiumExtras;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.keybinds.KeyboardInput;

@Mod.EventBusSubscriber(modid = "magnesium_extras", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZoomHandler {
    //Used internally in order to make zoom toggling possible.
    private static boolean lastZoomPress = false;

    //Used internally in order to make persistent zoom less buggy.
    private static boolean persistentZoom = false;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        //Handle zoom mode changes.
        if (!MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.HOLD.toString())) {
            if (!persistentZoom) {
                persistentZoom = true;
                lastZoomPress = true;
                ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
            }
        } else {
            if (persistentZoom) {
                persistentZoom = false;
                lastZoomPress = true;
            }
        }

        //If the press state is the same as the previous tick's, cancel the rest. Makes toggling usable and the zoom divisor adjustable.
        if (KeyboardInput.zoomKey.isDown() == lastZoomPress) {
            return;
        }

        if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.HOLD.toString()))
        {
            //If the zoom needs to be held, then the zoom signal is determined by if the key is pressed or not.
            ZoomUtils.zoomState = KeyboardInput.zoomKey.isDown();
            ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
        }
        else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.TOGGLE.toString()))
        {
            //If the zoom needs to be toggled, toggle the zoom signal instead.
            if (KeyboardInput.zoomKey.isDown()) {
                ZoomUtils.zoomState = !ZoomUtils.zoomState;
                ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
            }
        }
        else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.PERSISTENT.toString()))
        {
            //If persistent zoom is enabled, just keep the zoom on.
            ZoomUtils.zoomState = true;
        }

        //Manage the post-zoom signal.
        ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress;

        //Set the previous zoom signal for the next tick.
        lastZoomPress = KeyboardInput.zoomKey.isDown();
    }
}