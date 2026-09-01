# Changelog

All notable changes to this project will be documented in this file.

## [0.0.0-beta.1]

### Added
- Initial project setup.
- Iris / Sodium compat patch: fixes the `ClassNotFoundException: SodiumGameOptions$PerformanceSettings` crash caused by Iris 1.8.12's Sodium-compat mixin referencing a class Sodium 0.8.13 renamed to `SodiumOptions`.
