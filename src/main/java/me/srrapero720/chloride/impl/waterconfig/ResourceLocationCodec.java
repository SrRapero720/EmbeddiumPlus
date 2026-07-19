package me.srrapero720.chloride.impl.waterconfig;

import me.srrapero720.waterconfig.api.ICodec;
import net.minecraft.resources.Identifier;

/**
 * WaterConfig codec for {@link Identifier} fields, registered via
 * {@code META-INF/services/me.srrapero720.waterconfig.api.ICodec}.
 */
public class ResourceLocationCodec implements ICodec<Identifier> {
    @Override
    public String encode(final Identifier instance) {
        return instance.toString();
    }

    @Override
    public Identifier decode(final String value) {
        // ':*' IS THE LEGACY SPELLING OF THE ':all' NAMESPACE WILDCARD
        return Identifier.tryParse(value.replace(":*", ":all"));
    }

    @Override
    public Class<Identifier> type() {
        return Identifier.class;
    }
}
