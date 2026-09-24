package com.skd.mysticalrealmsmodpackfixes.compat.bettervillageranimations;

import com.skd.mysticalrealmsmodpackfixes.MysticalRealmsModpackFixes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;

/**
 * Client event wiring for the Better Villager Animations dialogue translation overlay.
 *
 * <p>Registers {@link BvaDialogueTranslations#RELOAD_LISTENER} so the translation table refreshes
 * whenever client resource packs reload. This subscriber is only ever invoked on the client:
 * {@link RegisterClientReloadListenersEvent} is a client-only mod-bus event that is never posted
 * on a dedicated server, and neither this class nor {@link BvaDialogueTranslations} is referenced
 * from any server-side code path. Static initialization is intentionally trivial so that even
 * class loading outside the client is harmless.
 */
@EventBusSubscriber(modid = MysticalRealmsModpackFixes.MODID)
public final class BvaClientSetup {
    private BvaClientSetup() {
    }

    @SubscribeEvent
    public static void onRegisterClientReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(BvaDialogueTranslations.RELOAD_LISTENER);
    }
}
