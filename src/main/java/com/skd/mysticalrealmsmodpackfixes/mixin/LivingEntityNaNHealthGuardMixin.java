package com.skd.mysticalrealmsmodpackfixes.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Defensive guard for {@link LivingEntity#setHealth(float)} and
 * {@link LivingEntity#setAbsorptionAmount(float)}.
 *
 * <p>Root cause of the corruption we investigated on the <em>Mystical Realms</em> server:
 * Apothic Attributes' {@code AttributeEvents#meleeDamageAttributes(LivingIncomingDamageEvent)}
 * computes {@code float hpDmg = (float) attacker.getAttributeValue(CURRENT_HP_DAMAGE) *
 * target.getHealth();} — a "true damage = percent of target's current health" mechanic. If the
 * attacking entity's {@code CURRENT_HP_DAMAGE} attribute value is ever {@code Infinity} (plausible
 * via a malformed Apotheosis affix roll on the Aquamirae giant underwater boss) and the target's
 * health is {@code 0.0F} at that exact instant, {@code Infinity * 0.0F = NaN} in IEEE-754 float
 * math. That {@code NaN} then flows into {@code LivingEntity#hurt(...)} and is subtracted from
 * health via {@code setHealth(currentHealth - NaN)}, permanently poisoning {@code Health}. A
 * similar overheal-to-absorption codepath in the same class poisons {@code AbsorptionAmount} the
 * same way. Both values are saved to disk verbatim as {@code NaN}.
 *
 * <p>Direct NBT inspection of the affected player's {@code .dat} file (with nbtlib) confirmed that
 * <em>only</em> {@code Health} and {@code AbsorptionAmount} were {@code NaN}; every other field,
 * including all vanilla/modded attributes such as {@code minecraft:generic.max_health}, was
 * completely normal.
 *
 * <p>This mixin does <strong>not</strong> patch Apothic Attributes (we cannot and will not modify
 * another mod's JAR); it sits on the vanilla setter boundary and:
 * <ol>
 *   <li>Rejects any incoming non-finite value before it can corrupt the entity's state, keeping
 *       the last good value instead.</li>
 *   <li>Self-heals an already-corrupted value that was loaded from disk (the exact scenario we
 *       hit), resetting health to {@code getMaxHealth()} (or {@code 20.0F} if that attribute is
 *       itself invalid) and absorption to {@code 0.0F}.</li>
 * </ol>
 */
@Mixin(value = LivingEntity.class, remap = false)
public abstract class LivingEntityNaNHealthGuardMixin {

    private static final Logger MRMF_LOGGER = LogUtils.getLogger();
    private static final float FALLBACK_HEALTH = 20.0F;
    private static final float FALLBACK_ABSORPTION = 0.0F;

    @Shadow
    public abstract float getHealth();

    @Shadow
    public abstract void setHealth(float health);

    @Shadow
    public abstract float getMaxHealth();

    @Shadow
    public abstract float getAbsorptionAmount();

    @Shadow
    public abstract void setAbsorptionAmount(float amount);

    private String mysticalRealmsModpackFixes$describeEntity() {
        Entity self = (Entity) (Object) this;
        return self.getName().getString() + " (" + self.getStringUUID() + ")";
    }

    @Inject(method = "setHealth(F)V", at = @At("HEAD"), cancellable = true)
    private void mysticalRealmsModpackFixes$guardSetHealth(float health, CallbackInfo ci) {
        if (Float.isFinite(health)) {
            return;
        }

        float current = this.getHealth();
        if (!Float.isFinite(current)) {
            float maxHealth = this.getMaxHealth();
            float safe = Float.isFinite(maxHealth) && maxHealth > 0.0F ? maxHealth : FALLBACK_HEALTH;
            MRMF_LOGGER.warn(
                    "[mystical_realms_modpack_fixes] {} had corrupted health ({}); self-healing to {}",
                    this.mysticalRealmsModpackFixes$describeEntity(), current, safe);
            this.setHealth(safe);
        } else {
            MRMF_LOGGER.warn(
                    "[mystical_realms_modpack_fixes] Rejected non-finite health write ({} -> keep {}) for {}",
                    health, current, this.mysticalRealmsModpackFixes$describeEntity());
        }

        ci.cancel();
    }

    @Inject(method = "setAbsorptionAmount(F)V", at = @At("HEAD"), cancellable = true)
    private void mysticalRealmsModpackFixes$guardSetAbsorptionAmount(float absorption, CallbackInfo ci) {
        if (Float.isFinite(absorption)) {
            return;
        }

        float current = this.getAbsorptionAmount();
        if (!Float.isFinite(current)) {
            MRMF_LOGGER.warn(
                    "[mystical_realms_modpack_fixes] {} had corrupted absorption ({}); self-healing to {}",
                    this.mysticalRealmsModpackFixes$describeEntity(), current, FALLBACK_ABSORPTION);
            this.setAbsorptionAmount(FALLBACK_ABSORPTION);
        } else {
            MRMF_LOGGER.warn(
                    "[mystical_realms_modpack_fixes] Rejected non-finite absorption write ({} -> keep {}) for {}",
                    absorption, current, this.mysticalRealmsModpackFixes$describeEntity());
        }

        ci.cancel();
    }
}
