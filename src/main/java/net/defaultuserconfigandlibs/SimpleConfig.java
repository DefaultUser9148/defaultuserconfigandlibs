package net.defaultuserconfigandlibs;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Simple key-value config backed by a .properties file.
 *
 * <p>Usage:
 * <pre>
 *   SimpleConfig cfg = new SimpleConfig(FabricLoader.getInstance().getConfigDir().resolve("mymod.properties"))
 *       .defineBoolean("myToggle", true)
 *       .defineBoolean("otherToggle", false);
 *   cfg.load();
 *   boolean val = cfg.getBoolean("myToggle");
 *   cfg.setBoolean("myToggle", false);
 *   cfg.save();
 * </pre>
 */
public class SimpleConfig {

    private final Path filePath;
    /** Insertion-ordered to keep consistent display/save order. */
    private final Map<String, Boolean> defaults = new LinkedHashMap<>();
    private final Map<String, Boolean> values   = new LinkedHashMap<>();

    public SimpleConfig(Path filePath) {
        this.filePath = filePath;
    }

    /** Registers a boolean key with a default. Returns {@code this} for chaining. */
    public SimpleConfig defineBoolean(String key, boolean defaultValue) {
        defaults.put(key, defaultValue);
        values.put(key, defaultValue);
        return this;
    }

    public boolean getBoolean(String key) {
        return values.getOrDefault(key, defaults.getOrDefault(key, false));
    }

    public void setBoolean(String key, boolean value) {
        if (defaults.containsKey(key)) values.put(key, value);
    }

    /** Keys in registration order. */
    public Iterable<String> keys() {
        return defaults.keySet();
    }

    /** Loads values from disk. Unknown keys are ignored; missing keys stay at default. */
    public void load() {
        if (!Files.exists(filePath)) return;
        Properties props = new Properties();
        try (Reader r = Files.newBufferedReader(filePath)) {
            props.load(r);
        } catch (IOException e) {
            return;
        }
        for (String key : defaults.keySet()) {
            String raw = props.getProperty(key);
            if (raw != null) values.put(key, Boolean.parseBoolean(raw.trim()));
        }
    }

    /** Writes current values to disk. */
    public void save() {
        try {
            Files.createDirectories(filePath.getParent());
            Properties props = new Properties();
            for (Map.Entry<String, Boolean> e : values.entrySet()) {
                props.setProperty(e.getKey(), String.valueOf(e.getValue()));
            }
            try (Writer w = Files.newBufferedWriter(filePath)) {
                props.store(w, "WorldVersionBackport config");
            }
        } catch (IOException ignored) {}
    }
}
