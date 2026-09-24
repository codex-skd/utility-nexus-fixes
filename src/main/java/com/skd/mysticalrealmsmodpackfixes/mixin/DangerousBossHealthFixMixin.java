package com.skd.mysticalrealmsmodpackfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.logging.LogUtils;
import com.skd.mysticalrealmsmodpackfixes.config.MRMFConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Monster;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Stops "Dangerous NeoForge" from exponentially inflating the max health of Apotheosis-boosted
 * ("boss") mobs on every day-based rescale.
 *
 * <p>Soft (string) target: the affected mod is an optional dependency and is not on the compile
 * classpath. {@code @Pseudo} tells the Mixin annotation processor not to require the target at
 * compile time (it cannot be resolved here by design). Only applied when it is present, and the
 * whole fix can be turned off from the config.
 *
 * <p>{@code Dangerous#scaleMonsterHealth} treats regular mobs correctly: it caches the mob's
 * <em>true</em> {@code generic.max_health} base value in the {@code "DangerousBaseHealth"} NBT tag
 * on the first rescale and thereafter computes {@code trueBase * currentMultiplier}, so health
 * grows linearly with elapsed days. Its "boss" branch (taken whenever the mob's saved NBT carries
 * {@code apotheosis:tier_augments_applied}, which is common in this pack) is broken: it reads
 * {@link AttributeInstance#getValue()} &mdash; the <em>current, already-scaled</em> max health &mdash;
 * as if it were the base and multiplies it by the new factor again. Because the previous rescale
 * already called {@code setBaseValue}, each day-based rescale compounds on top of the last one
 * instead of scaling from the original base, so these mobs' health balloons exponentially.
 *
 * <p>This wraps that single {@code getValue()} call and makes the boss branch behave like the
 * mod's already-correct non-boss branch: cache the true base value under the same
 * {@code "DangerousBaseHealth"} key (shared/consistent with the mod's own non-boss logic) and
 * return that cached value every time instead of the live, already-scaled value. The mod's own
 * scaling curve is otherwise left untouched.
 */
@Pseudo
@Mixin(targets = "com.wardanger.dangerous.Dangerous", remap = false)
public abstract class DangerousBossHealthFixMixin {

    private static final Logger MRMF_LOGGER = LogUtils.getLogger();
    private static final String BASE_HEALTH_KEY = "DangerousBaseHealth";

    @WrapOperation(
        method = "scaleMonsterHealth(Lnet/minecraft/world/entity/monster/Monster;Lnet/minecraft/server/level/ServerLevel;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/attributes/AttributeInstance;getValue()D"
        ),
        remap = false
    )
    private static double mysticalRealmsModpackFixes$useCachedBaseHealth(
        AttributeInstance instance,
        Operation<Double> original,
        @Local(argsOnly = true) Monster monster
    ) {
        if (!MRMFConfig.dangerousBossHealthFixEnabled()) {
            return original.call(instance);
        }

        CompoundTag persistentData = monster.getPersistentData();
        if (!persistentData.contains(BASE_HEALTH_KEY)) {
            double trueBaseHealth = instance.getBaseValue();
            persistentData.putDouble(BASE_HEALTH_KEY, trueBaseHealth);
            MRMF_LOGGER.debug("[mystical_realms_modpack_fixes] Cached true base health {} for boss monster {} so Dangerous NeoForge health scaling stays linear", trueBaseHealth, monster);
        }
        return persistentData.getDouble(BASE_HEALTH_KEY);
    }
}
