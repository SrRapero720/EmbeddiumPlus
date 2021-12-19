package vice.templatemod;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.NonNullLazyValue;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vice.templatemod.registry.autoreg.AutoRegistry;
import vice.templatemod.registry.simple.RegisteredBlocks;
import vice.templatemod.registry.simple.RegisteredItems;
import vice.templatemod.registry.util.ModItemTab;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("templatemod")
public class TemplateMod {
    public static final String MODID = "templatemod";
    public static final Logger LOGGER = LogManager.getLogger();

    //region Registrate
    private static final NonNullLazyValue<Registrate> REGISTRATE = new NonNullLazyValue<Registrate>(() -> {
        Registrate ret = Registrate.create(TemplateMod.MODID).itemGroup(() -> ModItemTab.tab);
        ret.addDataGenerator(ProviderType.LANG, prov -> {
            prov.add(ModItemTab.tab, "Template Mod");
        });
        return ret;
    });

    public static Registrate registrate() {
        return REGISTRATE.get();
    }

    public static Registrate newObject(String name) {
        return REGISTRATE.get().object(name);
    }

    public static <T extends Block> BlockBuilder<T, Registrate> newBlock(String name, String lang, NonNullFunction<Block.Properties, T> factory) {
        return REGISTRATE.get().object(name).block(factory).lang(lang);
    }

    //endregion



    public TemplateMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);

        AutoRegistry.initClasses("vice.templatemod.features");

        RegisteredBlocks.init();
        RegisteredItems.init();
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }
}