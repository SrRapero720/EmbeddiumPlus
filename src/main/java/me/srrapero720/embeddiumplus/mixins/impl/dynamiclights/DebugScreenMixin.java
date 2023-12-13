package me.srrapero720.embeddiumplus.mixins.impl.dynamiclights;

import me.srrapero720.dynamiclights.LambDynLights;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.DebugScreenOverlay;

/**
 * Adds a debug string for dynamic light sources tracking and updates.
 *
 * @author LambdAurora
 * @version 1.3.2
 * @since 1.3.2
 */
@Mixin(DebugScreenOverlay.class)
public class DebugScreenMixin {
	@Inject(method = "getGameInformation", at = @At("RETURN"))
	private void onGetLeftText(CallbackInfoReturnable<List<String>> cir) {
		var list = cir.getReturnValue();
		var ldl = LambDynLights.get();
		var builder = new StringBuilder("Dynamic Light Sources: ");
		builder.append(ldl.getLightSourcesCount())
				.append(" (U: ")
				.append(ldl.getLastUpdateCount());

		if (!LambDynLights.isEnabled()) {
			builder.append(" ; ");
			builder.append(ChatFormatting.RED);
			builder.append("Disabled");
			builder.append(ChatFormatting.RESET);
		}

		builder.append(')');
		list.add(builder.toString());
	}
}