package com.skd.utilitynexusfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.logging.LogUtils;
import com.skd.utilitynexusfixes.config.UNFConfig;
import com.skd.utilitynexusfixes.common.attachment.ModAttachments;
import com.skd.utilitynexusfixes.common.attachment.ReturnPortalData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalForcer;
import net.minecraft.world.level.border.WorldBorder;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

/**
 * Fixes Nether portals sending an entity to the wrong destination portal on the return trip.
 *
 * <p>Vanilla's {@code NetherPortalBlock#getExitPortal} always calls
 * {@code PortalForcer#findClosestPortalPosition}, which returns the closest existing portal to the
 * scaled coordinates &mdash; not the one the entity originally came through. This wraps that call and,
 * for Overworld&#8596;Nether trips, remembers the origin&#8594;exit portal pairing on a per-entity
 * attachment, then forces the remembered portal on the way back if it is still valid.
 *
 * <p>Ported verbatim in behaviour from {@code utility_core_fixes} (NeoForge 26.2); rewritten from
 * {@code @Redirect} to MixinExtras {@code @WrapOperation} because the standalone Sponge Mixin AP
 * bundled for NeoForge 21.1.249 cannot resolve an obfuscation mapping for the {@code @Redirect}
 * target and hard-errors at compile time.
 */
@Mixin(NetherPortalBlock.class)
public abstract class MixinNetherReturnPortalFix {

    private static final Logger UNF_LOGGER = LogUtils.getLogger();

    @Unique
    private static boolean utilityNexusFixes$isOverworldToNether(ResourceKey<Level> origin, ResourceKey<Level> destination) {
        return Level.OVERWORLD.equals(origin) && Level.NETHER.equals(destination);
    }

    @Unique
    private static boolean utilityNexusFixes$isNetherToOverworld(ResourceKey<Level> origin, ResourceKey<Level> destination) {
        return Level.NETHER.equals(origin) && Level.OVERWORLD.equals(destination);
    }

    @Unique
    private static BlockPos utilityNexusFixes$findOriginPortalPos(Entity entity, BlockPos portalEntryPos) {
        BlockState state = entity.level().getBlockState(portalEntryPos);
        if (state.is(Blocks.NETHER_PORTAL)) {
            return portalEntryPos.immutable();
        }
        for (Direction dir : Direction.values()) {
            BlockPos offset = portalEntryPos.relative(dir);
            if (entity.level().getBlockState(offset).is(Blocks.NETHER_PORTAL)) {
                return offset.immutable();
            }
        }
        return null;
    }

    @WrapOperation(
        method = "getExitPortal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/portal/PortalForcer;findClosestPortalPosition(Lnet/minecraft/core/BlockPos;ZLnet/minecraft/world/level/border/WorldBorder;)Ljava/util/Optional;"
        )
    )
    private Optional<BlockPos> utilityNexusFixes$redirectFindClosestPortal(
        PortalForcer instance,
        BlockPos approximateExitPos,
        boolean toNether,
        WorldBorder worldBorder,
        Operation<Optional<BlockPos>> original,
        @Local(argsOnly = true) ServerLevel newLevel,
        @Local(argsOnly = true) Entity entity,
        @Local(argsOnly = true, ordinal = 0) BlockPos portalEntryPos
    ) {
        if (!UNFConfig.netherReturnPortalFixEnabled()) {
            return original.call(instance, approximateExitPos, toNether, worldBorder);
        }

        ResourceKey<Level> originDimension = entity.level().dimension();
        ResourceKey<Level> destinationDimension = newLevel.dimension();

        if (!utilityNexusFixes$isOverworldToNether(originDimension, destinationDimension) && !utilityNexusFixes$isNetherToOverworld(originDimension, destinationDimension)) {
            return original.call(instance, approximateExitPos, toNether, worldBorder);
        }

        ReturnPortalData data = entity.getData(ModAttachments.RETURN_PORTAL_DATA);
        BlockPos originPortalPos = utilityNexusFixes$findOriginPortalPos(entity, portalEntryPos);

        if (originPortalPos == null) {
            UNF_LOGGER.debug("[utility_nexus_fixes] Entity {} not in a nether portal block at {}, skipping return portal tracking", entity, portalEntryPos);
            return original.call(instance, approximateExitPos, toNether, worldBorder);
        }

        if (utilityNexusFixes$isOverworldToNether(originDimension, destinationDimension)) {
            Optional<BlockPos> vanillaResult = original.call(instance, approximateExitPos, toNether, worldBorder);
            vanillaResult.ifPresent(exitPos -> {
                data.setReturnPortal(destinationDimension, exitPos, originPortalPos);
                UNF_LOGGER.debug("[utility_nexus_fixes] Stored return portal mapping: {} (Overworld) -> {} (Nether) for entity {}", originPortalPos, exitPos, entity);
            });
            return vanillaResult;
        }

        if (utilityNexusFixes$isNetherToOverworld(originDimension, destinationDimension)) {
            Optional<BlockPos> remembered = data.getReturnPortal(originDimension, originPortalPos);
            if (remembered.isPresent()) {
                BlockPos rememberedPos = remembered.get();
                if (newLevel.getBlockState(rememberedPos).is(Blocks.NETHER_PORTAL)) {
                    UNF_LOGGER.debug("[utility_nexus_fixes] Redirected return portal: {} (Nether) -> {} (Overworld) for entity {}", originPortalPos, rememberedPos, entity);
                    return Optional.of(rememberedPos);
                } else {
                    data.removeReturnPortal(originDimension, originPortalPos);
                    UNF_LOGGER.debug("[utility_nexus_fixes] Remembered portal at {} no longer valid, falling back to vanilla search", rememberedPos);
                }
            }
            Optional<BlockPos> vanillaResult = original.call(instance, approximateExitPos, toNether, worldBorder);
            vanillaResult.ifPresent(exitPos -> {
                data.setReturnPortal(originDimension, originPortalPos, exitPos);
                UNF_LOGGER.debug("[utility_nexus_fixes] Stored return portal mapping: {} (Nether) -> {} (Overworld) for entity {}", originPortalPos, exitPos, entity);
            });
            return vanillaResult;
        }

        return original.call(instance, approximateExitPos, toNether, worldBorder);
    }
}
