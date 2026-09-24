package com.skd.mysticalrealmsmodpackfixes.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class EntityCustomNameLenientMixin {
    private static final Logger MRMF_LOGGER = LogUtils.getLogger();

    @WrapOperation(
        method = "load",
        at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/Component$Serializer;fromJson(Ljava/lang/String;Lnet/minecraft/core/HolderLookup$Provider;)Lnet/minecraft/network/chat/MutableComponent;"))
    private MutableComponent mysticalRealmsModpackFixes$lenientCustomName(String json, HolderLookup.Provider provider, Operation<MutableComponent> original) {
        try {
            return original.call(json, provider);
        } catch (Exception e) {
            String s = json;
            if (s != null && s.length() >= 2 && s.charAt(0) == '"' && s.charAt(s.length() - 1) == '"') {
                s = s.substring(1, s.length() - 1);
            }
            MRMF_LOGGER.warn("[mystical_realms_modpack_fixes] Malformed entity CustomName JSON, using literal fallback: {}", json);
            return Component.literal(s == null ? "" : s);
        }
    }
}
