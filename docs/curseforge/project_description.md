<h1 align="center">&#129657; Utility Nexus Fixes</h1>

<p align="center"><strong>A small patch mod that stops known third-party crashes and quiets the harmless log spam left behind when a modpack's mod list changes.</strong></p>

<p align="center">
<img src="https://img.shields.io/curseforge/dt/1678000?style=plastic&logo=curseforge&label=downloads" alt="CurseForge Downloads">
<img src="https://img.shields.io/curseforge/v/1678000?style=plastic&logo=curseforge&label=latest" alt="CurseForge Version">
<img src="https://img.shields.io/badge/Minecraft-1.21.1-brightgreen?style=plastic" alt="Minecraft 1.21.1">
<img src="https://img.shields.io/badge/loader-NeoForge-orange?style=plastic&logo=curseforge" alt="NeoForge">
</p>

<br>

---

<br>

<h2>&#10024; Overview</h2>

<table>
<tr>
<td width="65%">
<p>Utility Nexus Fixes collects targeted compatibility and log-noise patches so packs don't have to hunt for a matching set of mod versions, or live with a console full of errors that don't matter. Every patch is a <strong>no-op when the thing it targets isn't there</strong> &mdash; nothing is bundled and nothing is a hard dependency. Built for NeoForge 21.1.249 on Minecraft 1.21.1.</p>

<p>What it covers:</p>
<ul>
<li><strong>Iris / Sodium</strong> &mdash; fixes the <code>SodiumGameOptions$PerformanceSettings</code> startup crash from the Iris 1.8.12 &times; Sodium 0.8.13+ class rename</li>
<li><strong>Broken entity names</strong> &mdash; a malformed <code>CustomName</code> no longer throws once per tick with a stack trace</li>
<li><strong>Stale-save log spam</strong> &mdash; a configurable filter drops known-harmless lines left over after a mod-list change</li>
<li><strong>Nether return portals</strong> &mdash; the return trip sends you back through the portal you actually came from, not whichever one happens to be closest</li>
</ul>
</td>
<td width="35%" align="center">
<a href="https://codex.skdragons.com/" target="_blank"><img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="160"></a>
</td>
</tr>
</table>

<br>

<h2>&#127919; Features</h2>

<h3>&#127765; Iris / Sodium compat patch</h3>
<p>Fixes a startup crash (<code>MixinTransformerError</code> / <code>ClassNotFoundException: SodiumGameOptions$PerformanceSettings</code>) that occurs when Iris 1.8.12's bundled Sodium-compat mixins target a class name (<code>SodiumGameOptions</code>) that Sodium renamed to <code>SodiumOptions</code> in 0.8.13+.</p>
<ul>
<li>Both mods are treated as optional dependencies &mdash; the patch only activates when both are present</li>
<li>Client-side only; nothing changes on a server or when Iris/Sodium are absent</li>
</ul>

<h3>&#127991;&#65039; Lenient entity CustomName parsing</h3>
<p>Stops the endless <code>Failed to parse entity custom name</code> warning &mdash; with a full stack trace, once per tick &mdash; that a datapack <code>tick</code> function can trigger when it writes a malformed <code>CustomName</code> to an entity (for example an unquoted string instead of a JSON text component).</p>
<ul>
<li>The bad value falls back to plain text instead of throwing, so the entity still gets a readable name</li>
<li>Implemented as a targeted mixin on the vanilla load path; valid names are untouched</li>
</ul>

<h3>&#128263; Configurable benign-log filter</h3>
<p>Drops a small, config-controlled allow-list of harmless log lines produced by stale save data after a mod-list change. Everything not on the list is left completely alone, and the filter never touches this mod's own logging.</p>
<ul>
<li>Default patterns: <code>Tried to load invalid item: 'Item must not be minecraft:air'</code>, unknown-item and unknown-fluid registry keys, and <code>Ignoring unknown attribute 'forge:&hellip;'</code></li>
<li>Fully editable &mdash; add or remove substrings, or switch the whole filter off</li>
</ul>
<blockquote><strong>Note</strong>: the <code>forge:</code> attribute lines come from old entity data using Forge-era attribute ids that NeoForge renamed. The filter only silences the warning; the stale modifier values themselves (from removed mods) are dropped by the game as before.</blockquote>

<h3>&#128293; Nether return-portal fix</h3>
<p>Vanilla sends a returning traveller to the <em>closest</em> existing portal at the scaled destination coordinates. When two portals sit near the same coordinates in the other dimension, the return trip can drop you at the wrong one. This fix remembers, per entity, which exit portal was paired with the portal you entered, and reuses it on the way back.</p>
<ul>
<li>Applies to Overworld&#8596;Nether travel in both directions; other portal types are untouched</li>
<li>Falls back to vanilla search when the remembered portal no longer exists</li>
<li>The mapping is kept in memory only &mdash; it resets on a server or world reload</li>
</ul>

<br>

<h2>&#9881;&#65039; Configuration Reference</h2>

<p>File: <code>config/utility_nexus/fixes/config.toml</code></p>

<table>
<tr><th>Key</th><th>Default</th><th>Notes</th></tr>
<tr><td colspan="3"><strong>[logfilter]</strong></td></tr>
<tr><td><code>logfilter.enabled</code></td><td>true</td><td>Master switch for the benign-log filter.</td></tr>
<tr><td><code>logfilter.patterns</code></td><td><em>(4 built-in lines)</em></td><td>List of case-sensitive substrings; any log message containing one is dropped. Edit freely.</td></tr>
<tr><td colspan="3"><strong>[fixes]</strong></td></tr>
<tr><td><code>fixes.enableNetherReturnPortalFix</code></td><td>true</td><td>Remember and reuse the portal you came from on Nether return trips. Set to <code>false</code> for vanilla routing.</td></tr>
</table>

<br>

<h2>&#128203; Requirements</h2>

<table>
<tr><td><strong>Minecraft</strong></td><td>1.21.1</td></tr>
<tr><td><strong>NeoForge</strong></td><td>21.1.249+</td></tr>
<tr><td><strong>Java</strong></td><td>21</td></tr>
</table>

<br>

<h2>&#127918; How to Use</h2>

<ol>
<li>Install NeoForge 21.1.249 for Minecraft 1.21.1.</li>
<li>Place Utility Nexus Fixes in your <code>mods</code> folder.</li>
<li>No setup required &mdash; every patch applies automatically when its target is detected.</li>
<li>To keep or drop different log lines, or to toggle individual fixes, edit <code>config/utility_nexus/fixes/config.toml</code> (<code>[logfilter]</code> and <code>[fixes]</code>).</li>
</ol>

<br>

---

<br>

<h2>&#128591; Credits</h2>

<p>Developed by <strong>Stalking Dragons</strong>.</p>

<br>
<br>

<p align="center">
  <a href="https://codex.skdragons.com/" target="_blank">
    <img src="https://node-files.skdragons.com/uploads/MINECRAFT/Codex/logo_codex_stalking_dragons.png" alt="Codex Stalking Dragons" width="200">
  </a>
  <br>
  <a href="https://codex.skdragons.com/">https://codex.skdragons.com/</a>
  <br>
  <em>Codex Stalking Dragons — Minecraft Modding</em>
</p>
