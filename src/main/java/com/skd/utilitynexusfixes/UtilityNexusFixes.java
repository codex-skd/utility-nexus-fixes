package com.skd.utilitynexusfixes;

import com.mojang.logging.LogUtils;
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

    public UtilityNexusFixes(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::onCommonSetup);

        LOGGER.info("Utility Nexus Fixes loaded! v{}", VERSION);
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Utility Nexus Fixes common setup complete");
    }
}
