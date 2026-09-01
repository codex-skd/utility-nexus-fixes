# Utility Nexus Fixes

A compatibility-patch mod for Minecraft NeoForge, fixing crashes and broken interactions between third-party mods that would otherwise require manually pinning mismatched versions.

## Features

- **Iris / Sodium compat patch**: fixes a `ClassNotFoundException` / `MixinTransformerError` crash caused by Iris 1.8.12's Sodium compat mixins referencing a class (`SodiumGameOptions$PerformanceSettings`) that newer Sodium builds (0.8.13+) renamed to `SodiumOptions$PerformanceSettings`.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.249
- Java 21

## Installation

1. Install NeoForge 21.1.249 for Minecraft 1.21.1
2. Download the latest release from [CurseForge](https://www.curseforge.com/minecraft/mc-mods/utility-nexus-fixes) or [GitLab](https://gitlab.com/stalking-dragons/minecraft/utility-nexus-fixes/-/releases)
3. Place the JAR file in your `mods` folder
4. Launch Minecraft with the NeoForge profile

## Building from Source

```bash
./gradlew build
```

The built JAR will be in `build/libs/`.

## Links

- [GitLab Repository](https://gitlab.com/stalking-dragons/minecraft/utility-nexus-fixes)
- [Issues](https://gitlab.com/stalking-dragons/minecraft/utility-nexus-fixes/-/issues)
- [Releases](https://gitlab.com/stalking-dragons/minecraft/utility-nexus-fixes/-/releases)

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
