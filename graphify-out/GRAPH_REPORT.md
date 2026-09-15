# Graph Report - 1.21.1  (2026-09-15)

## Corpus Check
- 30 files · ~51,075 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 194 nodes · 276 edges · 27 communities (24 shown, 3 thin omitted)
- Extraction: 99% EXTRACTED · 1% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `43aaa732`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- Configuration Management
- Logging Filter
- Mod Initialization
- Mixin for Custom Names
- Gradle Build Script
- Performance Settings
- Attribute Map Remap
- Utility Nexus Fixes Icon
- Flujo de trabajo — Utility Nexus Fixes (NeoForge)
- Project Variables — Utility Nexus Fixes
- Utility Nexus Fixes
- Changelog
- CLAUDE.md — utility_nexus_fixes (1.21.1)
- PortalKey
- ModAttachments.java
- UNFConfig
- BvaClientSetup.java
- BvaConversationMixin.java

## God Nodes (most connected - your core abstractions)
1. `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` - 13 edges
2. `PortalKey` - 10 edges
3. `BvaDialogueTranslations` - 10 edges
4. `ReturnPortalData` - 9 edges
5. `UNFConfig` - 9 edges
6. `BvaMixinPlugin` - 9 edges
7. `MixinNetherReturnPortalFix` - 7 edges
8. `Changelog` - 7 edges
9. `Utility Nexus Fixes` - 7 edges
10. `Project Variables — Utility Nexus Fixes` - 7 edges

## Surprising Connections (you probably didn't know these)
- `ModAttachments` --references--> `ReturnPortalData`  [EXTRACTED]
  src/main/java/com/skd/utilitynexusfixes/common/attachment/ModAttachments.java → src/main/java/com/skd/utilitynexusfixes/common/attachment/ReturnPortalData.java

## Import Cycles
- None detected.

## Communities (27 total, 3 thin omitted)

### Community 0 - "Configuration Management"
Cohesion: 0.25
Nodes (13): Entity, PortalForcer, ServerLevel, BlockPos, Level, Logger, Mixin, Operation (+5 more)

### Community 1 - "Logging Filter"
Cohesion: 0.12
Nodes (14): AbstractFilter, BooleanValue, ConfigValue, LogEvent, Marker, Message, ModConfigSpec, Result (+6 more)

### Community 2 - "Mod Initialization"
Cohesion: 0.22
Nodes (9): AttachmentType, DeferredRegister, FMLCommonSetupEvent, IEventBus, Mod, ModContainer, ModAttachments, Logger (+1 more)

### Community 3 - "Mixin for Custom Names"
Cohesion: 0.36
Nodes (7): MutableComponent, Provider, EntityCustomNameLenientMixin, Logger, Mixin, Operation, WrapOperation

### Community 4 - "Gradle Build Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

### Community 10 - "Flujo de trabajo — Utility Nexus Fixes (NeoForge)"
Cohesion: 0.14
Nodes (13): Buenas prácticas, Commits (Conventional Commits), Convenciones de nomenclatura, Específico del mod, Estructura del proyecto, Flujo de trabajo — Utility Nexus Fixes (NeoForge), Flujo por tarea, Idioma (+5 more)

### Community 11 - "Project Variables — Utility Nexus Fixes"
Cohesion: 0.25
Nodes (7): CurseForge, Game Versions (IDs), Mod, Project Variables — Utility Nexus Fixes, Rama, Variables para el script de subida (curseforge-upload.ps1), Versiones

### Community 12 - "Utility Nexus Fixes"
Cohesion: 0.25
Nodes (7): Building from Source, Features, Installation, License, Links, Requirements, Utility Nexus Fixes

### Community 13 - "Changelog"
Cohesion: 0.13
Nodes (14): [0.0.0-beta.1], [0.0.0-beta.2], [0.0.0-beta.3], [1.0.0] - 2026-09-09, [1.1.0] - 2026-09-14, [1.1.1] - 2026-09-15, Added, Added (+6 more)

### Community 14 - "CLAUDE.md — utility_nexus_fixes (1.21.1)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — utility_nexus_fixes (1.21.1), Prioridad de instrucciones, Workflow del mod

### Community 18 - "PortalKey"
Cohesion: 0.38
Nodes (6): BlockPos, Level, Override, ResourceKey, PortalKey, ReturnPortalData

### Community 19 - "ModAttachments.java"
Cohesion: 0.18
Nodes (12): Gson, ResourceLocation, ResourceManager, ResourceManagerReloadListener, BvaDialogueTranslations, Logger, BvaDialogueMixin, Mixin (+4 more)

### Community 21 - "UNFConfig"
Cohesion: 0.29
Nodes (5): ClassNode, IMixinConfigPlugin, IMixinInfo, BvaMixinPlugin, Override

### Community 23 - "BvaClientSetup.java"
Cohesion: 0.43
Nodes (4): EventBusSubscriber, RegisterClientReloadListenersEvent, BvaClientSetup, SubscribeEvent

### Community 24 - "BvaConversationMixin.java"
Cohesion: 0.48
Nodes (5): BvaConversationMixin, Mixin, Operation, Pseudo, WrapOperation

## Knowledge Gaps
- **35 isolated node(s):** `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones`, `Fixed`, `Added` (+30 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `ReturnPortalData` connect `PortalKey` to `Configuration Management`, `Mod Initialization`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones` to the rest of the system?**
  _35 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Logging Filter` be split into smaller, more focused modules?**
  _Cohesion score 0.12318840579710146 - nodes in this community are weakly interconnected._
- **Should `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._
- **Should `Changelog` be split into smaller, more focused modules?**
  _Cohesion score 0.13333333333333333 - nodes in this community are weakly interconnected._