package me.srrapero720.embeddiumplus.features.dynlights;

import me.srrapero720.embeddiumplus.EmbeddiumPlus;
import me.srrapero720.embeddiumplus.features.dynlights.accessors.DynamicLightHolder;
import me.srrapero720.embeddiumplus.features.dynlights.events.DynLightsSetupEvent;
import me.srrapero720.embeddiumplus.internal.EmbyConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = EmbeddiumPlus.ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class DynLightsHandlers {
	@SubscribeEvent
	public static void registerDefaultHandlers(DynLightsSetupEvent event) {
		registerDynamicLightHandler(EntityType.BLAZE, Entry.makeHandler(blaze -> 10, blaze -> true));
		registerDynamicLightHandler(EntityType.CREEPER, Entry.makeCreeperEntityHandler(null));
		registerDynamicLightHandler(EntityType.ENDERMAN, entity -> {
			int luminance = 0; // TODO: make it configurable
			if (entity.getCarriedBlock() != null)
				luminance = entity.getCarriedBlock().getLightEmission();
				// luminance = entity.getCarriedBlock().getLightEmission(entity.level(), entity.blockPosition());
			return luminance;
		});
		registerDynamicLightHandler(EntityType.ITEM,
				entity -> DynLightsPlus.getLuminanceFromItemStack(entity.getItem(), entity.isUnderWater()));
		registerDynamicLightHandler(EntityType.ITEM_FRAME, entity -> {
			var world = entity.getCommandSenderWorld();
			return DynLightsPlus.getLuminanceFromItemStack(entity.getItem(), !world.getFluidState(entity.blockPosition()).isEmpty());
		});
		registerDynamicLightHandler(EntityType.GLOW_ITEM_FRAME, entity -> {
			var world = entity.getCommandSenderWorld();
			return Math.max(14, DynLightsPlus.getLuminanceFromItemStack(entity.getItem(),
					!world.getFluidState(entity.blockPosition()).isEmpty()));
		});
		registerDynamicLightHandler(EntityType.MAGMA_CUBE, entity -> (entity.squish > 0.6) ? 11 : 8);
		registerDynamicLightHandler(EntityType.SPECTRAL_ARROW, entity -> 8);
		registerDynamicLightHandler(EntityType.GLOW_SQUID,
				entity -> (int) Mth.clampedLerp(0.f, 12.f, 1.f - entity.getDarkTicksRemaining() / 10.f)
		);
	}

	/**
	 * Registers an entity dynamic light handler.
	 *
	 * @param type the entity type
	 * @param handler the dynamic light handler
	 * @param <T> the type of the entity
	 */
	public static <T extends Entity> void registerDynamicLightHandler(EntityType<T> type, Entry<T> handler) {
		register(DynamicLightHolder.cast(type), handler);
	}

	/**
	 * Registers a block entity dynamic light handler.
	 *
	 * @param type the block entity type
	 * @param handler the dynamic light handler
	 * @param <T> the type of the block entity
	 */
	public static <T extends BlockEntity> void registerDynamicLightHandler(BlockEntityType<T> type, Entry<T> handler) {
		register(DynamicLightHolder.cast(type), handler);
	}

	private static <T> void register(DynamicLightHolder<T> holder, Entry<T> handler) {
		var registeredHandler = holder.lambdynlights$getDynamicLightHandler();
		if (registeredHandler != null) {
			holder.lambdynlights$setDynamicLightHandler(entity ->
					Math.max(registeredHandler.getLuminance(entity), handler.getLuminance(entity)));
		} else {
			holder.lambdynlights$setDynamicLightHandler(handler);
		}
	}

	/**
	 * Returns the registered dynamic light handler of the specified entity.
	 *
	 * @param type the entity type
	 * @param <T> the type of the entity
	 * @return the registered dynamic light handler
	 */
	public static <T extends Entity> @Nullable Entry<T> getDynamicLightHandler(EntityType<T> type) {
		return DynamicLightHolder.cast(type).lambdynlights$getDynamicLightHandler();
	}

	/**
	 * Returns the registered dynamic light handler of the specified block entity.
	 *
	 * @param type the block entity type
	 * @param <T> the type of the block entity
	 * @return the registered dynamic light handler
	 */
	public static <T extends BlockEntity> @Nullable Entry<T> getDynamicLightHandler(BlockEntityType<T> type) {
		return DynamicLightHolder.cast(type).lambdynlights$getDynamicLightHandler();
	}

	/**
	 * Returns whether the given entity can light up.
	 *
	 * @param entity the entity
	 * @param <T> the type of the entity
	 * @return {@code true} if the entity can light up, otherwise {@code false}
	 */
	public static <T extends Entity> boolean canLightUp(T entity) {
		return EmbyConfig.dynLightsOnEntitiesCache;
	}

	/**
	 * Returns whether the given block entity can light up.
	 *
	 * @param entity the entity
	 * @param <T> the type of the block entity
	 * @return {@code true} if the block entity can light up, otherwise {@code false}
	 */
	public static <T extends BlockEntity> boolean canLightUp(T entity) {
		return EmbyConfig.dynLightsOnTileEntitiesCache;
	}

	/**
	 * Returns the luminance from an entity.
	 *
	 * @param entity the entity
	 * @param <T> the type of the entity
	 * @return the luminance
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> int getLuminanceFrom(T entity) {
		if (!EmbyConfig.dynLightsOnEntitiesCache)
			return 0;

		var handler = (Entry<T>) getDynamicLightHandler(entity.getType());
		if (handler == null)
			return 0;
		if (!canLightUp(entity))
			return 0;
		if (handler.isWaterSensitive(entity)
				&& !entity.getCommandSenderWorld().getFluidState(new BlockPos(entity.getBlockX(), entity.getBlockY() + (int) entity.getEyeHeight(), entity.getBlockZ())).isEmpty())
			return 0;
		return handler.getLuminance(entity);
	}

	/**
	 * Returns the luminance from a block entity.
	 *
	 * @param entity the block entity
	 * @param <T> the type of the block entity
	 * @return the luminance
	 */
	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> int getLuminanceFrom(T entity) {
		if (!EmbyConfig.dynLightsOnTileEntitiesCache)
			return 0;
		Entry<T> handler = (Entry<T>) getDynamicLightHandler(entity.getType());
		if (handler == null)
			return 0;
		if (!canLightUp(entity))
			return 0;
		if (handler.isWaterSensitive(entity) && entity.getLevel() != null && !entity.getLevel().getFluidState(entity.getBlockPos()).isEmpty())
			return 0;
		return handler.getLuminance(entity);
	}

	/**
	 * Represents a dynamic light handler.
	 *
	 * @param <T> The type of the light source.
	 * @author LambdAurora
	 * @version 1.3.0
	 * @since 1.1.0
	 */
	public interface Entry<T> {
		/**
		 * Returns the luminance of the light source.
		 *
		 * @param lightSource The light source.
		 * @return The luminance.
		 */
		int getLuminance(T lightSource);

		/**
		 * Returns whether the light source is water-sensitive or not.
		 *
		 * @param lightSource The light source.
		 * @return True if the light source is water-sensitive, else false.
		 */
		default boolean isWaterSensitive(T lightSource) {
			return false;
		}

		/**
		 * Returns a dynamic light handler.
		 *
		 * @param luminance The luminance function.
		 * @param waterSensitive The water sensitive function.
		 * @param <T> The type of the entity.
		 * @return The completed handler.
		 */
		static <T extends LivingEntity> @NotNull Entry<T> makeHandler(Function<T, Integer> luminance, Function<T, Boolean> waterSensitive) {
			return new Entry<>() {
				@Override
				public int getLuminance(T lightSource) {
					return luminance.apply(lightSource);
				}

				@Override
				public boolean isWaterSensitive(T lightSource) {
					return waterSensitive.apply(lightSource);
				}
			};
		}

		/**
		 * Returns a living entity dynamic light handler.
		 *
		 * @param handler The handler.
		 * @param <T> The type of the entity.
		 * @return The completed handler.
		 */
		static <T extends LivingEntity> @NotNull Entry<T> makeLivingEntityHandler(@NotNull DynLightsHandlers.Entry<T> handler) {
			return entity -> {
				int luminance = 0;
				for (var equipped : entity.getAllSlots()) {
					luminance = Math.max(luminance, DynLightsPlus.getLuminanceFromItemStack(equipped, entity.isUnderWater()));
				}
				return Math.max(luminance, handler.getLuminance(entity));
			};
		}

		/**
		 * Returns a Creeper dynamic light handler.
		 *
		 * @param handler Extra handler.
		 * @param <T> The type of Creeper entity.
		 * @return The completed handler.
		 */
		// TODO: fix this, make getLuminance return dynLight config instead of manually get it
		static <T extends Creeper> @NotNull Entry<T> makeCreeperEntityHandler(@Nullable DynLightsHandlers.Entry<T> handler) {
			return new Entry<>() {
				@Override
				public int getLuminance(T entity) {
					int luminance = 0;

					if (entity.getSwelling(0.f) > 0.001) {
						luminance = switch (EmbyConfig.dynLightSpeed.get()) {
							case OFF -> 0;
							case NORMAL, SLOW, FAST, SUPERFAST, FASTESTS -> 12;
							case REALTIME -> (int) (entity.getSwelling(0.f) * 12.0);
						};
					}

					if (handler != null)
						luminance = Math.max(luminance, handler.getLuminance(entity));

					return luminance;
				}

				@Override
				public boolean isWaterSensitive(T lightSource) {
					return true;
				}
			};
		}
	}
}
