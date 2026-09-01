package net.caffeinemc.mods.sodium.client.gui;

/**
 * Compatibility stub for the class Sodium renamed to {@code SodiumOptions}
 * starting with 0.8.13.
 *
 * <p>Iris 1.8.12's bundled Sodium-compat mixin
 * ({@code net.irisshaders.iris.compat.sodium.mixin.MixinRenderSectionManager})
 * has {@code SodiumGameOptions$PerformanceSettings} compiled into its own
 * bytecode (as a method parameter type, a field-access descriptor, and the
 * literal {@code target} string of an {@code @At} annotation). When Sodium
 * 0.8.13+ is present, that class no longer exists under the old name and
 * SpongeMixin's pre-processor throws a {@code ClassNotFoundException} while
 * resolving Iris's own mixin bytecode — before it even attempts to match the
 * {@code @Redirect} against a real target method. That aborts the whole
 * mixin transform and crashes the client on world join.</p>
 *
 * <p>This class exists purely so that lookup succeeds. It is never meant to
 * be instantiated or to mirror Sodium's real option values: because the
 * {@code @At} target string inside Iris's compiled bytecode still literally
 * reads {@code SodiumGameOptions$PerformanceSettings}, it can never match a
 * field on the real (renamed) {@code SodiumOptions$PerformanceSettings}
 * class either — so once pre-processing succeeds, Mixin simply finds no
 * matching injection point for that specific {@code @Redirect} and skips it
 * (Iris's fog-occlusion micro-optimisation silently does not apply; nothing
 * else is affected). No Iris or Sodium jar is modified.</p>
 */
public final class SodiumGameOptions {

    private SodiumGameOptions() {
    }

    public static final class PerformanceSettings {
        public boolean useFogOcclusion = true;
    }
}
