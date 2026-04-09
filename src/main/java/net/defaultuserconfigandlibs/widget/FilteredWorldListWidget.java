package net.defaultuserconfigandlibs.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.text.Text;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A reusable scrollable world-list widget that filters saves by a caller-provided predicate.
 *
 * <p>Usage:
 * <pre>
 *   FilteredWorldListWidget list = new FilteredWorldListWidget(
 *       client, width, listHeight, listTopY, 20,
 *       client.getLevelStorage().getSavesDirectory(),
 *       path -> SomeMod.isSpecialWorld(path),
 *       entry -> onWorldSelected(entry.name)
 *   );
 *   addDrawableChild(list);
 *   list.preselectByName("MyWorld");
 * </pre>
 *
 * <p>The {@code onSelect} consumer is called with a {@link WorldEntry} (or {@code null} when
 * programmatically deselecting via {@link #forceDeselect()}).
 */
public class FilteredWorldListWidget extends AlwaysSelectedEntryListWidget<FilteredWorldListWidget.WorldEntry> {

    private final Path savesDir;
    private final Predicate<Path> filter;
    private final Consumer<WorldEntry> onSelect;

    /**
     * @param client    MinecraftClient instance
     * @param width     Widget width (usually screen width)
     * @param height    Widget height
     * @param topY      Top y-coordinate of the widget
     * @param itemHeight Height per entry row (typically 18–20)
     * @param savesDir  Path to the saves directory
     * @param filter    Predicate receiving each world directory Path; return true to include it
     * @param onSelect  Called whenever selection changes (entry may be null after forceDeselect)
     */
    public FilteredWorldListWidget(MinecraftClient client, int width, int height, int topY, int itemHeight,
                                   Path savesDir, Predicate<Path> filter, Consumer<WorldEntry> onSelect) {
        super(client, width, height, topY, itemHeight);
        this.savesDir = savesDir;
        this.filter = filter;
        this.onSelect = onSelect;
        refresh();
    }

    /** Rescans the saves directory and repopulates the list. Clears current selection. */
    public void refresh() {
        clearEntries();
        File[] worlds = savesDir.toFile().listFiles(File::isDirectory);
        if (worlds == null) return;
        for (File world : worlds) {
            if (filter.test(world.toPath())) {
                addEntry(new WorldEntry(world.getName()));
            }
        }
    }

    /**
     * Scrolls to and selects the entry whose name matches {@code name}.
     * Does nothing if no entry matches.
     */
    public void preselectByName(String name) {
        if (name == null) return;
        for (WorldEntry entry : children()) {
            if (entry.name.equals(name)) {
                setSelected(entry);
                centerScrollOn(entry);
                return;
            }
        }
    }

    /** Returns the number of entries currently in the list. */
    public int size() {
        return children().size();
    }

    /** Clears the selection without firing the onSelect callback. */
    public void forceDeselect() {
        super.setSelected(null);
    }

    @Override
    public void setSelected(WorldEntry entry) {
        super.setSelected(entry);
        onSelect.accept(entry);
    }

    // -------------------------------------------------------------------------
    // Entry
    // -------------------------------------------------------------------------

    public class WorldEntry extends AlwaysSelectedEntryListWidget.Entry<WorldEntry> {

        public final String name;

        public WorldEntry(String name) {
            this.name = name;
        }

        @Override
        public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight,
                           int mouseX, int mouseY, boolean hovered, float tickDelta) {
            boolean selected = FilteredWorldListWidget.this.getSelectedOrNull() == this;
            int color = selected ? 0xFFFFFF : (hovered ? 0xCCCCCC : 0xAAAAAA);
            ctx.drawTextWithShadow(
                FilteredWorldListWidget.this.client.textRenderer,
                Text.literal(name),
                x + 4,
                y + (entryHeight - 8) / 2,
                color
            );
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            FilteredWorldListWidget.this.setSelected(this);
            return true;
        }

        @Override
        public Text getNarration() {
            return Text.literal(name);
        }
    }
}
