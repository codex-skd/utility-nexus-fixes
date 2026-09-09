# Graph Report - 1.21.1  (2026-09-09)

## Corpus Check
- 23 files · ~49,136 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 142 nodes · 196 edges · 23 communities (20 shown, 3 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 3 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `2122546b`
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

## God Nodes (most connected - your core abstractions)
1. `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` - 13 edges
2. `PortalKey` - 10 edges
3. `ReturnPortalData` - 9 edges
4. `UNFConfig` - 9 edges
5. `MixinNetherReturnPortalFix` - 7 edges
6. `Utility Nexus Fixes` - 7 edges
7. `Project Variables — Utility Nexus Fixes` - 7 edges
8. `UtilityNexusFixes` - 5 edges
9. `ModAttachments` - 5 edges
10. `BenignLogFilter` - 5 edges

## Surprising Connections (you probably didn't know these)
- `ModAttachments` --references--> `ReturnPortalData`  [EXTRACTED]
  src/main/java/com/skd/utilitynexusfixes/common/attachment/ModAttachments.java → src/main/java/com/skd/utilitynexusfixes/common/attachment/ReturnPortalData.java

## Import Cycles
- None detected.

## Communities (23 total, 3 thin omitted)

### Community 0 - "Configuration Management"
Cohesion: 0.25
Nodes (13): Entity, PortalForcer, ServerLevel, BlockPos, Level, Logger, Mixin, Operation (+5 more)

### Community 1 - "Logging Filter"
Cohesion: 0.23
Nodes (9): AbstractFilter, LogEvent, Marker, Message, Result, BenignLogFilter, Level, Logger (+1 more)

### Community 2 - "Mod Initialization"
Cohesion: 0.36
Nodes (6): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, Logger, UtilityNexusFixes

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
Cohesion: 0.18
Nodes (10): [0.0.0-beta.1], [0.0.0-beta.2], [0.0.0-beta.3], [1.0.0] - 2026-09-09, Added, Added, Changelog, Fixed (+2 more)

### Community 14 - "CLAUDE.md — utility_nexus_fixes (1.21.1)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — utility_nexus_fixes (1.21.1), Prioridad de instrucciones, Workflow del mod

### Community 18 - "PortalKey"
Cohesion: 0.38
Nodes (6): BlockPos, Level, Override, ResourceKey, PortalKey, ReturnPortalData

### Community 19 - "ModAttachments.java"
Cohesion: 0.60
Nodes (3): AttachmentType, DeferredRegister, ModAttachments

### Community 21 - "UNFConfig"
Cohesion: 0.22
Nodes (5): BooleanValue, ConfigValue, ModConfigSpec, UNFConfig, SuppressWarnings

## Knowledge Gaps
- **33 isolated node(s):** `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones`, `Summary of the beta line`, `Notes` (+28 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `UNFConfig` connect `UNFConfig` to `Logging Filter`?**
  _High betweenness centrality (0.057) - this node is a cross-community bridge._
- **Why does `ReturnPortalData` connect `PortalKey` to `Configuration Management`, `ModAttachments.java`?**
  _High betweenness centrality (0.053) - this node is a cross-community bridge._
- **What connects `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones` to the rest of the system?**
  _33 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._