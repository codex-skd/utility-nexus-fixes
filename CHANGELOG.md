# Changelog

All notable changes to this project will be documented in this file.

## [2.0.0] - 2026-09-22

### Changed (BREAKING)
- **Mod renamed: "Utility Nexus Fixes" → "Mystical Realms Modpack Fixes"**. This mod leaves the `utility_nexus_*` family (`utility_nexus_admin`, `utility_nexus_qol`, `utility_core` are unaffected and keep their names) to become an independently-branded mod tied to the Mystical Realms modpack it was built for. This is a breaking change for anyone updating from a previous version:
  - `mod_id`: `utility_nexus_fixes` → `mystical_realms_modpack_fixes` — the old JAR must be **removed**, not replaced in place; NeoForge treats this as a different mod.
  - Java package: `com.skd.utilitynexusfixes` → `com.skd.mysticalrealmsmodpackfixes`; main class `UtilityNexusFixes` → `MysticalRealmsModpackFixes`; config class `UNFConfig` → `MRMFConfig`.
  - Config file moved: `config/utility_nexus/fixes/config.toml` → `config/mystical_realms_modpack_fixes/config.toml`. The old config file is **not migrated automatically** — reapply any non-default settings in the new file (the old one can be deleted).
  - Assets namespace: `assets/utility_nexus_fixes/` → `assets/mystical_realms_modpack_fixes/`.
  - NeoForge data attachment registry name changed (`utility_nexus_fixes:return_portal_data` → `mystical_realms_modpack_fixes:return_portal_data`) — any previously-remembered Nether return-portal mapping is dropped and regenerates on its own; not a data-loss concern (in-memory-equivalent convenience data).
  - GitLab repository moved to `https://gitlab.com/stalking-dragons/minecraft/mystical-realms-modpack-fixes`.
  - No functional/behavioral changes — every patch (Iris/Sodium, log-noise filters, Nether return portal, Better Party guard, Better Villager Animations translation, NaN/Infinity health guard, Dangerous NeoForge boss health fix) works exactly as in 1.4.0.

## [1.4.0] - 2026-09-22

### Added
- **"Dangerous NeoForge" boss health scaling fix** (optional, server-side): `Dangerous#scaleMonsterHealth` scales monster max health by a day-elapsed multiplier and handles regular mobs correctly — it caches the mob's true base health once in NBT (`DangerousBaseHealth`) and always recomputes `trueBase * currentMultiplier`. Its "boss" branch (any mob whose saved NBT carries `apotheosis:tier_augments_applied`, i.e. any Apotheosis rarity-affixed mob — common on the *(Develop) Mystical Realms* pack) is broken: it reads the mob's *current, already-scaled* max health instead of the true base and multiplies it again by the new day-based factor, so every subsequent rescale compounds on top of the last one instead of scaling linearly, making these mobs' health balloon exponentially over time. New soft mixin `DangerousBossHealthFixMixin` wraps the single `AttributeInstance#getValue()` call in that boss branch and makes it reuse the same `DangerousBaseHealth` cache the non-boss branch already uses, unifying both branches and restoring linear scaling. Gated by a new config toggle (`enableDangerousBossHealthFix`, default `true`). Does not modify the Dangerous NeoForge jar — the mod is an optional runtime dependency, patched entirely via `@Pseudo` mixin.

## [1.3.0] - 2026-09-21

### Added
- **LivingEntity NaN/Infinity health and absorption guard** (always-on, client and server): protects the vanilla `LivingEntity#setHealth(float)` and `LivingEntity#setAbsorptionAmount(float)` setters from non-finite writes. Root cause, confirmed via direct NBT inspection of the affected player's save file on the *(Develop) Mystical Realms* pack: the Aquamirae giant underwater boss applied Apothic Attributes' `CURRENT_HP_DAMAGE` percent-of-current-health true-damage attribute; when that attribute value was `Infinity` (plausible from a malformed Apotheosis affix roll layered onto the boss by a third mod) and the target's health was `0.0F` at the exact instant of the hit, `Infinity * 0.0F = NaN` in IEEE-754 float math. That `NaN` flowed into `LivingEntity#hurt(...)` and was subtracted from health via `setHealth(currentHealth - NaN)`, permanently poisoning `Health`; a similar overheal-to-absorption codepath in the same class poisoned `AbsorptionAmount`. nbtlib inspection showed *only* `Health` and `AbsorptionAmount` were `NaN` while every other field, including `minecraft:generic.max_health`, was intact. Two HEAD-cancellable mixin injections now reject any non-finite write before it can reach the entity's state and log a warning naming the entity, the rejected value, and the value being kept. They also self-heal already-corrupted values loaded from disk: non-finite health is reset to `getMaxHealth()` (or `20.0F` if that attribute is itself invalid) and non-finite absorption is reset to `0.0F`, so affected players recover automatically without manual NBT editing. This does not patch Apothic Attributes itself — it only hardens vanilla against the corrupted values that mod can produce.

## [1.2.1] - 2026-09-20

