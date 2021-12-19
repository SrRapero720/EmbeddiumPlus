package vice.templatemod.registry.util;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import vice.templatemod.TemplateMod;

import javax.annotation.Nonnull;

public class ModItemTab {

    public static class ModCreativeTab extends ItemGroup
    {

        public ModCreativeTab(String name) {
            super(name);
        }

        @Override
        public boolean hasSearchBar() {
            return false;
        }

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.DIRT);
        }
    }

    @Nonnull
    public static final ItemGroup tab = new ModCreativeTab(TemplateMod.MODID);

}