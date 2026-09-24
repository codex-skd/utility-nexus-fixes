package com.skd.mysticalrealmsmodpackfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.skd.mysticalrealmsmodpackfixes.compat.bettervillageranimations.BvaDialogueTranslations;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Translates Better Villager Animations villager-to-villager conversation lines.
 *
 * <p>Soft (string) target: the affected mod is an optional dependency and is not on the compile
 * classpath. {@code @Pseudo} tells the Mixin annotation processor not to require the target at
 * compile time (it cannot be resolved here by design). Only applied when it is present
 * (see {@code BvaMixinPlugin}) and only on the client (listed in the {@code client} array of the
 * mixin config).
 *
 * <p>Intercepts every {@code String.replace(CharSequence, CharSequence)} call inside the catalog
 * {@code select} method. The conversation catalog chains up to 8 such calls to substitute
 * placeholder tokens ({@code {speaker_goods}}, {@code {time}}, etc.). On the <em>first</em> call
 * the receiver is still the pristine English template, so
 * {@link BvaDialogueTranslations#translate} substitutes the Spanish version (which preserves
 * literal placeholder tokens verbatim). On each subsequent call in the chain the receiver is a
 * partially-substituted string that has no table entry, so {@code translate} is a safe no-op and
 * normal replacement proceeds as before.
 */
@Pseudo
@Mixin(targets = "dev.bettervillageranimations.animation.villager.VillagerConversationCatalog", remap = false)
public abstract class BvaConversationMixin {
    @WrapOperation(
        method = "select(Ldev/bettervillageranimations/animation/villager/VillagerConversationCatalog$Topic;ILjava/lang/String;Ljava/lang/String;JLdev/bettervillageranimations/animation/villager/VillagerConversationCatalog$DayPhase;Ldev/bettervillageranimations/animation/villager/VillagerConversationCatalog$Weather;)Ljava/lang/String;",
        at = @At(
            value = "INVOKE",
            target = "Ljava/lang/String;replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"
        ),
        remap = false
    )
    private static String mysticalRealmsModpackFixes$translateBeforeReplace(
        String instance,
        CharSequence target,
        CharSequence replacement,
        Operation<String> original
    ) {
        String translated = BvaDialogueTranslations.translate(instance);
        return original.call(translated, target, replacement);
    }
}
