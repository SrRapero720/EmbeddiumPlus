package me.srrapero720.embeddiumplus.mixins.impl.entitydistance;

import me.srrapero720.embeddiumplus.foundation.entitydistance.IEntityTypeAccess;
import me.srrapero720.embeddiumplus.EmbyConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(EntityType.class)
public abstract class EntityCullingMixin implements IEntityTypeAccess {
    @Shadow public abstract ResourceLocation getDefaultLootTable();

    @Unique private boolean embPlus$checked = false;
    @Unique private boolean embPlus$whitelisted = false;

    @Override
    @Unique
    public boolean embPlus$isWhitelisted() {
        EmbyConfig.load();
        if (embPlus$checked) return embPlus$whitelisted;

        List<?> entityWhitelist = EmbyConfig.entityWhitelist.get();
        for (int i = 0; i < entityWhitelist.size(); i++) {

            String[] result = ((String) entityWhitelist.get(i)).split(":");

            if (result[1].equals("*") && getDefaultLootTable().getNamespace().equals(result[0])) {
                embPlus$whitelisted = true;
                break;
            } else if (getDefaultLootTable().equals(new ResourceLocation(result[0], result[1]))) {
                embPlus$whitelisted = true;
                break;
            }
            embPlus$checked = true;
        }
        return embPlus$whitelisted;
    }
}
