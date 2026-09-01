package com.skd.utilitynexusfixes.compat.irissodium;

import com.skd.utilitynexusfixes.UtilityNexusFixes;
import cpw.mods.modlauncher.api.NamedPath;
import cpw.mods.modlauncher.serviceapi.ILaunchPluginService;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.ClassNode;

import java.util.EnumSet;
import java.util.Map;

/**
 * Launch plugin service that rewrites Iris's Sodium-compat mixin bytecode
 * before SpongeMixin's own transformer preprocesses it.
 *
 * <p>Iris 1.8.12 bundles mixin classes that reference the old Sodium class name
 * {@code SodiumGameOptions} (and its nested {@code $PerformanceSettings}).
 * Sodium 0.8.13 renamed the outer class to {@code SodiumOptions}; the nested
 * class and all fields are unchanged.  When SpongeMixin tries to resolve the
 * old name during mixin preprocessing it throws a {@code ClassNotFoundException}
 * and the game crashes on world join.</p>
 *
 * <p>This plugin intercepts every class under
 * {@code net.irisshaders.iris.compat.sodium.mixin.} during the
 * {@link Phase#BEFORE} phase and uses ASM {@link ClassRemapper} to rename all
 * constant-pool and descriptor references from {@code SodiumGameOptions} to
 * {@code SodiumOptions}.  Because the mixin class is loaded (via the
 * {@link cpw.mods.modlauncher.serviceapi.ILaunchPluginService.ITransformerLoader}
 * provided during {@link #initializeLaunch}) <em>before</em> SpongeMixin ever
 * applies it to a Sodium target class, the rewritten bytecode is what
 * SpongeMixin sees — the stale reference never reaches its pre-processor.</p>
 *
 * <h3>Ordering guarantee</h3>
 * <p>{@code ILaunchPluginService} has no inter-plugin priority mechanism; within
 * a phase the execution order among plugins is undefined.  That is irrelevant
 * here because our plugin and SpongeMixin's plugin operate on
 * <em>different</em> classes:</p>
 * <ul>
 *   <li>Our plugin handles the <b>mixin class</b> itself
 *       ({@code net.irisshaders.iris.compat.sodium.mixin.*}).</li>
 *   <li>SpongeMixin's plugin ({@code MixinLaunchPluginLegacy}) handles the
 *       <b>target class</b> (a Sodium rendering class) when it is first
 *       loaded, and only then reads the mixin class bytes to apply them.</li>
 * </ul>
 * <p>The mixin class is loaded early (during mixin-config processing at mod
 * startup) while the target class is loaded later (when first referenced at
 * world-join time).  Our rewrite therefore takes place well before
 * SpongeMixin reads the mixin bytes.</p>
 *
 * <h3>Safety</h3>
 * <ul>
 *   <li>Only classes whose fully-qualified name starts with
 *       {@code net.irisshaders.iris.compat.sodium.mixin.} are touched.</li>
 *   <li>If any error occurs during rewriting the original, unmodified class
 *       bytes are returned and a warning is logged — the mod degrades
 *       gracefully and never makes the crash worse.</li>
 *   <li>No Iris or Sodium jar files are modified; they remain read-only
 *       compile references.</li>
 * </ul>
 */
public final class IrisSodiumRewritePlugin implements ILaunchPluginService {

    /**
     * Unique name for this launch plugin.  Must not collide with any other
     * registered {@code ILaunchPluginService} name.
     */
    private static final String PLUGIN_NAME = "utility_nexus_fixes_sodium_compat";

    /**
     * Prefix that all of Iris's Sodium-compat mixin classes share.
     */
    private static final String IRIS_SODIUM_MIXIN_PKG = "net.irisshaders.iris.compat.sodium.mixin.";

    /**
     * Old (pre-0.8.13) outer class name that Iris's mixin bytecode still
     * references.  Internal-form (slashes, not dots).
     */
    private static final String OLD_OUTER = "net/caffeinemc/mods/sodium/client/gui/SodiumGameOptions";

    /**
     * New (0.8.13+) outer class name.  Internal-form.
     */
    private static final String NEW_OUTER = "net/caffeinemc/mods/sodium/client/gui/SodiumOptions";

    /**
     * Complete remapping table.  Covers the outer class and every known
     * nested class.  Only entries that actually appear in Iris's mixin
     * bytecode are strictly necessary, but listing the full inner-class
     * name ensures the constant-pool, descriptor, and signature references
     * are all rewritten in a single pass.
     */
    private static final Map<String, String> REMAP_TABLE = Map.of(
            OLD_OUTER,                          NEW_OUTER,
            OLD_OUTER + "$PerformanceSettings", NEW_OUTER + "$PerformanceSettings"
    );

    // -------------------------------------------------------------- public

    @Override
    public String name() {
        return PLUGIN_NAME;
    }

    @Override
    public EnumSet<Phase> handlesClass(final Type classType, final boolean isEmpty) {
        return handlesClass(classType, isEmpty, "");
    }

