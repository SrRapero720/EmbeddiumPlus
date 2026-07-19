package me.srrapero720.chloride.impl.sodium.pages;

import com.google.common.collect.ImmutableList;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.options.control.TickBoxControl;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import me.srrapero720.chloride.Chloride;
import me.srrapero720.chloride.ChlorideConfig;
import me.srrapero720.chloride.impl.sodium.ParticleListScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static me.srrapero720.chloride.impl.sodium.SodiumFeatures.STORAGE;

public class ParticlesPage extends OptionPage {
    public static final OptionIdentifier<Void> ID = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "particles")));
    public static final OptionIdentifier<Void> PARTICLE_BASE_PAGE = OptionIdentifier.create(Objects.requireNonNull(ResourceLocation.tryBuild(Chloride.ID, "particles_page_base")));
    public ParticlesPage() {
        super(ID, Component.translatable("chloride.particles"), create());
    }

    private static ImmutableList<OptionGroup> create() {
        final List<OptionGroup> groups = new ArrayList<>();

        // EMPTY ANCHOR GROUP: SodiumFeatures RELOCATES THE VANILLA "PARTICLES" OPTION INTO IT
        final var particles = OptionGroup.createBuilder();
        particles.setId(PARTICLE_BASE_PAGE);

        final var base = OptionGroup.createBuilder();
        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.rain.title"))
                .setTooltip(Component.translatable("chloride.particles.rain.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.particles.rain = value, opts -> ChlorideConfig.particles.rain)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.rain.drop.title"))
                .setTooltip(Component.translatable("chloride.particles.rain.drop.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.particles.rainDrops = value, opts -> ChlorideConfig.particles.rainDrops)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.block.cracking.title"))
                .setTooltip(Component.translatable("chloride.particles.block.cracking.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.particles.blockCracking = value, opts -> ChlorideConfig.particles.blockCracking)
                .build()
        );

        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setName(Component.translatable("chloride.particles.block.destroy.title"))
                .setTooltip(Component.translatable("chloride.particles.block.destroy.desc"))
                .setControl(TickBoxControl::new)
                .setBinding((opts, value) -> ChlorideConfig.particles.blockDestroyed = value, opts -> ChlorideConfig.particles.blockDestroyed)
                .build()
        );

        // PER-PARTICLE-TYPE TOGGLES LIVE IN ParticleListScreen: ONE SODIUM OPTION PER REGISTERED PARTICLE
        // FROZE THE SETTINGS SCREEN ON BIG MODPACKS, SO THE ROW BELOW OPENS A DEDICATED, LAZY LIST INSTEAD
        base.add(OptionImpl.createBuilder(boolean.class, STORAGE)
                .setId(ResourceLocation.tryBuild(Chloride.ID, "particle_list"))
                .setName(Component.translatable("chloride.particles.list"))
                .setTooltip(Component.translatable("chloride.particles.list.desc"))
                .setControl(opt -> new OpenScreenControl(opt, Component.literal(">")))
                .setBinding((opts, value) -> {}, opts -> false)
                .build()
        );

        groups.add(particles.build());
        groups.add(base.build());

        return ImmutableList.copyOf(groups);
    }

    // OPENS ParticleListScreen WHEN THE ROW IS CLICKED; THE OLD EMBEDDIUM API HAS NO NATIVE SCREEN-LINK CONTROL
    private static final class OpenScreenControl implements Control<Boolean> {
        private final Option<Boolean> option;
        private final Component action;

        private OpenScreenControl(final Option<Boolean> option, final Component action) {
            this.option = option;
            this.action = action;
        }

        @Override
        public Option<Boolean> getOption() {
            return this.option;
        }

        @Override
        public int getMaxWidth() {
            return 70;
        }

        @Override
        public ControlElement<Boolean> createElement(final Dim2i dim) {
            return new ControlElement<>(this.option, dim) {
                @Override
                public void render(final GuiGraphics graphics, final int mouseX, final int mouseY, final float delta) {
                    super.render(graphics, mouseX, mouseY, delta);
                    final int textWidth = this.font.width(OpenScreenControl.this.action);
                    this.drawString(graphics, OpenScreenControl.this.action, this.dim.getLimitX() - textWidth - 6, this.dim.getCenterY() - 4, 0xFFFFFFFF);
                }

                @Override
                public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
                    if (button == 0 && this.option.isAvailable() && this.dim.containsCursor(mouseX, mouseY)) {
                        this.playClickSound();
                        final Minecraft mc = Minecraft.getInstance();
                        mc.setScreen(new ParticleListScreen(mc.screen));
                        return true;
                    }
                    return false;
                }
            };
        }
    }
}
