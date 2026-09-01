# Flujo de trabajo — Utility Nexus Fixes (NeoForge)

> **Versión del workflow**: 1.0.0
> Este archivo pertenece al proyecto **Utility Nexus Fixes**. Cambios aquí solo afectan a este proyecto.
> **Trabaja directamente con este archivo**: es el workflow operativo del mod, autocontenido. No leas `codex-docs/WORKFLOW_AGENT.md` ni `WORKFLOW_GENERIC.md` de forma rutinaria.
> On-demand (solo si la tarea lo necesita): `codex-docs/reference/CURSEFORGE.md` (formato HTML al publicar), `codex-docs/reference/GRAPHIFY.md` (backend LLM de Graphify), `codex-docs/reference/REPO_SETUP.md` (setup único de repo).

## Específico del mod

| Campo | Valor |
|---|---|
| **Mod ID** | `utility_nexus_fixes` |
| **Clase principal** | `com.skd.utilitynexusfixes.UtilityNexusFixes` |
| **Display name** | `Utility Nexus Fixes` |
| **Versión Minecraft** | `1.21.1` |
| **Versión NeoForge** | `21.1.249` |
| **Rama de trabajo** | `minecraft/1.21.1/neoforge-21.1.249/production` |
| **Repositorio GitLab** | `https://gitlab.com/stalking-dragons/minecraft/utility-nexus-fixes.git` |
| **Paquete base** | `com.skd.utilitynexusfixes` |

## Qué es este mod

Mod de **parches de compatibilidad** entre mods de terceros que crashean o se comportan mal por desajustes de versión — nunca modifica los jars de los mods afectados, todos son dependencias `optional` (el parche solo se activa si el mod afectado está presente). No es un fork de ningún mod existente.

## Convenciones de nomenclatura

| Convención | Uso | Ejemplo |
|---|---|---|
| **snake_case** | `mod_id` en gradle.properties, assets/, packages Java | `utility_nexus_fixes` |
| **PascalCase** | Clases Java principales | `UtilityNexusFixes` |
| **camelCase** | Variables, métodos, config keys | `utilityNexusFixes` |
| **Title Case** | Display name en README, CHANGELOG, docs, CurseForge | `Utility Nexus Fixes` |

Reglas:
- `mod_id` en `gradle.properties` coincide con el nombre del directorio del proyecto
- Display name en `README.md`/`CHANGELOG.md` en **Title Case**
- Clases Java principales en **PascalCase** del `mod_id` (`utility_nexus_fixes` → `UtilityNexusFixes`)
- Cada parche de compatibilidad vive en su propio sub-paquete: `com.skd.utilitynexusfixes.compat.<mod_afectado>`

## Organización y ramas

| Rama | Propósito |
|---|---|
| `minecraft/1.21.1/neoforge-21.1.249/production` | **Rama única de trabajo** (default). Trabajo diario: código, docs/, lib_ext/, graphify-out/, tokens reales |
| `minecraft/1.21.1/neoforge-21.1.249/main` | Rama espejo pública (CI deriva desde production, sanitiza secrets). No tocar a mano. |

La rama ya incluye el framework (`minecraft/1.21.1/neoforge-21.1.249/production`); la carpeta local añade el nivel de framework para que convivan builds de distintos loaders para el mismo MC.
Localmente cada `<framework>/<mc-version>/` es un **clon independiente** con su propio `.git/`, en la rama `production` de su versión.

## Estructura del proyecto

