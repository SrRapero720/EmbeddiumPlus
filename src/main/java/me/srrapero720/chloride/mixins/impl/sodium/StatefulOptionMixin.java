package me.srrapero720.chloride.mixins.impl.sodium;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.srrapero720.chloride.ChlorideConfig;
import net.caffeinemc.mods.sodium.api.config.option.OptionFlag;
import net.caffeinemc.mods.sodium.client.config.structure.StatefulOption;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashSet;
import java.util.Set;

@Mixin(StatefulOption.class)
public class StatefulOptionMixin {
    private static final Identifier GRAPHICS_API = Identifier.parse("sodium:general.graphics_api");

    @ModifyReturnValue(method = "getFlags", at = @At("RETURN"))
    private Set<Identifier> chloride$liveGraphicsApi(final Set<Identifier> flags) {
        final Identifier restart = OptionFlag.REQUIRES_GAME_RESTART.getId();
        if (flags == null || !ChlorideConfig.hotswap || !flags.contains(restart)) return flags;
        if (!GRAPHICS_API.equals(((OptionAccessor) this).chloride$id())) return flags;
        final Set<Identifier> filtered = new HashSet<>(flags);
        filtered.remove(restart);
        return filtered;
    }
}
