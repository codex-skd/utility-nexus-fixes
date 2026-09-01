# Changelog

All notable changes to this project will be documented in this file.

## [0.0.0-beta.1]

### Added
- Initial project setup.
- **Iris / Sodium compat patch**: fixes the `ClassNotFoundException: SodiumGameOptions$PerformanceSettings` crash caused by Iris 1.8.12's Sodium-compat mixin referencing a class Sodium 0.8.13 renamed to `SodiumOptions`.
- **Lenient entity CustomName parse** (`EntityCustomNameLenientMixin`): malformed `CustomName` JSON in stale NBT now falls back to a plain-text component instead of throwing, so a datapack `tick` function doing `data modify entity ... CustomName <bad value>` no longer spams `Failed to parse entity custom name` with a stack trace every tick.
- **Configurable Log4j2 log filter** (`BenignLogFilter`): drops a small, config-controlled allow-list of benign stale-NBT / legacy-attribute log lines — by default `Tried to load invalid item: 'Item must not be minecraft:air'`, `Tried to load invalid item: 'Unknown registry key ...'`, `Tried to load invalid fluid: ...` and `Ignoring unknown attribute 'forge:...'`. Settings live in `config/utility_nexus/fixes/config.toml` under `[logfilter]` (`enabled`, `patterns`).
- NOTE: a data-preserving mixin remap of the legacy `forge:entity_gravity` / `forge:step_height_addition` attribute ids to their `neoforge:` equivalents was attempted but the Mixin annotation processor for NeoForge 21.1.249 cannot map `AttributeMap#load`. The warning is suppressed by the log filter instead; the stale modifier values (from removed mods) are dropped.
