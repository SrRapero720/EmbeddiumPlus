package vice.templatemod.registry.util;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.util.IItemProvider;

import java.util.function.Consumer;

public class RecipeHelper
{
    public static BlockRecipe Block(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        return new BlockRecipe(consumer, BlockItem);
    }

    public static CustomRecipeBuilder Shaped(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        return new CustomRecipeBuilder(consumer, BlockItem);
    }
}


