# Vapor 4.21 MC26.2

## Release summary

This release converts the previous Vape 4.21 recovery workspace into a standalone Fabric client mod for Minecraft 26.2.

The project builds a standard Fabric mod JAR. It does not build a native DLL, injector executable, embedded Java payload, or native CMake project.

## Project Scope 

This project was created as a proof of concept to determine whether the recovered Vape 4.21 Java client could be converted into a standalone Fabric mod for Minecraft 26.2 without relying on native injection.

That objective has been completed. The client builds as a standard Fabric mod, loads successfully, and the core integration required to run the recovered client has been implemented.

Individual hacks and modules have received basic functional testing, but the recovered feature set has not been exhaustively tested, validated for complete behavioral accuracy, or compared feature-for-feature against upstream Vape 4.21. Reproducing every upstream behavior was not the purpose of this project.

The primary goal was to remove the native DLL/injector architecture and produce a conventional, non-injectable Fabric implementation. With that accomplished, development of this repository is considered complete.

No further updates, compatibility work, module fixes, or upstream parity work are planned. Any broken, incomplete, or inaccurate functionality is left for users who choose to use, modify, or fork the project.

## Added

### Fabric integration

- Added Fabric mod metadata in [`fabric.mod.json`](src/main/resources/fabric.mod.json).
- Added `VaporFabricClient` as the Fabric client entrypoint.
- Added Fabric mixin configuration in [`vapor421.mixins.json`](src/main/resources/vapor421.mixins.json).
- Added Minecraft 26.2 integration for:
  - Client startup and tick lifecycle
  - Keyboard and mouse input
  - Right Shift ClickGUI activation
  - Player movement, motion, and attack events
  - Packet send and receive events
  - World-change notifications
  - World and GUI rendering
  - OpenGL capability management

### Rendering and UI

- Added Fabric render hooks for the recovered OpenGL-based UI.
- Added deferred initialization until the first playable world tick.
- Added handling for Minecraft 26.2's deferred GUI renderer.
- Vapor HUD elements now render after native HUD mods such as Jade, with inherited clipping state cleared before drawing.
- Added OpenGL capability restoration for render callbacks.
- Added Fabric key binding registration for the Right Shift GUI shortcut.
- Added local frame-state persistence for overlay visibility and layout.

### Event bridge

- Reconnected the recovered event system to Fabric lifecycle callbacks.
- Added pre/post tick and render dispatch.
- Added player tick, motion, travel, attack, packet, and world-change event dispatch.
- Added cancellation support for outgoing and incoming client packets.
- Restricted packet interception to active client play connections, excluding status/login traffic.

## Changed

### Build system

The build was migrated to Fabric Loom.

- Fabric Loom manages Minecraft dependencies and mappings.
- Project name: `vapor421-fabric`.
- Maven group: `gg.vapor`.
- Version: `4.21.0`.
- Minecraft target: 26.2.
- Fabric Loader requirement: 0.19.3 or newer.
- Fabric API requirement: `0.156.0+26.2` or newer.
- Java 25 is required.
- `jar` produces the standard Fabric JAR; `build` also produces the sources JAR in `build/libs/`.

Expected artifacts:

```text
build/libs/vapor421-fabric-4.21.0.jar
build/libs/vapor421-fabric-4.21.0-sources.jar
```

### Runtime identity

- User-facing product metadata, logging, and documentation use the Vapor identity.
- The recovered Java package namespace remains `gg.vape` for source compatibility.

### Initialization and persistence

- Initialization is scheduled after Minecraft enters a playable world.
- Item and hotbar-dependent setup is delayed until Minecraft registries and data reload are complete.
- The legacy frame tree is not constructed before the client is ready.
- Saved frame state is deferred until all Fabric UI frames exist, so overlay visibility and positions are restored correctly.
- Startup no longer overwrites saved frame state with an empty layout.

## Removed

### Native injection build inputs and obsolete UI

The release tree no longer contains:

- Native source directory or CMake project
- Native DLL/injector build tasks
- `ForgePayloadClassLoader`
- `NativeEventBridge`
- `NativePresenceUpdater`
- Reconstruction metadata referencing a sample DLL
- The Rearview/off-screen camera renderer and UI
- The Session Spoof/offline-account login screen and its disconnect trigger

The release JAR contains no `.dll`, `.so`, or `.dylib` files.

### Unsupported release targets

This release supports Fabric on Minecraft 26.2 only. It does not support Forge, legacy Minecraft versions, launcher injection, or direct DLL-based process injection.

## Compatibility

Supported target:

- Minecraft 26.2
- Fabric Loader 0.19.3+
- Fabric API 0.156.0+26.2+
- Java 25
- Client-side OpenGL rendering

Known limitations:

- Vulkan rendering is not supported.
- This release is Fabric-only.
- Native DLL/injector workflows are not included.
- Online and recovery services are not a supported part of the Fabric release.

## Legacy compatibility note

The recovered Java codebase still contains compatibility-oriented classes such as `NativeBridge` and `ClassTransformer`, plus historical mappings for older environments. They are Java source compatibility remnants; they do not add native binaries or an injector workflow to the published Fabric artifact.

## Build

Windows:

```powershell
.\gradlew.bat build
```

Linux or macOS:

```sh
./gradlew build
```

## License

This repository is provided under [CC0 1.0 Universal](LICENSE). Third-party libraries, trademarks, fonts, textures, and other existing materials remain subject to their respective rights.

## Legal

This is a Fabric conversion of a recovered client implementation, not the original official Vape source