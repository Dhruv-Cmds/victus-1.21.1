# Victus

[![curseforge](https://img.shields.io/badge/-CurseForge-gray?style=for-the-badge&logo=curseforge&labelColor=orange)](https://www.curseforge.com/minecraft/mc-mods/victus)
[![modrinth](https://img.shields.io/badge/-modrinth-gray?style=for-the-badge&labelColor=green&labelWidth=15&logo=appveyor&logoColor=white)](https://modrinth.com/mod/victus)
[![release](https://img.shields.io/github/v/release/glisco03/victus?logo=github&style=for-the-badge)](https://github.com/glisco03/victus/releases)
[![discord](https://img.shields.io/discord/825828008644313089?label=wisp%20forest&logo=discord&logoColor=white&style=for-the-badge)](https://discord.gg/xrwHKktV2d)

## Overview

Victus is a mod about Hearts and how to use them in interesting ways. The core of the mod is about crafting custom hearts, the so-called Heart Aspects, which you can then apply on top of your health bar. When these hearts get broken (by taking damage) a certain effect is triggered, depending on the Aspect. These effects range from "feeding" you a steak, to spawning Iron Golems and Vexes, or even outright preventing your death.

## Credits
 - **Pois1x** for providing the textures
 - **pvpTurtle** for providing some code as well as ideas 
 - **naschhorn** for assisting with gameplay design and balancing

## Building (1.21.1 port)

This is an **unverified source port** of Victus 1.20.2 to Minecraft 1.21.1 (Fabric). It has not been compiled yet.

**Option A – GitHub Actions (no local setup):** push this folder to a GitHub repo. The workflow in
`.github/workflows/build.yml` builds it; download the jar from the run's *Artifacts*.

**Option B – locally:** install JDK 21, then run `./gradlew build` (Windows: `gradlew.bat build`).
The jar is written to `build/libs/victus-0.1.15+1.21.1.jar` (ignore the `-sources` jar).

Requires at runtime: Fabric Loader 0.16+, Fabric API, owo-lib, Cardinal Components (entity) 6.x.
Lavender is bundled inside the jar.

If a dependency version in `gradle.properties` is not found, check the Maven pages linked in that file
and pick the closest 1.21 / 1.21.1 release.
