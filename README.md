# Auto Tool for Minecraft 1.21.4

A client-side Fabric mod that automatically selects the best tool in your hotbar when you begin or continue mining a block.

## Features

- Selects the best hotbar item before Minecraft processes the mining action.
- Uses vanilla's complete block-breaking speed calculation, including enchantments, effects, water/airborne penalties, and item suitability.
- Prioritizes a tool that can harvest the correct drops, then breaking speed.
- Considers pickaxes, shovels, axes, hoes, shears, swords, special tools, and empty hand automatically through vanilla item rules.
- Keeps the currently selected slot on ties to prevent visual flicker.
- Works while the attack/break key is held continuously, including when that key is rebound.
- Toggle in-game by holding **X** and pressing **T**.
- Enabled by default.
- Client-side only; no server installation is required.

## Scope

The selector searches the nine hotbar slots. Restricting it to the hotbar makes the switch immediate and server-recognized without inventory-click packet delays.

## Requirements

- Minecraft Java Edition 1.21.4
- Java 21
- Fabric Loader 0.16.10 or newer compatible release

Fabric API is not required.

## Build

From the project directory, with Gradle installed:

```bash
gradle build
```

The remapped mod jar will be written to `build/libs/autotool-1.0.0.jar`.


## Controls

- **X + T**: toggle Auto Tool on or off.
- The normal **T** chat key still works unless X is held at the same time.

## Notes

- Tool choice is evaluated at the beginning of mining and while block-breaking progress updates.
- The mod does not simulate mouse clicks, so it follows the user's configured attack/break key naturally.
- The toggle state resets to enabled each time the game starts.
