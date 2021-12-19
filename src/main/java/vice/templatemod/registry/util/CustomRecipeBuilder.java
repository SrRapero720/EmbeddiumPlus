package vice.templatemod.registry.util;

import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.util.IItemProvider;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class CustomRecipeBuilder
{
    Consumer<IFinishedRecipe> consumer;
    IItemProvider BlockItem;

    private final HashMap<IItemProvider, Character> definitionLookup = new HashMap<>();
    private final List<List<IItemProvider>> Rows = new ArrayList<>();

    private IItemProvider UnlockedBy;

    public CustomRecipeBuilder(Consumer<IFinishedRecipe> consumer, IItemProvider BlockItem)
    {
        this.consumer = consumer;
        this.BlockItem = BlockItem;
    }

    public CustomRecipeBuilder UnlockedBy(IItemProvider unlockedBy)
    {
        UnlockedBy = unlockedBy;
        return this;
    }

    public CustomRecipeBuilder Row(@Nullable IItemProvider item1, @Nullable IItemProvider item2, @Nullable IItemProvider item3)
    {
        ArrayList<IItemProvider> row = new ArrayList<>(3);
        Rows.add(row);

        AddItem(row, item1);
        AddItem(row, item2);
        AddItem(row, item3);

        return this;
    }

    public CustomRecipeBuilder Row(@Nullable IItemProvider item1, @Nullable IItemProvider item2)
    {
        ArrayList<IItemProvider> row = new ArrayList<>(2);
        Rows.add(row);

        AddItem(row, item1);
        AddItem(row, item2);

        return this;
    }

    public CustomRecipeBuilder Row(@Nullable IItemProvider item1)
    {
        ArrayList<IItemProvider> row = new ArrayList<>(1);
        Rows.add(row);

        AddItem(row, item1);

        return this;
    }

    private void AddItem(ArrayList<IItemProvider> row, IItemProvider item)
    {
        row.add(item);

        if (item != null)
        {
            if (!definitionLookup.containsKey(item))
            {
                boolean flag = true;
                while (flag)
                {
                    char letter = RandomLetter();
                    if (!definitionLookup.containsValue(letter))
                    {
                        flag = false;
                        definitionLookup.put(item, letter);
                    }
                }
            }
        }
    }

    public void Save()
    {
        ShapedRecipeBuilder builder = ShapedRecipeBuilder.shaped(BlockItem);

        for (List<IItemProvider> row : Rows)
        {
            String pattern = row
                    .stream()
                    .map(i -> (i == null) ? " " : definitionLookup.get(i).toString())
                    .collect(Collectors.joining());

            builder.pattern(pattern);
        }

        for (HashMap.Entry<IItemProvider, Character> definition : definitionLookup.entrySet())
        {
            builder.define(definition.getValue(), definition.getKey());
        }

        builder.unlockedBy(UnlockedBy.toString(), InventoryChangeTrigger.Instance.hasItems(UnlockedBy));
        builder.save(consumer);
    }

    private final Random r = new Random();
    private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private char RandomLetter()
    {
        return alphabet.charAt(r.nextInt(alphabet.length()));
    }

}
