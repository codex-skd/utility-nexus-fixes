package com.skd.utilitynexusfixes.mixin;

import net.neoforged.fml.ModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Mixin config plugin gating this mod's soft (string-target) mixins on their affected mod
 * actually being loaded &mdash; Mixin only allows one plugin per config file, so every soft
 * mixin in {@code utility_nexus_fixes.mixins.json} is gated from here, not just the original
 * Better Villager Animations pair the class was named after.
 *
 * <p>{@code BvaDialogueMixin} / {@code BvaConversationMixin} target classes from the optional
 * {@code bettervillageranimations} mod, and {@code BetterPartyMemberHealthGuardMixin} targets a
 * class from the optional {@code better_party} mod. Each only applies when its affected mod is
 * actually loaded; every other (non-soft) mixin in the config is unaffected.
 */
public final class BvaMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "com.skd.utilitynexusfixes.mixin.";
    private static final String DIALOGUE_MIXIN = MIXIN_PACKAGE + "BvaDialogueMixin";
    private static final String CONVERSATION_MIXIN = MIXIN_PACKAGE + "BvaConversationMixin";
    private static final String BETTER_PARTY_HEALTH_GUARD_MIXIN = MIXIN_PACKAGE + "BetterPartyMemberHealthGuardMixin";

    /** Soft mixin class name -> mod id it requires to be loaded. */
    private static final Map<String, String> SOFT_MIXIN_MOD_IDS = Map.of(
            DIALOGUE_MIXIN, "bettervillageranimations",
            CONVERSATION_MIXIN, "bettervillageranimations",
            BETTER_PARTY_HEALTH_GUARD_MIXIN, "better_party"
    );

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        String requiredModId = SOFT_MIXIN_MOD_IDS.get(mixinClassName);
        if (requiredModId == null) {
            return true;
        }
        ModList modList = ModList.get();
        return modList != null && modList.isLoaded(requiredModId);
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
