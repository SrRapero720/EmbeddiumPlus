package vice.magnesium_extras.mixins.BorderlessFullscreen;

import net.minecraft.client.MainWindow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(MainWindow.class)
public interface MainWindowAccessor
{
    @Accessor("dirty")
    public void setDirty(boolean value);
}