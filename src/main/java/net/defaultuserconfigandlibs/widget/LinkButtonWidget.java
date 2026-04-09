package net.defaultuserconfigandlibs.widget;

import net.defaultuserconfigandlibs.DefaultUserConfigAndLibs;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.net.URI;

/**
 * An IconButtonWidget that opens a URL when clicked.
 */
public class LinkButtonWidget extends IconButtonWidget {

    public LinkButtonWidget(int x, int y, int size, Identifier texture, Text tooltip, String url) {
        super(x, y, size, texture, tooltip, btn -> {
            try {
                Util.getOperatingSystem().open(new URI(url));
            } catch (Exception e) {
                DefaultUserConfigAndLibs.LOGGER.error("Failed to open link: {}", url, e);
            }
        });
    }
}
