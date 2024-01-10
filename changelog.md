# RELEASE 1.2.1
Well, we are no longer redundant, but we have redudant code :)

- [🛠️] Optimized FPS Display feature (fixes [#20](https://github.com/SrRapero720/EmbeddiumPlus/issues/20) and [#21](https://github.com/SrRapero720/EmbeddiumPlus/issues/21))
  - 🛠️ Removed rendering mixins (using forge events)
  - 🐛 Fixed FPS Chart is not rendering
  - 🐛 [18.2-19.2] Fixed crashes related to that feature 
  - 👟 Optimizes rendering speeds (sweet)
  - 🔥 FPS, MIN, AVG, GPU, RAM texts are translatable
  - 🔥 Now texts are better colored
- [🛠️] Optimized EntityCulling feature (fixes [#19](https://github.com/SrRapero720/EmbeddiumPlus/issues/19))
  - 🐛 Fixed Block Entities never stop culled disabling on config
  - 🐛 Fixed WATERFrAMES Block Entities got culled
  - 🔥 Added whitelist for Block Entities
  - 👟 Speed up whitelist check for Entities and Block Entities
    - ℹ️ When an Entity matches with the first whitelist value, this never got marked as "checked," checking it indefinitely
- [🐛] Fixed DynLights on entities (like GlowSquid) are not glowing (closes [#23](https://github.com/SrRapero720/EmbeddiumPlus/issues/23))
  - 🐛 Also added missing resources for the proper functionality of DynLights

# RELEASE 1.2.0
The performance and overhaul update... just to be less "redundant"... or what do you think LunaPixel?
I am redundant now?

- [🐛] Removed ``ExtendedServerViewDistanceMixin``
  - 🛠️ Some reports culprit it causes a GPU MemoryLeak. I suggest using other alternatives like [Farsight](https://legacy.curseforge.com/minecraft/mc-mods/farsight)
- [🔥] Added "FastChest" feature, which increases FPS in hundreds on chest rooms
  - 🐛 It can't be used with Flywheel + Instancing or Batching backend.
  - ℹ️ Placed on Embeddium's performance page
  - 👟 Disabled if Enhanced Block Entities are installed
- [🛠️] Increased range of DynLights from 7.2 to 12 (workarround for [#8](https://github.com/SrRapero720/EmbeddiumPlus/issues/8))
- [🔥] Added "FastBeds" feature, which increases FPS in bedrooms
  - ℹ️ Why do you in the first place have a lot of beds?
  - ℹ️ Placed on Embeddium's performance page
- [🔥] Added "Font Shadows" toggle
  - ℹ️ Let you disable font shadows, giving texts on minecraft a flat-style
  - 🛠️ Increases FPS on screens with a lot of text
  - ℹ️ Placed on Embeddium's performance page
- [🛠️] Added missing options of True darkness into video settings
  - ℹ️ These options exist since port was made, but it never got added into video settings
  - 🛠️ Now are more accessible and contains a bunch of configurations
- [🛠️] Added Mixins page to disable Emb++ features
  - 🛠️ Can disable F11 key mixin (which adds the borderless option)
  - 🛠️ Can disable LanguageReload mixin feature
- [🔥] Overhaul Display FPS feature
  - 🔥 Now can be change position between LEFT, CENTER, RIGHT (left by default)
  - 🔥 Added option to also show GPU and RAM usage in percent
  - 🔥 Added option to render text with a shadow box
  - 🛠️ Now texts are colored based on how bad are your FPS and GPU/RAM usage.
  - 🛠️ [1.18.2] Backported Mojang GPU usage profiling (added on F3 overlay and FPS Display)
  - ℹ️ Placed on Embeddium's general page
- [🔥] Redistributed config
  - 👟 Now it was easier to find certain options like "Display FPS" that was placed in general
  - 🔥 Adds "Quality++" page, containing all extra quality settings
  - 🔥 Adds "True Darkness" page, containing darkness features
  - 🔥 Adds "Entity Culling" page, which contains all render limit for BE and entities
  - ℹ️ Now mod depends on [TexTure's Embeddium Options](https://www.curseforge.com/minecraft/mc-mods/textrues-embeddium-options)
- [🔥] Changing language no longer reloads all resources
  - 🛠️ A QoL change that increases language changing
- [🛠️] Rewrote configs
  - 🛠️ May you notice a 1~5 extra FPS. Thank me later
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