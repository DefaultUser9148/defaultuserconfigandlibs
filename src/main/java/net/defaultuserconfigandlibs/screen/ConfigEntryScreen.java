package net.defaultuserconfigandlibs.screen;

import net.defaultuserconfigandlibs.SimpleConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * A settings screen that displays boolean config entries as left-aligned toggles —
 * like raw HTML with no CSS: no centering, no frills.
 *
 * <p>Each entry occupies one row: [label at left] [ON/OFF button at fixed column].
 * Entries are stacked top-to-bottom starting from the top-left.
 *
 * <p>Changes are saved immediately when a toggle is clicked.
 */
public class ConfigEntryScreen extends Screen {

    private static final int PAD         = 8;
    private static final int ROW_H       = 20;
    private static final int ROW_GAP     = 4;
    private static final int LABEL_COL   = PAD;
    private static final int TOGGLE_COL  = 240;
    private static final int TOGGLE_W    = 50;
    private static final int TOP_OFFSET  = 30; // below title line

    private final Screen parent;
    private final SimpleConfig config;

    public ConfigEntryScreen(Screen parent, String screenTitle, SimpleConfig config) {
        super(Text.literal(screenTitle));
        this.parent = parent;
        this.config = config;
    }

    @Override
    protected void init() {
        int y = TOP_OFFSET;

        for (String key : config.keys()) {
            final String k = key;
            final int btnY = y;

            ButtonWidget toggle = ButtonWidget.builder(
                Text.literal(config.getBoolean(k) ? "ON" : "OFF"),
                btn -> {
                    boolean next = !config.getBoolean(k);
                    config.setBoolean(k, next);
                    btn.setMessage(Text.literal(next ? "ON" : "OFF"));
                    config.save();
                }
            ).dimensions(TOGGLE_COL, btnY, TOGGLE_W, ROW_H).build();

            addDrawableChild(toggle);
            y += ROW_H + ROW_GAP;
        }

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), btn ->
            client.setScreen(parent)
        ).dimensions(PAD, height - PAD - ROW_H, 100, ROW_H).build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);

        // Screen title
        ctx.drawTextWithShadow(textRenderer, title, PAD, PAD, 0xFFFFFF);

        int y = TOP_OFFSET;
        for (String key : config.keys()) {
            // Vertically center label in the row
            ctx.drawTextWithShadow(textRenderer, Text.literal(key),
                LABEL_COL, y + (ROW_H - 8) / 2, 0xCCCCCC);
            y += ROW_H + ROW_GAP;
        }
    }

    @Override
    public void close() {
        client.setScreen(parent);
    }
}
