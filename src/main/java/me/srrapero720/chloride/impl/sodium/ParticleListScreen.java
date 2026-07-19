package me.srrapero720.chloride.impl.sodium;

import me.srrapero720.chloride.ChlorideConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ParticleListScreen extends Screen {
    private final Screen parent;
    private ParticleList list;
    private EditBox search;
    private boolean dirty;

    public ParticleListScreen(final Screen parent) {
        super(Component.translatable("chloride.particles.list"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        final EditBox previous = this.search;
        this.search = new EditBox(this.font, this.width / 2 - 100, 24, 200, 20, Component.translatable("chloride.particles.list.search"));
        if (previous != null) this.search.setValue(previous.getValue());
        this.search.setResponder(text -> this.list.refresh(text));
        this.addRenderableWidget(this.search);

        this.list = new ParticleList(this.minecraft, this.width, this.height - 52 - 36, 52, 32);
        this.addRenderableWidget(this.list);
        this.list.refresh(this.search.getValue());

        this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, b -> this.onClose())
                .bounds(this.width / 2 - 100, this.height - 28, 200, 20)
                .build());
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        graphics.centeredText(this.font, this.title, this.width / 2, 9, 0xFFFFFFFF);
    }

    @Override
    public void onClose() {
        if (this.dirty) {
            ChlorideConfig.write();
        }
        this.minecraft.setScreen(this.parent);
    }

    private class ParticleList extends ContainerObjectSelectionList<ParticleList.Entry> {
        private final HashMap<String, String> providerNames = new HashMap<>();

        public ParticleList(final Minecraft minecraft, final int width, final int height, final int y, final int itemHeight) {
            super(minecraft, width, height, y, itemHeight);
        }

        public void refresh(final String filter) {
            final String query = filter.trim().toLowerCase(Locale.ROOT);
            final List<Identifier> keys = new ArrayList<>(BuiltInRegistries.PARTICLE_TYPE.keySet());
            keys.sort(Comparator.comparing(Identifier::toString));

            this.clearEntries();
            for (final Identifier key: keys) {
                if (!query.isEmpty() && !key.toString().contains(query) && !this.providerName(key).toLowerCase(Locale.ROOT).contains(query))
                    continue;
                this.addEntry(new Entry(key));
            }
            this.setScrollAmount(0);
        }

        private String providerName(final Identifier key) {
            return this.providerNames.computeIfAbsent(key.getNamespace(), namespace -> FabricLoader.getInstance()
                    .getModContainer(namespace)
                    .map(mod -> mod.getMetadata().getName())
                    .orElseGet(() -> I18n.get("chloride.particles.provider.unknown")));
        }

        @Override
        public int getRowWidth() {
            return 320;
        }

        @Override
        protected int scrollBarX() {
            return this.width / 2 + (this.getRowWidth() / 2) + 8;
        }

        private class Entry extends ContainerObjectSelectionList.Entry<Entry> {
            private final Identifier key;
            private final CycleButton<Boolean> toggle;

            Entry(final Identifier key) {
                this.key = key;
                this.toggle = CycleButton.onOffBuilder(!ChlorideConfig.particles.disabled.contains(key))
                        .displayOnlyValue()
                        .create(0, 0, 44, 20, Component.empty(), (btn, enabled) -> {
                            if (enabled) {
                                ChlorideConfig.particles.disabled.remove(key);
                            } else if (!ChlorideConfig.particles.disabled.contains(key)) {
                                ChlorideConfig.particles.disabled.add(key);
                            }
                            ParticleListScreen.this.dirty = true;
                        });
            }

            @Override
            public void extractContent(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final boolean hovering, final float partialTick) {
                final int left = this.getX();
                final int top = this.getY();
                final int width = this.getWidth();
                graphics.text(ParticleListScreen.this.font, this.key.toString(), left + 2, top + 4, 0xFFFFFFFF);
                graphics.text(ParticleListScreen.this.font, ParticleList.this.providerName(this.key), left + 2, top + 16, 0xFFAAAAAA);

                this.toggle.setX(left + width - 48);
                this.toggle.setY(top + 4);
                this.toggle.extractRenderState(graphics, mouseX, mouseY, partialTick);
            }

            @Override
            public List<? extends GuiEventListener> children() {
                return List.of(this.toggle);
            }

            @Override
            public List<? extends NarratableEntry> narratables() {
                return List.of(this.toggle);
            }
        }
    }
}
