package vice.templatemod.features.example;

import com.tterrag.registrate.util.entry.BlockEntry;
import lombok.experimental.ExtensionMethod;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import vice.templatemod.TemplateMod;
import vice.templatemod.registry.util.RegistrateExtensions;

@ExtensionMethod({RegistrateExtensions.class})
public class ExampleBlock extends Block
{
    public ExampleBlock(Properties p_i48440_1_)
    {
        super(p_i48440_1_);
    }

    public static final BlockEntry<ExampleBlock> Registration =
            TemplateMod.newBlock("exampleblock", "Example Block", ExampleBlock::new)
            .itemWithRecipe(recipe -> recipe
                    .Row(Items.DIRT, Items.DIRT, Items.DIRT)
                    .Row(Items.DIRT, Items.COBBLESTONE, Items.DIRT)
                    .Row(Items.DIRT, Items.DIRT, Items.DIRT)
                    .UnlockedBy(Items.DIRT))
            .register();
}