```
utility_nexus_fixes/neoforge/1.21.1/          # Raíz real del proyecto
├── build.gradle                        # Build con net.neoforged.moddev
├── gradle.properties                   # mod_id, mod_version, mod_group_id, mod_framework...
├── settings.gradle
├── src/main/java/com/skd/utilitynexusfixes/  # Código fuente del mod
│   └── compat/<mod_afectado>/          # Un paquete por parche de compatibilidad
├── src/main/resources/
│   ├── assets/utility_nexus_fixes/     # Texturas, icon.png (64x64), lang, modelos
│   ├── templates/META-INF/neoforge.mods.toml   # Template con placeholders ${...}
│   ├── META-INF/accesstransformer.cfg
│   └── META-INF/neoforge.mods.toml     # Generado desde template
├── libs/                               # Deps reales (JARs de compilación: Iris, Sodium...). Versionado.
├── lib_ext/                            # Librerías de análisis de sesión. NO versionado.
├── temp/                               # Archivos temporales (investigación, prototipos). NO versionado.
├── docs/
│   ├── WORKFLOW_UTILITY_NEXUS_FIXES_1-21-1.md
│   └── curseforge/                     # project_vars.md · project_description.md · versions/<version>.md
├── CHANGELOG.md · README.md
├── graphify-out/                       # GRAPH_REPORT.md · graph.html · graph.json (versionado)
└── .gitlab-ci.yml                      # CI/CD (setup único — ver codex-docs/reference/REPO_SETUP.md)
```

## Versionado

| Estado | Formato | Ejemplos |
|---|---|---|
| Beta / desarrollo | `0.0.0-beta.X` | `0.0.0-beta.1`, `0.0.0-beta.2` |
| Release estable | `X.Y.Z` (SemVer) | `1.0.0`, `1.2.3`, `2.0.0` |

SemVer: `MAJOR` breaking · `MINOR` funcionalidad compatible · `PATCH` bug fix compatible.

- Incrementar en cada commit funcional (no solo docs) y al preparar una subida a CurseForge
- Se define en `gradle.properties`: `mod_version=0.0.0-beta.1`
- JAR: `<mod_id>-<minecraft_version>-neoforge-<neo_version>-<mod_version>.jar` (`base.archivesName` en build.gradle)
  - Ejemplo: `utility_nexus_fixes-1.21.1-neoforge-21.1.249-0.0.0-beta.1.jar`
- `build.gradle`: `base { archivesName = "${mod_id}-${minecraft_version}-neoforge-${neo_version}" }`

## Commits (Conventional Commits)

`<tipo>[<ámbito>]: <descripción>` + body opcional. Tipos: `feat` · `fix` · `refactor` · `docs` · `chore` · `style` · `perf` · `test`. El mensaje **debe incluir la versión** (`v<version>`):

```
git commit -m "feat[iris-sodium]: patch SodiumGameOptions class rename crash

v0.0.0-beta.1"
```

## Tags (GitLab)

Cada subida a CurseForge crea un tag. Formato: beta `<mc-version>-<framework>-beta.X` · release `<mc-version>-<framework>-X.Y.Z`.

```bash
git tag -a 1.21.1-neoforge-beta.1 -m "v0.0.0-beta.1: Initial project setup"
git push origin 1.21.1-neoforge-beta.1
```

## Flujo por tarea

**0. Alcance de versión/framework** — Este mod solo tiene una versión (1.21.1) y un framework (NeoForge). No requiere selector.

**1. Desarrollo**

```bash
git checkout minecraft/1.21.1/neoforge-21.1.249/production
./gradlew.bat build
git add -A
git commit -m "feat[iris-sodium]: patch SodiumGameOptions class rename crash

v0.0.0-beta.1"
git push
```

**2. Preparar versión para CurseForge** — solo si el usuario confirma:

```bash
# bump en gradle.properties: mod_version=0.0.0-beta.2
./gradlew.bat clean build
# release notes: docs/curseforge/versions/0.0.0-beta.2.md + actualizar CHANGELOG.md
git commit -m "chore: bump version to 0.0.0-beta.2"
git tag -a 1.21.1-neoforge-beta.2 -m "v0.0.0-beta.2: <resumen>"
git push origin 1.21.1-neoforge-beta.2
# Subir JAR (build/libs/utility_nexus_fixes-1.21.1-neoforge-21.1.249-0.0.0-beta.2.jar) solo si el usuario confirma:
# powershell -File ../../../codex-docs/scripts/curseforge-upload.ps1
# (desde utility_nexus_fixes/neoforge/1.21.1/; lee project_vars.md + gradle.properties)
```

