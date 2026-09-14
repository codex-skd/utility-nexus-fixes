package com.skd.utilitynexusfixes.mixin;

import net.neoforged.fml.ModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

/**
 * Mixin config plugin gating the Better Villager Animations soft mixins.
 *
 * <p>{@code BvaDialogueMixin} and {@code BvaConversationMixin} target classes from the optional
 * {@code bettervillageranimations} mod via string targets, so they must only apply when that mod
 * is actually loaded. Every other mixin is unaffected.
 */
public final class BvaMixinPlugin implements IMixinConfigPlugin {
    private static final String MIXIN_PACKAGE = "com.skd.utilitynexusfixes.mixin.";
    private static final String DIALOGUE_MIXIN = MIXIN_PACKAGE + "BvaDialogueMixin";
    private static final String CONVERSATION_MIXIN = MIXIN_PACKAGE + "BvaConversationMixin";
    private static final String AFFECTED_MOD_ID = "bettervillageranimations";

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (DIALOGUE_MIXIN.equals(mixinClassName) || CONVERSATION_MIXIN.equals(mixinClassName)) {
            return ModList.get().isLoaded(AFFECTED_MOD_ID);
        }
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
    }
}
