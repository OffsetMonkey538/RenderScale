# RenderScale

RenderScale allows you to change Minecraft's render resolution **separately** from the HUD elements.

This is a fork of [ResolutionControl++](https://github.com/ModLabsCC/Resolution-Control) for 1.21.4, with added support for NeoForge.

![Comparison of FPS for each common render scale](comparison2.webp)

Check out [Fabrishot](https://modrinth.com/mod/fabrishot) if you also want the large screenshot feature that was in ResolutionControl.

---

# How

Use the mod config to control the render scale multiplier. You can choose to increase for better antialiasing, or decrease for improved performance. This is heavily recommended for laptops with retina displays!

Additionally, you can set the scaling algorithm used.
Linear is good for antialiasing & scaling higher than native, while nearest neighbor keeps it pixelated.

If you're using shaders, it may be a good idea to stick to nearest as to not conflict with the shaders' own antialiasing, which could make things blurry.

There are no plans to support DLSS, FSR 2.0+, etc. I'm looking into potential FSR 1.0 support, but I'm not sure yet.

---

# Compatibility

RenderScale **does not work with Fabulous Graphics**!

**Sodium** - Compatible

**Canvas Renderer** - No idea

**OptiFine** - No idea, but OptiFine has this as "Render Quality" in the Shaders tab. 
