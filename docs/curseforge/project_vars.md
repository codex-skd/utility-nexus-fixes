# Project Variables — Utility Nexus Fixes

<!--
  Este archivo contiene las variables específicas del proyecto para CurseForge.
  Se usa como referencia al subir versiones. No contiene secrets directamente,
  solo referencias a los tokens que están en el gestor de contraseñas.
-->

## CurseForge

| Variable | Valor |
|----------|-------|
| Project ID | `1678000` |
| Upload Token | `ee776b0a-ee95-4850-b554-06be02a8657f` |
| Core API GET | `$2a$10$yGwryAfmRkS9ZJsJUDf5YOKZpOIsmHB8Fji2D8JVCKBSZEKYlwmaO` |

## Game Versions (IDs)

| Recurso | ID | gameVersionTypeId |
|---------|----|----|
| Minecraft 1.21.1 | `11779` | 86297 |
| Client | `9638` | 75208 |
| Server | `9639` | 75208 |
| NeoForge | `10150` | 68441 |

## Versiones

| Recurso | Versión |
|---------|---------|
| Minecraft | `1.21.1` |
| NeoForge | `21.1.249` |
| Java | `21` |
| Mod loader | `neoforge` |

## Mod

| Variable | Valor |
|----------|-------|
| mod_id | `utility_nexus_fixes` |
| display_name | `Utility Nexus Fixes` |
| mod_group_id | `com.skd.utilitynexusfixes` |
| package_path | `com/skd/utilitynexusfixes` |

## Rama

| Variable | Valor |
|----------|-------|
| branch | `minecraft/1.21.1/neoforge-21.1.249/production` |
| tag_prefix | `1.21.1-neoforge-` |

## Variables para el script de subida (curseforge-upload.ps1)

<!-- Formato clave = valor requerido por Get-VarFromMd en codex-docs/scripts/curseforge-upload.ps1 -->

project_id = 1678000
api_token = ee776b0a-ee95-4850-b554-06be02a8657f
game_versions = 11779,9638,9639,10150
release_type = release
