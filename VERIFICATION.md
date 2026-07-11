# Verification

The project was checked in the creation environment with:

- Java 21 source compilation against API stubs matching the referenced Minecraft/Fabric signatures.
- JSON parsing for `fabric.mod.json`, the mixin configuration, and the language file.
- Structural checks for the Minecraft 1.21.4 dependency, client-only environment, X+T chord, both mining hooks, and vanilla harvest/speed scoring.

A full Fabric Loom build was not run in the creation environment because Gradle and the Minecraft/Fabric dependency cache were unavailable. Run `gradle build` in an internet-connected development environment to produce the remapped release jar.
