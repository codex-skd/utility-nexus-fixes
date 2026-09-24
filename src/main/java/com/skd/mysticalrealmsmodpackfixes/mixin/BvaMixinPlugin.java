package com.skd.mysticalrealmsmodpackfixes.mixin;

import com.mojang.logging.LogUtils;
import org.objectweb.asm.tree.ClassNode;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Mixin config plugin for this mod's single mixin config. Mixin allows only one plugin per JSON
 * file, so every mixin in {@code mystical_realms_modpack_fixes.mixins.json} is routed through here.
 *
 * <p><strong>Soft-mixin gating was removed on purpose.</strong> Earlier revisions used this plugin
 * to gate the optional-mod mixins ({@code BvaDialogueMixin}, {@code BvaConversationMixin},
 * {@code BetterPartyMemberHealthGuardMixin}) behind {@code ModList.get().isLoaded(modId)}. Mixin
 * queries {@link #shouldApplyMixin} while it <em>prepares</em> its configs, a phase that runs
 * before FML has finished populating {@code ModList}, so the check returned {@code null}/early and
 * the plugin answered {@code false} &mdash; permanently disabling those mixins for the entire
 * session (and, for {@code BetterPartyMemberHealthGuardMixin}, letting Better Party's member-health
 * {@code IllegalArgumentException} keep crashing the dedicated server). Every mixin here is either
 * an always-on vanilla target or a {@code @Pseudo} mixin aimed at an optional mod; {@code @Pseudo}
 * already makes Mixin skip a mixin harmlessly when its target class is absent, so gating in this
 * plugin is unnecessary. Always allow, and let {@code @Pseudo} decide at apply time.
 */
public final class BvaMixinPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String MIXIN_PACKAGE = "com.skd.mysticalrealmsmodpackfixes.mixin.";
    private static final String BETTER_PARTY_HEALTH_GUARD_MIXIN = MIXIN_PACKAGE + "BetterPartyMemberHealthGuardMixin";

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        // See class javadoc: gating on ModList here is unreliable (queried before it is populated)
        // and returning false disables the mixin permanently. @Pseudo handles absent targets.
        return true;
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
        if (BETTER_PARTY_HEALTH_GUARD_MIXIN.equals(mixinClassName)) {
            LOGGER.info("[mystical_realms_modpack_fixes] Applied Better Party member-health guard to {}", targetClassName);
        }
    }
}
