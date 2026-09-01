package com.skd.utilitynexusfixes;

import com.mojang.logging.LogUtils;
import com.skd.utilitynexusfixes.config.UNFConfig;
import com.skd.utilitynexusfixes.log.BenignLogFilter;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(UtilityNexusFixes.MODID)
public class UtilityNexusFixes {
    public static final String MODID = "utility_nexus_fixes";
    public static final String VERSION = "0.0.0-beta.1";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static boolean FILTER_INSTALLED = false;

    public UtilityNexusFixes(IEventBus modEventBus, ModContainer modContainer) {
        // Same nested layout as utility_nexus_admin: config/utility_nexus/fixes/config.toml
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, UNFConfig.SPEC, UNFConfig.CONFIG_PATH);

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

        LOGGER.info("Utility Nexus Fixes loaded! v{}", VERSION);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Utility Nexus Fixes common setup complete");
    }
}
