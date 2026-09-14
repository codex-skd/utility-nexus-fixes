package com.skd.utilitynexusfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skd.utilitynexusfixes.compat.bettervillageranimations.BvaDialogueTranslations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Translates Better Villager Animations single-villager dialogue lines.
 *
 * <p>Soft (string) target: the affected mod is an optional dependency and is not on the compile
 * classpath. {@code @Pseudo} tells the Mixin annotation processor not to require the target at
 * compile time (it cannot be resolved here by design). Only applied when it is present
 * (see {@code BvaMixinPlugin}) and only on the client (listed in the {@code client} array of the
 * mixin config).
 *
 * <p>Intercepts every {@code String.replace(CharSequence, CharSequence)} call inside the catalog
 * {@code select} method. On the first call in the chain the receiver is still the pristine English
 * template, so {@link BvaDialogueTranslations#translate} substitutes the Spanish version (which
 * preserves literal placeholder tokens like {@code {player}} verbatim). On any subsequent call in
 * the same chain the receiver is a partially-substituted string that has no table entry, so
 * {@code translate} is a safe no-op and normal replacement proceeds as before.
 */
@Pseudo
@Mixin(targets = "dev.bettervillageranimations.animation.villager.VillagerDialogueCatalog", remap = false)
public abstract class BvaDialogueMixin {
    @WrapOperation(
        method = "select(Ldev/bettervillageranimations/animation/villager/VillagerDialogueCatalog$Cue;Ljava/lang/String;JLjava/lang/String;)Ljava/lang/String;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"
        ),
        remap = false
    )
    private static String utilityNexusFixes$translateBeforeReplace(
        String instance,
        CharSequence target,
        CharSequence replacement,
        Operation<String> original
    ) {
        String translated = BvaDialogueTranslations.translate(instance);
        return original.call(translated, target, replacement);
    }
}
