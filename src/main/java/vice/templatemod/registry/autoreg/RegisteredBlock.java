package vice.templatemod.registry.autoreg;


import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.block.Block;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisteredBlock {
}

