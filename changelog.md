# RELEASE 1.2.0
The performance update (to be less "redundant"... or what do you think LunaPixel?)

- [🐛] Disabled ``ExtendedServerViewDistanceMixin``
  - 🛠️ Some reports culprit it causes a GPU MemoryLeak. I suggest using other alternatives like [Farsight](https://legacy.curseforge.com/minecraft/mc-mods/farsight)
- [🔥] Redistributed config
  - 👟 Now it was easier to find certain options like "Display FPS" that was placed in general
  - 🔥 Adds "Quality++" and "Performance++" tabs
  - ℹ️ Recommended usage with [TexTure's Embeddium Options](https://www.curseforge.com/minecraft/mc-mods/textrues-embeddium-options)
- [🔥] Added "FastChest" option, witch increases FPS in hundreds on chest rooms
  - 🐛 It can't be used with Flywheel + Instancing or Batching backend.
- [🔥] Changing language no longer reloads all resources
  - 🛠️ A QoL change that increases language changing
- [🔥] Added fresh localization languages
  - 🍜 Simplified Chinese (thanks to Wheatley11 and qznfbnj)
  - 🥐 French (thanks to Myuui)
  - 🍾 Russia (thanks to CatAndPaste)
  - 🌮 Mexican Spanish (thanks to ME, that's right, I am mexican)

# RELEASE 1.1.0
Note: The Next version plan was focused on optimised DynLights, cleanup Emb++ and Expand TrueDarnkess

- [🔥] Embedded DynamicLights on Embeddium++ (i fork this mod just to do this damn joke)
  - ⚠️ That means you need to REMOVE Magnesium/Rubidium DynamicLights
  - 🔥 Adds "Faster" speed option
- [🛠️] EntityCulling whitelist optimized
  - 👟 Works much faster than before
  - 🛠️ Fixes a memory leak (check [#3](https://github.com/SrRapero720/EmbeddiumPlus/issues/3))
  - 🔥 Now supports wildcards (``modid:*`` ignores all entities of that mod)
- [🛠️] Fixed you can't go back to windowed screen after switching to Borderless/FullScreen
- [🔥] Added back JEI Overlay hide when you are not searching
  - ℹ️ Message for mezz: is disabled by default :) don't worry
- [🛠️] All options screen text of Emb++ is now localized
  - ℹ️ You can contribute with translation [here](https://github.com/SrRapero720/EmbeddiumPlus/blob/1.20/src/main/resources/assets/embeddiumplus/lang/en_us.json)