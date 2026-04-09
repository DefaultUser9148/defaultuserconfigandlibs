package net.defaultuserconfigandlibs;

import net.minecraft.world.level.storage.LevelSummary;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Lets any mod override the "Play" button text and tooltip in the
 * Select World screen when a specific world is selected.
 *
 * Usage (in your ClientModInitializer):
 *   WorldSelectButtonRegistry.register((level) -> myCheck(level) ? "Custom Label" : null,
 *                                       (level) -> myCheck(level) ? "Tooltip text" : null);
 *
 * Or use the interface:
 *   WorldSelectButtonRegistry.register(new WorldSelectButtonRegistry.WorldButtonOverride() { ... });
 */
public final class WorldSelectButtonRegistry {

    private static final List<WorldButtonOverride> overrides = new ArrayList<>();

    private WorldSelectButtonRegistry() {}

    public interface WorldButtonOverride {
        /** Return a custom button label, or null to leave the default. */
        @Nullable String getButtonLabel(LevelSummary level);

        /** Return tooltip text shown when hovering the button, or null for none. */
        @Nullable String getTooltipText(LevelSummary level);
    }

    public static void register(WorldButtonOverride override) {
        overrides.add(override);
    }

    @Nullable
    public static String resolveLabel(LevelSummary level) {
        for (WorldButtonOverride o : overrides) {
            String label = o.getButtonLabel(level);
            if (label != null) return label;
        }
        return null;
    }

    @Nullable
    public static String resolveTooltip(LevelSummary level) {
        for (WorldButtonOverride o : overrides) {
            String tip = o.getTooltipText(level);
            if (tip != null) return tip;
        }
        return null;
    }
}
