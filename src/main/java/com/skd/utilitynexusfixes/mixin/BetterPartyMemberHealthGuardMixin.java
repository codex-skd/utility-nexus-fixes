package com.skd.utilitynexusfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Prevents Better Party's per-tick party sync from crashing the whole server when a player's
 * vanilla health/max-health is momentarily invalid.
 *
 * <p>{@code BetterPartyServerApi.memberHealth(ServerPlayer)} falls back, whenever no custom
 * {@code PartyMemberHealthProvider} add-on is registered, to
 * {@code new PartyMemberHealth(player.getHealth(), player.getMaxHealth())}. That record's
 * constructor throws {@code IllegalArgumentException("Party member health must be finite and
 * maxHealth must be positive")} if either value is non-finite or {@code maxHealth <= 0}. That
 * exception is never caught: it propagates out of {@code BetterParty#onServerTick} through the
 * event bus and kills the entire dedicated server mid-tick for every player online (observed:
 * {@code v1.1.5}, {@code better-party-x-xaeros-world-map}, NeoForge 21.1.249).
 *
 * <p>Soft (string) target: {@code better_party} is an optional dependency and is not on the
 * compile classpath, so {@code PartyMemberHealth} itself cannot be referenced here. Instead of
 * wrapping the record constructor, this wraps the two vanilla {@link ServerPlayer} accessor calls
 * that feed it &mdash; both real, compiled Minecraft types &mdash; and substitutes a safe finite
 * value if either reading is currently broken, so Better Party keeps ticking instead of crashing
 * the server. This does not fix whatever corrupted the player's {@code generic.max_health}
 * attribute in the first place; it only stops that corruption from taking the server down.
 */
@Pseudo
@Mixin(targets = "dev.betterparty.api.v1.server.BetterPartyServerApi", remap = false)
public abstract class BetterPartyMemberHealthGuardMixin {

    private static final Logger UNF_LOGGER = LogUtils.getLogger();
    private static final float FALLBACK_MAX_HEALTH = 1.0F;

    @WrapOperation(
        method = "memberHealth(Lnet/minecraft/server/level/ServerPlayer;)Ldev/betterparty/api/v1/model/PartyMemberHealth;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getMaxHealth()F"),
        remap = false
    )
    private static float utilityNexusFixes$guardMaxHealth(ServerPlayer player, Operation<Float> original) {
        float maxHealth = original.call(player);
        if (!Float.isFinite(maxHealth) || maxHealth <= 0.0F) {
            UNF_LOGGER.warn("[utility_nexus_fixes] {} has an invalid max-health attribute ({}); reporting {} to Better Party instead of crashing the server",
                    player.getGameProfile().getName(), maxHealth, FALLBACK_MAX_HEALTH);
            return FALLBACK_MAX_HEALTH;
        }
        return maxHealth;
    }

    @WrapOperation(
        method = "memberHealth(Lnet/minecraft/server/level/ServerPlayer;)Ldev/betterparty/api/v1/model/PartyMemberHealth;",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F"),
        remap = false
    )
    private static float utilityNexusFixes$guardHealth(ServerPlayer player, Operation<Float> original) {
        float health = original.call(player);
        if (!Float.isFinite(health)) {
            UNF_LOGGER.warn("[utility_nexus_fixes] {} has a non-finite health value ({}); reporting 0 to Better Party instead of crashing the server",
                    player.getGameProfile().getName(), health);
            return 0.0F;
        }
        return Math.max(health, 0.0F);
    }
}
