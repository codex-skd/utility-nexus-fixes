package com.skd.mysticalrealmsmodpackfixes;

import com.mojang.logging.LogUtils;
import com.skd.mysticalrealmsmodpackfixes.common.attachment.ModAttachments;
import com.skd.mysticalrealmsmodpackfixes.config.MRMFConfig;
import com.skd.mysticalrealmsmodpackfixes.log.BenignLogFilter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(MysticalRealmsModpackFixes.MODID)
public class MysticalRealmsModpackFixes {
    public static final String MODID = "mystical_realms_modpack_fixes";
    public static final String VERSION = "1.3.0";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static boolean FILTER_INSTALLED = false;

    public MysticalRealmsModpackFixes(IEventBus modEventBus, ModContainer modContainer) {
        // config/mystical_realms_modpack_fixes/config.toml
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, MRMFConfig.SPEC, MRMFConfig.CONFIG_PATH);

        // Attachment types (per-entity return-portal tracking for MixinNetherReturnPortalFix)
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);

        if (!FILTER_INSTALLED) {
            try {
                org.apache.logging.log4j.core.LoggerContext ctx =
                    (org.apache.logging.log4j.core.LoggerContext) org.apache.logging.log4j.LogManager.getContext(false);
                ctx.getConfiguration().getRootLogger().addFilter(new BenignLogFilter());
                ctx.updateLoggers();
                FILTER_INSTALLED = true;
                LOGGER.info("Registered benign-log-noise filter");
            } catch (Throwable t) {
                LOGGER.warn("Could not install benign-log-noise filter: {}", t.toString());
            }
        }

        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("Mystical Realms Modpack Fixes loaded! v{}", VERSION);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Mystical Realms Modpack Fixes common setup complete");
    }
}
