# Changelog

All notable changes to this project will be documented in this file.

## [0.0.0-beta.3]

### Added
- **Nether return-portal fix** (`MixinNetherReturnPortalFix`): vanilla's `NetherPortalBlock#getExitPortal` always routes an entity to the *closest* existing portal at the scaled destination coordinates, so two Overworld portals near the same Nether coordinates can send you back through the wrong one. This wraps the `PortalForcer#findClosestPortalPosition` call and, for Overworld&#8596;Nether trips, records the origin&#8594;exit portal pairing on a per-entity attachment (`utility_nexus_fixes:return_portal_data`), then forces the remembered portal on the return trip while it is still a valid `nether_portal` block. Ported from `utility_core_fixes` (NeoForge 26.2). Toggle: `[fixes] enableNetherReturnPortalFix` in `config/utility_nexus/fixes/config.toml` (default `true`). The attachment is in-memory only (not serialized), so mappings reset on restart.
  - Implemented with MixinExtras `@WrapOperation` instead of `@Redirect`: the standalone Sponge Mixin AP bundled for NeoForge 21.1.249 hard-errors on an unresolved obfuscation mapping for the `@Redirect` target.

## [0.0.0-beta.2]

### Fixed
- **Mod-loading crash from the benign-log filter**: `BenignLogFilter` is installed from the `@Mod` constructor, which runs before NeoForge loads the COMMON config. Any log line emitted during that window (e.g. Supplementaries' `DepthDataHandler` static init triggering a Netty debug log) reached `ModConfigSpec.ConfigValue#get()` before it was loaded, throwing `IllegalStateException: Cannot get config value before config is loaded`. That escaped as an `ExceptionInInitializerError` and aborted mod loading, with the crash report blaming whichever mod happened to log first (`supplementaries`). `UNFConfig.enabled()` / `UNFConfig.patterns()` now fall back to the built-in defaults until `ModConfigSpec#isLoaded()` is true, so the filter still suppresses early stale-NBT noise without touching the unloaded spec.

## [0.0.0-beta.1]

### Added
- Initial project setup.
- **Iris / Sodium compat patch**: fixes the `ClassNotFoundException: SodiumGameOptions$PerformanceSettings` crash caused by Iris 1.8.12's Sodium-compat mixin referencing a class Sodium 0.8.13 renamed to `SodiumOptions`.
- **Lenient entity CustomName parse** (`EntityCustomNameLenientMixin`): malformed `CustomName` JSON in stale NBT now falls back to a plain-text component instead of throwing, so a datapack `tick` function doing `data modify entity ... CustomName <bad value>` no longer spams `Failed to parse entity custom name` with a stack trace every tick.
- **Configurable Log4j2 log filter** (`BenignLogFilter`): drops a small, config-controlled allow-list of benign stale-NBT / legacy-attribute log lines — by default `Tried to load invalid item: 'Item must not be minecraft:air'`, `Tried to load invalid item: 'Unknown registry key ...'`, `Tried to load invalid fluid: ...` and `Ignoring unknown attribute 'forge:...'`. Settings live in `config/utility_nexus/fixes/config.toml` under `[logfilter]` (`enabled`, `patterns`).
- NOTE: a data-preserving mixin remap of the legacy `forge:entity_gravity` / `forge:step_height_addition` attribute ids to their `neoforge:` equivalents was attempted but the Mixin annotation processor for NeoForge 21.1.249 cannot map `AttributeMap#load`. The warning is suppressed by the log filter instead; the stale modifier values (from removed mods) are dropped.
