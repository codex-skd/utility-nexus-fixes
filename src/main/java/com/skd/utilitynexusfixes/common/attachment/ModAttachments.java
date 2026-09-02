package com.skd.utilitynexusfixes.common.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, "utility_nexus_fixes");

    public static final Supplier<AttachmentType<ReturnPortalData>> RETURN_PORTAL_DATA =
            ATTACHMENT_TYPES.register("return_portal_data",
                    () -> AttachmentType.builder(ReturnPortalData::new).build());

    private ModAttachments() {
    }
}