**3. Release estable** — `mod_version=1.0.0` + commit + tag `1.21.1-neoforge-1.0.0`.

**4. Actualizar Knowledge Graph (Graphify)** — tras cada push a remoto. Versión instalada 0.9.12: **`build` no es un comando válido** — usar `extract` o `update`:

```bash
GRAPHIFY="C:\Users\llagu\AppData\Local\Packages\PythonSoftwareFoundation.Python.3.13_qbz5n2kfra8p0\LocalCache\local-packages\Python313\Scripts\graphify.exe"

"$GRAPHIFY" extract .              # 1ª vez (si graphify-out/ NO existe): extracción completa con LLM
"$GRAPHIFY" update . --force       # actualización tras cambios de código: más barato, sin LLM
git add graphify-out/
git commit -m "chore: update knowledge graph"
git push
```

- **Nunca crear copias fechadas** (`graphify-out/YYYY-MM-DD/`): el historial vive en git (`git log -- graphify-out/`). El `.gitignore` (ver `templates/.gitignore`) las excluye como red de seguridad.
- **Qué leer**: `GRAPH_REPORT.md` (resumen legible: nodos, comunidades, hubs). **Nunca** `graph.json`/`graph.html` como contexto (el `graph.json` pesa >1MB y anula el ahorro de tokens). El grafo reduce el consumo de tokens hasta 71× al entender la arquitectura sin leer todo el código.
- Backend LLM (Ollama local `qwen2.5-coder:7b`) para `extract`/`label`: `codex-docs/reference/GRAPHIFY.md`.

## Buenas prácticas

- **Un commit por cambio lógico** · commit+push tras cada cambio funcional y tras cualquier cambio de documentación
- **`clean build` siempre antes del JAR final** (la caché de Gradle puede dejar artefactos obsoletos)
- **Versionar antes de subir a CurseForge** (tag → commit exacto del JAR) · **CHANGELOG.md siempre actualizado**
- **Graphify**: mantener actualizado tras cada release · leer `GRAPH_REPORT.md`, nunca `graph.json`/`graph.html` · sin copias fechadas
- **Nomenclatura consistente**: no mezclar casos de los 4 estilos en contextos que no corresponden
- **Sin archivos basura**: eliminar `nul`, `TEMPLATE_LICENSE.txt`, `*_errors.txt` antes de commitear
- **README.md en inglés** y siempre actualizado (descripción, requisitos, instalación, enlaces)
- **Cada parche de compatibilidad se activa solo si el mod afectado está presente** (dependencia `optional` en `neoforge.mods.toml` + comprobación en runtime vía `ModList.get().isLoaded(...)` antes de aplicar cualquier transformación de bytecode)
- **Nunca tocar los JARs de Iris/Sodium ni otros mods afectados** — el parche vive enteramente en el classloading/mixin de este mod

## Idioma

| Ámbito | Idioma |
|---|---|
| Código fuente, logs, nombres técnicos, commits | **Inglés** (en-US) |
| README.md | **Inglés** (en-US) |
| Documentación interna (docs/, CHANGELOG, WORKFLOW) | **Castellano** (es-ES) |
| CurseForge (descripción, release notes) | **Inglés** (en-US) |

---

## Próximas funcionalidades planificadas

1. **Iris / Sodium compat patch** — Fix del crash `MixinTransformerError` por renombrado de `SodiumGameOptions` → `SodiumOptions` (en desarrollo, ver `docs/curseforge/project_description.md`)
2. Futuros parches de compatibilidad entre otros mods del modpack, según se detecten
