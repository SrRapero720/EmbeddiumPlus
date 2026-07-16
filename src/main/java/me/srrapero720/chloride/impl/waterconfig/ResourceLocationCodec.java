package me.srrapero720.chloride.impl.waterconfig;

import me.srrapero720.waterconfig.api.ICodec;
import net.minecraft.resources.ResourceLocation;

/**
 * WaterConfig codec for {@link ResourceLocation} fields, registered via
 * {@code META-INF/services/me.srrapero720.waterconfig.api.ICodec}.
 */
public class ResourceLocationCodec implements ICodec<ResourceLocation> {
    @Override
    public String encode(final ResourceLocation instance) {
        return instance.toString();
    }

    @Override
    public ResourceLocation decode(final String value) {
        // ':*' IS THE LEGACY SPELLING OF THE ':all' NAMESPACE WILDCARD
        return ResourceLocation.tryParse(value.replace(":*", ":all"));
    }

    @Override
    public Class<ResourceLocation> type() {
        return ResourceLocation.class;
    }
}
