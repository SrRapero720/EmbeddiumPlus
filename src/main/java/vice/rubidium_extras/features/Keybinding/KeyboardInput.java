package vice.rubidium_extras.features.Keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import vice.rubidium_extras.MagnesiumExtras;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class KeyboardInput
{
    public static final KeyMapping zoomKey = new KeyMapping("key.zoom",
            KeyConflictContext.IN_GAME,
            KeyModifier.NONE,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "key.category." + MagnesiumExtras.MODID);

}