    @Override
    public EnumSet<Phase> handlesClass(final Type classType, final boolean isEmpty,
                                       final String reason) {
        // Never intercept when Mixin itself is loading a class (avoids
        // re-entry).  In every other case, if the class lives in Iris's
        // Sodium-compat mixin package we want to see it during BEFORE so
        // we can rewrite it before SpongeMixin's own plugin reads it.
        if ("mixin".equals(reason)) {
            return EnumSet.noneOf(Phase.class);
        }
        if (classType.getClassName().startsWith(IRIS_SODIUM_MIXIN_PKG)) {
            return EnumSet.of(Phase.BEFORE);
        }
        return EnumSet.noneOf(Phase.class);
    }

    @Override
    public boolean processClass(final Phase phase, final ClassNode classNode,
                                final Type classType) {
        final String className = classType.getClassName();

        // Double-guard: only rewrite Iris's Sodium-compat mixin classes.
        if (!className.startsWith(IRIS_SODIUM_MIXIN_PKG)) {
            return false;
        }

        UtilityNexusFixes.LOGGER.info(
                "[UtilityNexusFixes] Rewriting stale SodiumGameOptions references "
                        + "in Iris mixin class: {}", className);

        try {
            // ----------------------------------------------------------
            // 1. Write the incoming ClassNode to bytes through a
            //    ClassRemapper that renames the old Sodium class names to
            //    the new ones.  ClassRemapper + SimpleRemapper handle every
            //    kind of reference (descriptors, signatures, type
            //    annotations, etc.) in a single visitor pass.
            // ----------------------------------------------------------
            final SimpleRemapper remapper = new SimpleRemapper(REMAP_TABLE);

            final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            final ClassRemapper remappingVisitor = new ClassRemapper(cw, remapper);
            classNode.accept(remappingVisitor);
            final byte[] rewrittenBytes = cw.toByteArray();

            // ----------------------------------------------------------
            // 2. Read the rewritten bytes back into a fresh ClassNode so
            //    we can replace the contents of the original node that the
            //    framework will later serialise.
            // ----------------------------------------------------------
            final ClassNode rewrittenNode = new ClassNode();
            new ClassReader(rewrittenBytes).accept(rewrittenNode, 0);

            // ----------------------------------------------------------
            // 3. Swap every mutable field from the rewritten node into the
            //    original.  We deliberately keep the original node's
            //    identity (object reference) because the framework already
            //    holds a reference to it.
            // ----------------------------------------------------------
            classNode.version       = rewrittenNode.version;
            classNode.access        = rewrittenNode.access;
            classNode.name          = rewrittenNode.name;
            classNode.signature     = rewrittenNode.signature;
            classNode.superName     = rewrittenNode.superName;

            classNode.interfaces.clear();
            classNode.interfaces.addAll(rewrittenNode.interfaces);

            classNode.sourceFile    = rewrittenNode.sourceFile;
            classNode.sourceDebug   = rewrittenNode.sourceDebug;
            classNode.module        = rewrittenNode.module;
            classNode.nestHostClass = rewrittenNode.nestHostClass;

            classNode.nestMembers.clear();
            classNode.nestMembers.addAll(rewrittenNode.nestMembers);

            classNode.permittedSubclasses.clear();
            classNode.permittedSubclasses.addAll(rewrittenNode.permittedSubclasses);

            classNode.recordComponents.clear();
            classNode.recordComponents.addAll(rewrittenNode.recordComponents);

            classNode.innerClasses.clear();
            classNode.innerClasses.addAll(rewrittenNode.innerClasses);

            classNode.fields.clear();
            classNode.fields.addAll(rewrittenNode.fields);

            classNode.methods.clear();
            classNode.methods.addAll(rewrittenNode.methods);

            classNode.visibleAnnotations   = rewrittenNode.visibleAnnotations;
            classNode.invisibleAnnotations = rewrittenNode.invisibleAnnotations;
            classNode.visibleTypeAnnotations   = rewrittenNode.visibleTypeAnnotations;
            classNode.invisibleTypeAnnotations = rewrittenNode.invisibleTypeAnnotations;

            UtilityNexusFixes.LOGGER.info(
                    "[UtilityNexusFixes] Successfully rewrote SodiumGameOptions "
                            + "-> SodiumOptions references in {}", className);
            return true;

        } catch (final Exception e) {
            // On any failure return the original, unmodified ClassNode so
            // we never make the situation worse.
            UtilityNexusFixes.LOGGER.warn(
                    "[UtilityNexusFixes] Failed to rewrite SodiumGameOptions "
                            + "references in {}: {} — falling back to original "
                            + "bytecode",
                    className, e.getMessage());
            return false;
        }
    }

    // -------------------------------------------------------------- lifecycle

    @Override
    public void initializeLaunch(final ITransformerLoader transformerLoader,
                                 final NamedPath[] specialPaths) {
        UtilityNexusFixes.LOGGER.info(
                "[UtilityNexusFixes] Iris/Sodium compat rewrite plugin initialised");
    }
}
