# Graph Report - 1.21.1  (2026-09-02)

## Corpus Check
- 18 files · ~47,456 words
- Verdict: corpus is large enough that graph structure adds value.

## Summary
- 98 nodes · 106 edges · 18 communities (15 shown, 3 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `5314bc25`
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

## God Nodes (most connected - your core abstractions)
1. `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` - 13 edges
2. `UNFConfig` - 8 edges
3. `Utility Nexus Fixes` - 7 edges
4. `Project Variables — Utility Nexus Fixes` - 7 edges
5. `UtilityNexusFixes` - 5 edges
6. `BenignLogFilter` - 5 edges
7. `EntityCustomNameLenientMixin` - 4 edges
8. `CLAUDE.md — utility_nexus_fixes (1.21.1)` - 3 edges
9. `Changelog` - 3 edges
10. `AttributeMapLegacyRemapMixin` - 2 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Import Cycles
- None detected.

## Communities (18 total, 3 thin omitted)

### Community 0 - "Configuration Management"
Cohesion: 0.25
Nodes (5): BooleanValue, ConfigValue, ModConfigSpec, UNFConfig, SuppressWarnings

### Community 1 - "Logging Filter"
Cohesion: 0.23
Nodes (9): AbstractFilter, Level, LogEvent, Marker, Message, Override, Result, BenignLogFilter (+1 more)

### Community 2 - "Mod Initialization"
Cohesion: 0.36
Nodes (6): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, Logger, UtilityNexusFixes

### Community 3 - "Mixin for Custom Names"
Cohesion: 0.36
Nodes (7): Mixin, MutableComponent, Operation, Provider, EntityCustomNameLenientMixin, Logger, WrapOperation

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
Cohesion: 0.33
Nodes (5): [0.0.0-beta.1], [0.0.0-beta.2], Added, Changelog, Fixed

### Community 14 - "CLAUDE.md — utility_nexus_fixes (1.21.1)"
Cohesion: 0.50
Nodes (3): CLAUDE.md — utility_nexus_fixes (1.21.1), Prioridad de instrucciones, Workflow del mod

## Knowledge Gaps
- **30 isolated node(s):** `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones`, `Fixed`, `Added` (+25 more)
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `UNFConfig` connect `Configuration Management` to `Logging Filter`?**
  _High betweenness centrality (0.033) - this node is a cross-community bridge._
- **What connects `PerformanceSettings`, `Workflow del mod`, `Prioridad de instrucciones` to the rest of the system?**
  _30 weakly-connected nodes found - possible documentation gaps or missing edges._
- **Should `Flujo de trabajo — Utility Nexus Fixes (NeoForge)` be split into smaller, more focused modules?**
  _Cohesion score 0.14285714285714285 - nodes in this community are weakly interconnected._