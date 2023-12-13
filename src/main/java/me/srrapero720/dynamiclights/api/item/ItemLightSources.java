/*
 * Copyright © 2020 LambdAurora <aurora42lambda@gmail.com>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package me.srrapero720.dynamiclights.api.item;

import com.google.gson.JsonParser;
import me.srrapero720.dynamiclights.LambDynLights;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

/**
 * Represents an item light sources manager.
 *
 * @author LambdAurora
 * @version 2.0.2
 * @since 1.3.0
 */
public final class ItemLightSources {
	private static final JsonParser JSON_PARSER = new JsonParser();
	private static final List<ItemLightSource> ITEM_LIGHT_SOURCES = new ArrayList<>();
	private static final List<ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new ArrayList<>();

	private ItemLightSources() {
		throw new UnsupportedOperationException("ItemLightSources only contains static definitions.");
	}

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
			var json = JSON_PARSER.parse(new InputStreamReader(resource.open())).getAsJsonObject();

			ItemLightSource.fromJson(id, json).ifPresent(data -> {
				if (!STATIC_ITEM_LIGHT_SOURCES.contains(data))
					register(data);
			});
		} catch (IOException | IllegalStateException e) {
			LambDynLights.get().warn("Failed to load item light source \"" + id + "\".");
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
				LambDynLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
						+ ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + other.id() + "\".");
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
				LambDynLights.get().warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
						+ ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + other.id() + "\".");
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
