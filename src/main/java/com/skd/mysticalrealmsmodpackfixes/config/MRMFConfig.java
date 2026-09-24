package com.skd.mysticalrealmsmodpackfixes.config;

import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;
import java.util.List;

/**
 * First config for this mod. Holds the settings for {@link com.skd.mysticalrealmsmodpackfixes.log.BenignLogFilter}.
 *
 * <p>File: {@code config/mystical_realms_modpack_fixes/config.toml}. Registered from the
 * {@code @Mod} constructor via
 * {@code modContainer.registerConfig(ModConfig.Type.COMMON, SPEC, "mystical_realms_modpack_fixes/config.toml")}.
 */
public final class MRMFConfig {

    /** Path passed to {@code ModContainer#registerConfig}. NeoForge creates the nested dirs. */
    public static final String CONFIG_PATH = "mystical_realms_modpack_fixes/config.toml";

    private static final List<String> DEFAULT_PATTERNS = List.of(
            "Tried to load invalid item: 'Item must not be minecraft:air'",
            "Tried to load invalid item: 'Unknown registry key",
            "Tried to load invalid fluid:",
            "Ignoring unknown attribute 'forge:"
    );

    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue ENABLED;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> PATTERNS;
    private static final ModConfigSpec.BooleanValue ENABLE_NETHER_RETURN_PORTAL_FIX;
    private static final ModConfigSpec.BooleanValue ENABLE_DANGEROUS_BOSS_HEALTH_FIX;

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

        builder.comment("Vanilla behaviour fixes.").push("fixes");

        ENABLE_NETHER_RETURN_PORTAL_FIX = builder
                .comment("Prevents Nether portals from sending players to the wrong destination portal on return trips",
                        "by remembering, per entity, the portal they originally used.")
                .define("enableNetherReturnPortalFix", true);

        ENABLE_DANGEROUS_BOSS_HEALTH_FIX = builder
                .comment("Prevents \"Dangerous NeoForge\" day-based health scaling from inflating Apotheosis-boosted",
                        "\"boss\" mobs exponentially, by caching their true base health instead of re-scaling the",
                        "already-scaled value on every rescale.")
                .define("enableDangerousBossHealthFix", true);

        builder.pop();

        SPEC = builder.build();
    }

    private MRMFConfig() {
    }

    /**
     * The {@link com.skd.mysticalrealmsmodpackfixes.log.BenignLogFilter} is installed from the {@code @Mod}
     * constructor, which runs during parallel mod construction &mdash; before {@code ModLoader} loads
     * the COMMON config. Any log line emitted in that window would otherwise reach a raw
     * {@link ModConfigSpec.ConfigValue#get()} and blow up with
     * {@code IllegalStateException: Cannot get config value before config is loaded}, which then
     * escapes as an {@code ExceptionInInitializerError} in whatever mod happened to log first.
     * Fall back to the built-in defaults until the spec is loaded.
     */
    public static boolean enabled() {
        return SPEC.isLoaded() ? ENABLED.get() : true;
    }

    /**
     * Whether the Nether return-portal fix ({@code MixinNetherReturnPortalFix}) should apply. Like
     * {@link #enabled()}, this is read from a mixin that can run before the COMMON config is loaded,
     * so it falls back to the built-in default ({@code true}) until {@link ModConfigSpec#isLoaded()}.
     */
    public static boolean netherReturnPortalFixEnabled() {
        return SPEC.isLoaded() ? ENABLE_NETHER_RETURN_PORTAL_FIX.get() : true;
    }

    /**
     * Whether the Dangerous NeoForge boss health fix ({@code DangerousBossHealthFixMixin}) should
     * apply. Like {@link #netherReturnPortalFixEnabled()}, this is read from a mixin that can run
     * before the COMMON config is loaded, so it falls back to the built-in default ({@code true})
     * until {@link ModConfigSpec#isLoaded()}.
     */
    public static boolean dangerousBossHealthFixEnabled() {
        return SPEC.isLoaded() ? ENABLE_DANGEROUS_BOSS_HEALTH_FIX.get() : true;
    }

    @SuppressWarnings("unchecked")
    public static List<String> patterns() {
        if (!SPEC.isLoaded()) {
            return DEFAULT_PATTERNS;
        }
        return (List<String>) (List<?>) PATTERNS.get();
    }

    /** Absolute path of the generated TOML on disk. */
    public static Path getConfigPath() {
        return FMLPaths.CONFIGDIR.get().resolve(CONFIG_PATH);
    }
}
