package net.defaultuserconfigandlibs.widget;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Utility for creating and placing icon buttons on screens.
 *
 * createIconButton / createLinkButton — slot-based fixed placement.
 * findSmartX — scans existing widgets on a row and returns the next open X position.
 */
public class ModMenuButtonHelper {

    public static final int BUTTON_SIZE = 20;

    // Vanilla title screen row Y: height/4 + 156
    public static final int TITLE_ROW_Y_OFFSET_A = 4;   // divide height by this
    public static final int TITLE_ROW_Y_OFFSET_B = 156;  // then add this

    /**
     * Scans all children for ClickableWidgets on the given row Y (±2px tolerance)
     * and returns the X position just after the rightmost one.
     * Falls back to fallbackX if no widgets found on that row.
     */
    public static int findSmartX(List<? extends Element> children, int rowY, int fallbackX) {
        int maxRight = fallbackX;
        for (Element child : children) {
            if (child instanceof ClickableWidget cw) {
                if (Math.abs(cw.getY() - rowY) <= 2) {
                    int right = cw.getX() + cw.getWidth();
                    if (right > maxRight) {
                        maxRight = right;
                    }
                }
            }
        }
        return maxRight + 4;
    }

    /**
     * Creates a square icon button at a slot-based fixed X position.
     * x = screenWidth/2 + 106 + (slot * (BUTTON_SIZE + 2))
     */
    public static IconButtonWidget createIconButton(int screenWidth, int baseY, int slot,
                                                    Identifier texture, Text tooltip,
                                                    ButtonWidget.PressAction onPress) {
        int x = screenWidth / 2 + 106 + (slot * (BUTTON_SIZE + 2));
        return new IconButtonWidget(x, baseY, BUTTON_SIZE, texture, tooltip, onPress);
    }

    /**
     * Creates a link button at a slot-based fixed X position.
     */
    public static LinkButtonWidget createLinkButton(int screenWidth, int baseY, int slot,
                                                    Identifier texture, Text tooltip, String url) {
        int x = screenWidth / 2 + 106 + (slot * (BUTTON_SIZE + 2));
        return new LinkButtonWidget(x, baseY, BUTTON_SIZE, texture, tooltip, url);
    }
}