### Fixed
- **Better Party member-health guard never actually applied**: `BvaMixinPlugin.shouldApplyMixin` gated the soft mixins behind `ModList.get().isLoaded(modId)`, but Mixin queries config plugins while *preparing* its configs — before FML has populated `ModList` — so the check saw a not-yet-ready list and answered `false`, permanently disabling `BetterPartyMemberHealthGuardMixin` (and the two Better Villager Animations dialogue mixins) for the whole session. That is why Better Party v1.1.7 still crashed the dedicated server with `IllegalArgumentException: Party member health must be finite and maxHealth must be positive` even with v1.2.0 installed. Every mixin in this mod's config is either an always-on vanilla target or a `@Pseudo` mixin aimed at an optional mod, and `@Pseudo` already makes Mixin skip a mixin harmlessly when the target class is absent — so the plugin no longer gates and simply allows every mixin through. Added a `postApply` log line confirming when the Better Party guard is applied. Verified on a dev server: `Mixing BetterPartyMemberHealthGuardMixin ... into dev.betterparty.api.v1.server.BetterPartyServerApi`.

## [1.2.0] - 2026-09-19

### Added
- **Better Party member-health crash guard** (optional, server-side): `BetterPartyServerApi.memberHealth(ServerPlayer)` falls back to `new PartyMemberHealth(player.getHealth(), player.getMaxHealth())` whenever no custom health-provider add-on is registered, and that record's constructor throws an uncaught `IllegalArgumentException` if either value is non-finite or `maxHealth <= 0` — which propagates out of `BetterParty#onServerTick` and crashes the entire dedicated server mid-tick (observed: `better-party` v1.1.5 on the *(Develop) Mystical Realms* pack, NeoForge 21.1.249, triggered by a single player's corrupted `generic.max_health` attribute). New soft, server-only mixin `BetterPartyMemberHealthGuardMixin` (gated by `BvaMixinPlugin` on `better_party` actually being loaded) wraps the two vanilla `ServerPlayer` accessor calls that feed the record and substitutes a safe finite value if either reading is currently invalid, logging a warning instead of crashing. Does not fix whatever corrupts the attribute in the first place — only stops that corruption from taking the server down.

### Changed
- `BvaMixinPlugin` generalized from a Better Villager Animations-only gate to a mixin-name -> mod-id map, since Mixin only allows one config plugin per JSON config and the new Better Party guard needed the same soft-mixin gating.

## [1.1.1] - 2026-09-15

### Fixed
- **`BvaMixinPlugin` crash noise on every launch**: `shouldApplyMixin` called `ModList.get().isLoaded(...)` to gate the two Better Villager Animations soft mixins, but Mixin config plugins are queried while preparing mixin configs, a phase that runs before FML has finished populating `ModList` — so `ModList.get()` returned `null` and every launch logged two `NullPointerException`s wrapped in `InvalidMixinException`. Now guards against `ModList.get()` returning `null` and simply skips the two soft mixins in that case (the correct outcome anyway when `bettervillageranimations` isn't loaded yet), so the log stays clean.

## [1.1.0] - 2026-09-14

### Added
- **Better Villager Animations dialogue translation patch** (optional, client-side): that mod keeps its villager speech-bubble flavor text as hardcoded Java strings, so normal `lang` files cannot translate it. Two soft client-only mixins (`BvaDialogueMixin`, `BvaConversationMixin`, gated by `BvaMixinPlugin` on `bettervillageranimations` being present) intercept every `String.replace(CharSequence, CharSequence)` call inside each dialogue catalog's `select(...)` method — translating the raw English template *before* the mod substitutes its own placeholder tokens (`{player}`, `{speaker_goods}`, `{time}`, `{weather}`, etc.) — and substitute the Spanish text from `assets/bettervillageranimations/dialogue/es_es.json` supplied by a separately installed resource pack. Active only when both mods are present and the client language is exactly `es_es`; every other case returns the original line unchanged. The table reloads with client resource packs (`RegisterClientReloadListenersEvent`) and the lookup never throws.

## [1.0.0] - 2026-09-09

First stable release for **Minecraft 1.21.1 / NeoForge 21.1.249** (Java 21). Same code as
`0.0.0-beta.3`; promoted to stable after running in the *(Develop) Mystical Realms* modded-server
pack.

### Summary of the beta line

- **beta.1** — Iris / Sodium compat patch (fixes the
  `ClassNotFoundException: SodiumGameOptions$PerformanceSettings` crash from Iris 1.8.12 against
  Sodium 0.8.13+); lenient entity `CustomName` parse mixin (malformed NBT falls back to plain text
  instead of spamming `Failed to parse entity custom name` every tick); configurable Log4j2
  `BenignLogFilter` for a small allow-list of benign stale-NBT / legacy-attribute log lines
  (`[logfilter]` in `config/utility_nexus/fixes/config.toml`).
- **beta.2** — fixed a mod-loading crash: the benign-log filter is installed from the `@Mod`
  constructor, before the COMMON config loads, so an early log line hit
  `ModConfigSpec.ConfigValue#get()` and threw. `UNFConfig` now falls back to the built-in
  defaults until `ModConfigSpec#isLoaded()` is true.
- **beta.3** — Nether return-portal fix (`MixinNetherReturnPortalFix`): records the
  origin → exit portal pairing per entity on an Overworld↔Nether trip and forces the remembered
  portal on the return trip, instead of vanilla's "closest portal" search. Ported from
  `utility_core_fixes` (26.2); toggle `[fixes] enableNetherReturnPortalFix` (default `true`).
  In-memory only (mappings reset on restart).

### Notes

- No code change relative to `0.0.0-beta.3`. Verified: `./gradlew clean build` is green.

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
