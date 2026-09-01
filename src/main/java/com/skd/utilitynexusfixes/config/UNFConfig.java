package com.skd.utilitynexusfixes.config;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;
import java.util.List;

/**
 * First config for this mod. Holds the settings for {@link com.skd.utilitynexusfixes.log.BenignLogFilter}.
 *
 * <p>File: {@code config/utility_nexus/fixes/config.toml} — same nested layout that
 * {@code utility_nexus_admin} uses ({@code config/utility_nexus/admin/config.toml}). Registered from
 * the {@code @Mod} constructor via
 * {@code modContainer.registerConfig(ModConfig.Type.COMMON, SPEC, "utility_nexus/fixes/config.toml")}.
 */
public final class UNFConfig {

    /** Path passed to {@code ModContainer#registerConfig}. NeoForge creates the nested dirs. */
    public static final String CONFIG_PATH = "utility_nexus/fixes/config.toml";

    private static final List<String> DEFAULT_PATTERNS = List.of(
            "Tried to load invalid item: 'Item must not be minecraft:air'",
            "Tried to load invalid item: 'Unknown registry key",
            "Tried to load invalid fluid:",
            "Ignoring unknown attribute 'forge:"
    );

    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue ENABLED;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> PATTERNS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("Benign log-noise filter settings.").push("logfilter");

        ENABLED = builder
                .comment("Master switch for the benign-log-noise filter.")
                .define("enabled", true);

        PATTERNS = builder
                .comment("Log messages containing any of these substrings (case-sensitive) are dropped.",
                        "Never applied to this mod's own logger.")
                .defineList("patterns", DEFAULT_PATTERNS, () -> "", o -> o instanceof String);

        builder.pop();

        SPEC = builder.build();
    }

    private UNFConfig() {
    }

    public static boolean enabled() {
        return ENABLED.get();
    }

    @SuppressWarnings("unchecked")
    public static List<String> patterns() {
        return (List<String>) (List<?>) PATTERNS.get();
    }

    /** Absolute path of the generated TOML on disk. Mirrors {@code UNAConfig.getConfigPath()}. */
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_PATH);
    }
}
