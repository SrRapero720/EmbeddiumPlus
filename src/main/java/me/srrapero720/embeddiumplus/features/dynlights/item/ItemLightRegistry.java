package me.srrapero720.embeddiumplus.features.dynlights.item;

import com.google.gson.JsonParser;
import me.srrapero720.embeddiumplus.features.dynlights.DynLightsPlus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static me.srrapero720.embeddiumplus.EmbeddiumPlus.LOGGER;

/**
 * Represents an item light sources manager.
 *
 * @author LambdAurora
 * @version 2.0.2
 * @since 1.3.0
 */
public final class ItemLightRegistry {
	private static final Marker IT = MarkerManager.getMarker("ItemLightRegistry");
    private static final List<ItemLightSource> ITEM_LIGHT_SOURCES = new ArrayList<>();
	private static final List<ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new ArrayList<>();

	/**
	 * Loads the item light source data from resource pack.
	 *
	 * @param resourceManager The resource manager.
	 */
	public static void load(@NotNull ResourceManager resourceManager) {
		ITEM_LIGHT_SOURCES.clear();

		resourceManager.listResources("dynamiclights/item", path -> path.getPath().endsWith(".json")).forEach((id, resource) -> {
			load(resourceManager, id, resource);
		});

		ITEM_LIGHT_SOURCES.addAll(STATIC_ITEM_LIGHT_SOURCES);
	}

	private static void load(@NotNull ResourceManager resourceManager, @NotNull ResourceLocation resourceId, Resource resource) {
		var id = new ResourceLocation(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));

		try {
			var json = JsonParser.parseReader(new InputStreamReader(resource.open())).getAsJsonObject();

			ItemLightSource.fromJson(id, json).ifPresent(data -> {
				if (!STATIC_ITEM_LIGHT_SOURCES.contains(data))
					register(data);
			});
		} catch (IOException | IllegalStateException e) {
			LOGGER.warn(IT, "Failed to load item light source '{}'", id);
		}
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data The item light source data.
	 */
	private static void register(@NotNull ItemLightSource data) {
		for (var other : ITEM_LIGHT_SOURCES) {
			if (other.item() == data.item()) {
				LOGGER.warn(IT, "Failed to register item light source '{}', duplicates item '{}' found in '{}'",
						data.id(), ForgeRegistries.ITEMS.getKey(data.item()), other.id());
				return;
			}
		}

		ITEM_LIGHT_SOURCES.add(data);
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data the item light source data
	 */
	public static void registerItemLightSource(@NotNull ItemLightSource data) {
		for (var other : STATIC_ITEM_LIGHT_SOURCES) {
			if (other.item() == data.item()) {
				LOGGER.warn(IT, "Failed to register item light source '{}', duplicates item '{}' found in '{}'",
						data.id(), ForgeRegistries.ITEMS.getKey(data.item()), other.id());
				return;
			}
		}

		STATIC_ITEM_LIGHT_SOURCES.add(data);
	}

	/**
	 * Returns the luminance of the item in the stack.
	 *
	 * @param stack the item stack
	 * @param submergedInWater {@code true} if the stack is submerged in water, else {@code false}
	 * @return a luminance value
	 */
	public static int getLuminance(@NotNull ItemStack stack, boolean submergedInWater) {
		for (var data : ITEM_LIGHT_SOURCES) {
			if (data.item() == stack.getItem()) {
				return data.getLuminance(stack, submergedInWater);
			}
		}
		if (stack.getItem() instanceof BlockItem blockItem)
			return ItemLightSource.BlockItemLightSource.getLuminance(stack, blockItem.getBlock().defaultBlockState());
		else return 0;
	}
}
