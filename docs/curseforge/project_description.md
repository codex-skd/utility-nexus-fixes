<h1 align="center">🩹 Utility Nexus Fixes</h1>

<p align="center"><strong>A compatibility-patch mod that fixes crashes and broken interactions caused by third-party mod version mismatches — no manual version pinning required.</strong></p>

<br>

---

<br>

<h2>✨ Overview</h2>

<p>Utility Nexus Fixes centralizes compatibility patches for known crash-causing interactions between popular mods, so packs don't have to hunt for a matching combination of versions. Built for NeoForge 21.1.249 on Minecraft 1.21.1.</p>

<br>

<h2>🎯 Features</h2>

<h3>🌗 Iris / Sodium compat patch</h3>
<p>Fixes a startup crash (<code>MixinTransformerError</code> / <code>ClassNotFoundException: SodiumGameOptions$PerformanceSettings</code>) that occurs when Iris 1.8.12's bundled Sodium-compat mixins target a class name (<code>SodiumGameOptions</code>) that Sodium renamed to <code>SodiumOptions</code> in 0.8.13+. Both mods are treated as optional dependencies — the patch only activates when both are present.</p>

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
<li>Download the latest Utility Nexus Fixes release and place it in your <code>mods</code> folder alongside Iris and Sodium.</li>
<li>No configuration needed — patches apply automatically when the affected mods are detected.</li>
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
