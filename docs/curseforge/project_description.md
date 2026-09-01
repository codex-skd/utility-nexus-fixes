<h1 align="center">🩹 Utility Nexus Fixes</h1>

<p align="center"><strong>A small patch mod that stops known third-party crashes and quiets the log spam left behind when a modpack's mod list changes.</strong></p>

<br>

---

<br>

<h2>✨ Overview</h2>

<p>Utility Nexus Fixes collects targeted compatibility and log-noise patches so packs don't have to hunt for a matching set of mod versions or live with a console full of harmless errors. Every patch is a no-op when the thing it targets isn't there — nothing is bundled and nothing is a hard dependency. Built for NeoForge 21.1.249 on Minecraft 1.21.1.</p>

<br>

<h2>🎯 Features</h2>

<h3>🌗 Iris / Sodium compat patch</h3>
<p>Fixes a startup crash (<code>MixinTransformerError</code> / <code>ClassNotFoundException: SodiumGameOptions$PerformanceSettings</code>) that occurs when Iris 1.8.12's bundled Sodium-compat mixins target a class name (<code>SodiumGameOptions</code>) that Sodium renamed to <code>SodiumOptions</code> in 0.8.13+. Only activates when both mods are present.</p>

<h3>🏷️ Lenient entity CustomName parsing</h3>
<p>Stops the endless <code>Failed to parse entity custom name</code> warning (with a full stack trace, once per tick) that a datapack <code>tick</code> function can trigger when it writes a malformed <code>CustomName</code> to an entity. The bad value now falls back to plain text instead of throwing.</p>

<h3>🔇 Configurable benign-log filter</h3>
<p>Drops a small, config-controlled allow-list of harmless log lines left over from stale save data after a mod-list change — by default <code>Tried to load invalid item: 'Item must not be minecraft:air'</code>, unknown-item / unknown-fluid registry keys, and <code>Ignoring unknown attribute 'forge:…'</code>. Everything else is untouched. Settings live in <code>config/utility_nexus/fixes/config.toml</code> under <code>[logfilter]</code> (<code>enabled</code>, <code>patterns</code>); the filter never touches this mod's own logging.</p>

<br>

<h2>📋 Requirements</h2>

<table>
<tr><td><strong>Minecraft</strong></td><td>1.21.1</td></tr>
<tr><td><strong>NeoForge</strong></td><td>21.1.249+</td></tr>
<tr><td><strong>Java</strong></td><td>21</td></tr>
</table>

<br>

<h2>🎮 How to Use</h2>

<ol>
<li>Install NeoForge 21.1.249 for Minecraft 1.21.1.</li>
<li>Place Utility Nexus Fixes in your <code>mods</code> folder.</li>
<li>No setup required — every patch applies automatically when its target is detected. Adjust the log filter in <code>config/utility_nexus/fixes/config.toml</code> if you want to keep or drop different lines.</li>
</ol>

<br>

---

<br>

<h2>🙏 Credits</h2>

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
