package vice.templatemod.registry.util;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import lombok.val;
import lombok.var;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.function.Function;

public class RegistrateExtensions
{
    public static <T extends BlockItem, P> ItemBuilder<T, P> shapedRecipe(
            ItemBuilder<T, P> builder,
            Function<CustomRecipeBuilder, CustomRecipeBuilder> recipe)
    {
        builder.recipe((gen, prov) -> {
            val helper = RecipeHelper.Shaped(prov, gen.get());
            recipe.apply(helper).Save();
        });

        return builder;
    }

    public static <T extends Block, P> BlockBuilder<T, P> itemWithRecipe(
            BlockBuilder<T, P> builder,
            Function<CustomRecipeBuilder, CustomRecipeBuilder> recipe)
    {
        var item = builder.item();

        item.recipe((gen, prov) -> {
            val helper = RecipeHelper.Shaped(prov, gen.get());
            recipe.apply(helper).Save();
        });

        return item.build();
    }


    public static String newBlock(Registrate registrate, String name) {
        return "shadpapdpdp";
    }
}
