package com.skd.mysticalrealmsmodpackfixes.compat.bettervillageranimations;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.slf4j.Logger;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Client-side translation overlay for Better Villager Animations hardcoded villager dialogue.
 *
 * <p>That mod keeps its speech-bubble flavor text as raw Java string literals, so normal
 * {@code lang} files cannot translate it. Its dialogue funnels through two static
 * {@code select(...)} methods (see {@code BvaDialogueMixin} and {@code BvaConversationMixin});
 * this class maps the returned English line to Spanish when a matching entry exists.
 *
 * <p>The translation table itself is NOT shipped here. It is loaded at runtime from
 * {@code assets/bettervillageranimations/dialogue/es_es.json} (namespace
 * {@code bettervillageranimations}, path {@code dialogue/es_es.json}) provided by a separately
 * installed resource pack, and reloaded whenever client resources reload.
 *
 * <p>CLIENT-ONLY: touches {@code Minecraft.getInstance()}. It is only ever referenced from the
 * client-only mixins and the client event subscriber, so it is never loaded on a dedicated
 * server. Static initializers below are intentionally trivial (no client access at class-load).
 */
public final class BvaDialogueTranslations {
    private static final Logger LOGGER = LogUtils.getLogger();

    /** Mod id of the affected mod. Also the resource namespace of the translation file. */
    public static final String AFFECTED_MOD_ID = "bettervillageranimations";

    /** Client language code this overlay applies to. Exact match only. */
    public static final String TARGET_LANGUAGE_CODE = "es_es";

    private static final ResourceLocation TRANSLATION_FILE =
        ResourceLocation.fromNamespaceAndPath(AFFECTED_MOD_ID, "dialogue/es_es.json");

    private static final Gson GSON = new Gson();
    private static final Type MAP_TYPE = new TypeToken<Map<String, String>>() {}.getType();

    /** Cached English-to-Spanish table. Null until the first load attempt. */
    private static volatile Map<String, String> TRANSLATIONS = null;

    /** Ensures a load/parse failure is logged once instead of once per call. */
    private static volatile boolean LOAD_FAILURE_LOGGED = false;

    /**
     * Reload listener registered on the client mod event bus by {@link BvaClientSetup}.
     * Re-parses the translation file whenever client resource packs reload.
     */
    public static final ResourceManagerReloadListener RELOAD_LISTENER = BvaDialogueTranslations::reload;

    private BvaDialogueTranslations() {
    }

    /**
     * (Re)loads the translation table from the client resource manager.
     * Never throws; a missing file simply yields an empty table.
     */
    public static void reload(ResourceManager resourceManager) {
        if (resourceManager == null) {
            return;
        }
        try {
            Optional<Resource> resource = resourceManager.getResource(TRANSLATION_FILE);
            if (resource.isEmpty()) {
                TRANSLATIONS = Collections.emptyMap();
                return;
            }
            try (Reader reader = new InputStreamReader(resource.get().open(), StandardCharsets.UTF_8)) {
                Map<String, String> parsed = GSON.fromJson(reader, MAP_TYPE);
                TRANSLATIONS = parsed != null && !parsed.isEmpty()
                    ? Collections.unmodifiableMap(parsed)
                    : Collections.emptyMap();
                LOAD_FAILURE_LOGGED = false;
                LOGGER.info("[mystical_realms_modpack_fixes] Loaded {} Better Villager Animations dialogue translations",
                    TRANSLATIONS.size());
            }
        } catch (Exception e) {
            TRANSLATIONS = Collections.emptyMap();
            if (!LOAD_FAILURE_LOGGED) {
                LOAD_FAILURE_LOGGED = true;
                LOGGER.warn("[mystical_realms_modpack_fixes] Failed to load Better Villager Animations translations from {}: {}",
                    TRANSLATION_FILE, e.toString());
            }
        }
    }

    /**
     * Returns the Spanish translation for an English dialogue line when the client language is
     * exactly {@code es_es} and the loaded table contains an exact key match.
     * Returns {@code original} unchanged in every other case. Never throws.
     */
    public static String translate(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        try {
            if (!TARGET_LANGUAGE_CODE.equals(getSelectedLanguageCode())) {
                return original;
            }
            Map<String, String> table = TRANSLATIONS;
            if (table == null) {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft != null) {
                    reload(minecraft.getResourceManager());
                }
                table = TRANSLATIONS;
                if (table == null) {
                    return original;
                }
            }
            String translated = table.get(original);
            return translated != null ? translated : original;
        } catch (Exception e) {
            return original;
        }
    }

    private static String getSelectedLanguageCode() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft == null || minecraft.getLanguageManager() == null) {
            return null;
        }
        return minecraft.getLanguageManager().getSelected();
    }
}
