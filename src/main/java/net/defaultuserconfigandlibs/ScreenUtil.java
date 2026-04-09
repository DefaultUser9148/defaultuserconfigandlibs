package net.defaultuserconfigandlibs;

import net.minecraft.client.gui.DrawContext;

/**
 * Static rendering utilities for screens.
 */
public class ScreenUtil {

    private ScreenUtil() {}

    /**
     * Draws a filled progress bar centered on cx.
     *
     * @param ctx       DrawContext
     * @param cx        Center x
     * @param y         Top y
     * @param barW      Total bar width in pixels
     * @param barH      Bar height in pixels
     * @param progress  Fill fraction, clamped to [0, 1]
     * @param fillColor ARGB fill color (e.g. 0xFF55FF55)
     */
    public static void drawBar(DrawContext ctx, int cx, int y, int barW, int barH, float progress, int fillColor) {
        int barX = cx - barW / 2;
        ctx.fill(barX, y, barX + barW, y + barH, 0xFF000000);
        int filled = (int)(barW * Math.min(1f, Math.max(0f, progress)));
        if (filled > 0) ctx.fill(barX, y, barX + filled, y + barH, fillColor);
    }
}
