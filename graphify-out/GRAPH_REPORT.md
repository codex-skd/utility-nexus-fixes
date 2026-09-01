# Graph Report - .  (2026-09-02)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 55 nodes · 71 edges · 10 communities (7 shown, 3 thin omitted)
- Extraction: 100% EXTRACTED · 0% INFERRED · 0% AMBIGUOUS
- Token cost: 366 input · 97 output

## Graph Freshness
- Built from commit: `ec815c1f`
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

## God Nodes (most connected - your core abstractions)
1. `UNFConfig` - 8 edges
2. `UtilityNexusFixes` - 5 edges
3. `BenignLogFilter` - 5 edges
4. `EntityCustomNameLenientMixin` - 4 edges
5. `AttributeMapLegacyRemapMixin` - 2 edges
6. `SodiumGameOptions` - 2 edges
7. `PerformanceSettings` - 1 edges
8. `Icon for Utility Nexus Fixes mod` - 0 edges

## Surprising Connections (you probably didn't know these)
- None detected - all connections are within the same source files.

## Import Cycles
- None detected.

## Communities (10 total, 3 thin omitted)

### Community 0 - "Configuration Management"
Cohesion: 0.20
Nodes (6): BooleanValue, ConfigValue, ModConfigSpec, Result, UNFConfig, SuppressWarnings

### Community 1 - "Logging Filter"
Cohesion: 0.29
Nodes (8): AbstractFilter, Level, LogEvent, Marker, Message, Override, BenignLogFilter, Logger

### Community 2 - "Mod Initialization"
Cohesion: 0.36
Nodes (6): FMLCommonSetupEvent, IEventBus, Mod, ModContainer, Logger, UtilityNexusFixes

### Community 3 - "Mixin for Custom Names"
Cohesion: 0.36
Nodes (7): Mixin, MutableComponent, Operation, Provider, EntityCustomNameLenientMixin, Logger, WrapOperation

### Community 4 - "Gradle Build Script"
Cohesion: 0.83
Nodes (3): gradlew script, die(), warn()

## Knowledge Gaps
- **2 isolated node(s):** `PerformanceSettings`, `Icon for Utility Nexus Fixes mod`
  These have ≤1 connection - possible missing edges or undocumented components.
- **3 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `BenignLogFilter` connect `Logging Filter` to `Configuration Management`?**
  _High betweenness centrality (0.039) - this node is a cross-community bridge._
- **What connects `PerformanceSettings`, `Icon for Utility Nexus Fixes mod` to the rest of the system?**
  _2 weakly-connected nodes found - possible documentation gaps or missing edges._