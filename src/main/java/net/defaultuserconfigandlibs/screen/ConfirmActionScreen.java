package net.defaultuserconfigandlibs.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.Consumer;

/**
 * A reusable confirmation dialog screen with a title, multiple body lines,
 * and Yes/No buttons.
 *
 * Usage:
 *   client.setScreen(new ConfirmActionScreen(
 *       parent,
 *       Text.literal("Warning"),
 *       List.of("Line one", "Line two"),
 *       confirmed -> { if (confirmed) doThing(); }
 *   ));
 */
public class ConfirmActionScreen extends Screen {

    private final Screen parent;
    private final List<String> bodyLines;
    private final Consumer<Boolean> callback;

    public ConfirmActionScreen(Screen parent, Text title, List<String> bodyLines, Consumer<Boolean> callback) {
        super(title);
        this.parent = parent;
        this.bodyLines = bodyLines;
        this.callback = callback;
    }

    @Override
    protected void init() {
        int centerX = width / 2;
        int buttonY = height / 2 + (bodyLines.size() * 10) + 20;

        addDrawableChild(ButtonWidget.builder(Text.literal("Yes"), btn -> {
            callback.accept(true);
            client.setScreen(parent);
        }).dimensions(centerX - 102, buttonY, 100, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("No"), btn -> {
            callback.accept(false);
            client.setScreen(parent);
        }).dimensions(centerX + 2, buttonY, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        // Title
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - (bodyLines.size() * 10) - 20, 0xFF5555);

        // Body lines
        int y = height / 2 - (bodyLines.size() * 10);
        for (String line : bodyLines) {
            context.drawCenteredTextWithShadow(textRenderer, Text.literal(line), width / 2, y, 0xFFFFFF);
            y += 12;
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
