package com.skd.utilitynexusfixes.mixin;

/**
 * DISABLED placeholder — intentionally NOT a mixin (no {@code @Mixin} annotation) and NOT listed in
 * {@code utility_nexus_fixes.mixins.json}.
 *
 * <p>// NOTE: The goal was a data-preserving remap of the legacy Forge attribute ids
 * {@code forge:entity_gravity} -> {@code neoforge:entity_gravity} and
 * {@code forge:step_height_addition} -> {@code neoforge:step_height} inside
 * {@code net.minecraft.world.entity.ai.attributes.AttributeMap#load(ListTag)}.
 * The Mixin annotation processor for NeoForge 21.1.249 fails to resolve an obfuscation mapping for
 * that method (compile error: "Unable to locate obfuscation mapping for @Redirect target load"),
 * so the remap could not be implemented via mixin.
 *
 * <p>Fallback in effect: the resulting {@code "Ignoring unknown attribute 'forge:..."} WARN spam is
 * suppressed by {@link com.skd.utilitynexusfixes.log.BenignLogFilter} through the default
 * {@code logfilter.patterns} entry {@code "Ignoring unknown attribute 'forge:"}. The stale modifier
 * values are dropped (they originate from removed mods), which is acceptable for this data.
 *
 * <p>Kept as a class (not deleted) to document the intent; re-implement here if a working
 * injection point / mapping is found.
 */
public final class AttributeMapLegacyRemapMixin {
    private AttributeMapLegacyRemapMixin() {
    }
}
