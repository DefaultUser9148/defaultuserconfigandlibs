package net.defaultuserconfigandlibs.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * A square button that renders a texture icon instead of text.
 * Size defaults to 20x20 to match vanilla button rows.
 */
public class IconButtonWidget extends ButtonWidget {

    protected final Identifier texture;
    protected final int iconSize;

    public IconButtonWidget(int x, int y, int size, Identifier texture, Text tooltip, PressAction onPress) {
        super(x, y, size, size, Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
        this.texture = texture;
        this.iconSize = size - 4; // 2px padding each side
        if (!tooltip.getString().isEmpty()) {
            setTooltip(net.minecraft.client.gui.tooltip.Tooltip.of(tooltip));
        }
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        // Draw the standard button background
        super.renderWidget(context, mouseX, mouseY, delta);
        // Draw the icon centered inside using drawGuiTexture (1.21.4 API)
        int iconX = getX() + (width  - iconSize) / 2;
        int iconY = getY() + (height - iconSize) / 2;
        context.drawTexture(net.minecraft.client.render.RenderLayer::getGuiTextured, texture, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
    }
}
