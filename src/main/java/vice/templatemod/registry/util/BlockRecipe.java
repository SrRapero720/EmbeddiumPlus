package vice.templatemod.registry.util;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class BlockRecipe
{
    Consumer<IFinishedRecipe> consumer;
    IItemProvider BlockItem;

    public BlockRecipe(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        this.consumer = consumer;
        this.BlockItem = BlockItem;
    }

    public void MadeFrom(IItemProvider IngotItem)
    {
        // block recipe
        ShapedRecipeBuilder.shaped(BlockItem)
                .pattern("xxx")
                .pattern("xxx")
                .pattern("xxx")
                .define('x', IngotItem)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.Instance.hasItems(IngotItem))
                .save(consumer);

        // ingot recipe
        ShapelessRecipeBuilder.shapeless(IngotItem, 9)
                .requires(BlockItem, 1)
                .unlockedBy(IngotItem.toString(), InventoryChangeTrigger.Instance.hasItems(BlockItem))
                .save(consumer);
    }
}
