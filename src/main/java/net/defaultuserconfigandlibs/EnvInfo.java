package net.defaultuserconfigandlibs;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.SharedConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Quick access to environment info for any mod using this library.
 *
 * Usage:
 *   EnvInfo.getMcVersion()       → "1.21.4"
 *   EnvInfo.getFabricVersion()   → "0.16.9"
 *   EnvInfo.getModVersion("mymod") → "1.0.0" or "?" if not found
 *   EnvInfo.getOS()              → "Windows 10 10.0"
 *   EnvInfo.getDateTime()        → "14:23:01 09/04/2026"
 *   EnvInfo.getDateTime("yyyy/MM/dd HH:mm") → custom format
 */
public final class EnvInfo {

    private static final DateTimeFormatter DEFAULT_FMT = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");

    private EnvInfo() {}

    public static String getMcVersion() {
        return SharedConstants.getGameVersion().getName();
    }

    public static String getFabricVersion() {
        return FabricLoader.getInstance()
            .getModContainer("fabricloader")
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("?");
    }

    /** Returns the version string of any loaded mod by its mod ID, or "?" if not found. */
    public static String getModVersion(String modId) {
        return FabricLoader.getInstance()
            .getModContainer(modId)
            .map(c -> c.getMetadata().getVersion().getFriendlyString())
            .orElse("?");
    }

    /** Returns true if the mod is currently loaded. */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /** OS name + version, e.g. "Windows 10 10.0" */
    public static String getOS() {
        return System.getProperty("os.name", "Unknown") + " " + System.getProperty("os.version", "").trim();
    }

    /** Current date/time formatted as HH:mm:ss dd/MM/yyyy */
    public static String getDateTime() {
        return LocalDateTime.now().format(DEFAULT_FMT);
    }

    /** Current date/time with a custom pattern. */
    public static String getDateTime(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}
