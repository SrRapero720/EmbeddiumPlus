# RELEASE 1.5.1
- [🛠️] Tweak modpack mode to only hide Zoom and True darkness page
- [🌐] Updated translations
  - [🍾] Updated Russian translation (by mpustovoi)

# RELEASE 1.5.0
- [✨] Added option "ModpackMode": disables chloride pages registration (forces applied configs)
- [✨] Added Zoom key defaulted to (C)
  - 🛠️ It gots disabled by default when JustZoom or Zume is installed
- [🛠️] Replace ForgeConfigAPI with a custom config utility
- [🛠️] Renamed Metrics page to Overlay
- [🛠️] Removed Zume translations
- [🛠️] Removed Zume integrations
- [🛠️] Updated all JEI, REI and EMI integrations to their latest versions
- [🐛] Fixed crashes caused by Create 0.6.1 (new Flywheel Update)
- [🐛] Fixed crashes with Cubes without Borders
- [🐛] Fixed crashes with Forgery

# RELEASE 1.2.13
- [🐛] Fixed crashes due to non-renamed accessors (fixes BadOptimizations mod crashes)

# RELEASE 1.2.12
- [🛠️] Unshipped MixinExtra (embeddium has it now)
- [🛠️] Re-moved unused resources 
- [🛠️] Performed some sanity changes on the codee

# RELEASE 1.2.11
- [🐛] Fixed NullPointerException crashes opening video settings (mb)

# RELEASE 1.2.10
- [❌] Dropped 1.18.2 and 1.19.2 (this version will be the last release of EMB++ for those versions)
- [❌] (1.18-1.19) Removed ZUME integration (prevent crashes with modern zume)
- [🌐] Updated translations (I forgot to rebase again)
  - [🍾] Updated Russian translation (by mpustovoi)

# RELEASE 1.2.9
- [🛠️] Removed optional dependency of Textrues Embeeddium Options
- [🛠️] Bumped zume version range (and added support for zume 0.19)
- [🛠️] Removed mixins related to embeddium (now uses embeddium events)
- [🛠️] Deprecated zume config screen in favor of future zume releases (And Embeddium++Additions)
- [🌐] Updated translations
  - [🍾] Updated Russian translation (by mpustovoi)

# RELEASE 1.2.8
- [🐛] Fixed weird crash with some mods unregistering their block entities
- [🛠️] Marked Textrues Embeeddium Options as a optional dependency (in favor of future Embeddium versions)
- [🔥] Added "leaves culling"
  - ℹ️ Culls leaves faces when are around other leaves
  - ℹ️ Only culls leaves of the same type (oak leaves cannot cull dark oak leaves)
  - ℹ️ At the moment only available ALL and OFF
- [🛠️] You didn't feel the general page too flooded?
  - 🛠️ Moved the "display FPS" options to Metrics page (spoiler)
- [🛠️] Tweaked FPS display code
  - 🛠️ Doesn't impact the behavior (at all), just simplification
  - 🛠️ the GPU usage update was forced to had a cooldown like the current FPS.
If for some reason a weird mod hardly depends on the immediate gpu usage rate, please report to me :P 
- [🌐] Updated translations (git lost track of master branch and "rebase" never rebased the PRs): 
  - [🍜] Updated Simplified Chinese translation (by qznfbnj)
  - [🍾] Updated Russian translation (by mpustovoi)

# RELEASE 1.2.7
- [🐛] Fix REI is not working
- [🐛] Disabled FastModels
  - 🛠️ The config field is renamed, and options in video settings are disabled too
- [🔥] Added "disable NameTag rendering"
  - ℹ️ Let you not render names on entities, players and item frames.
  - ℹ️ Placed in Quality tab
- [🔥] Added "blueBand" toggle option
  - ℹ️ Let you disable the blue gradient, making skies flat-colored
  - ℹ️ Placed in Quality page
- [🐛] Fix wrong AVG calculation (again) 

# RELEASE 1.2.6
- [🛠️] Compat fix for zume 0.18 (nojil, please slowdown)
  - 🛠️ Version was restricted to above 0.18 and below 0.19
- [🐛] Make static beds and chests models/textures as a integrated resourcepack
  - 🐛 This avoid invisible models/textures for mods with some weird reliance on MC resources.
- [🐛] Fix wrong AVG display on initial games
  - 🛠️ AVG was also tweaked to store 18 values instead of 14
- [🔥] Added EMI/REI support for "Hide JEI until searching" feature
- [🛠️] Revoked license (now is ARR)

# RELEASE 1.2.5
- [🛠️] Removed embedded version of Zume
  - 🛠️ Now is an optional dependency (can be removed)
- [🔥] Added Zoom options on Video settings
  - 🔥 Now you can configure all options of Zume easier and faster.
- [🛠️] Added crash warning for Xenon (avoid any weird report)
- [🐛] Fixed hide JEI option can't be used if Flywheel is enabled

# RELEASE 1.2.4
- [🛠️] Removed ported code of Lamb Dynamic Lights
  - Will be back a new implementation for 1.3.0, more powerful and efficient.
- [🔥] Featured [ZUME](https://www.curseforge.com/minecraft/mc-mods/zume/files/5082090) embedded
  - 🔥 Zoom key is back! powered by [ZUME](https://www.curseforge.com/minecraft/mc-mods/zume/files/5082090)
  - 🔥 Press [C] to zoom
  - 🛠️ Can be configured zoom behavior on video settings
- [🐛] Fixed entity whitelist isn't working
- [🛠️] Moved default ignored entities and tile entities to config defaults
- [🛠️] Fixed true darkness is inverted
- [⚡] Optimized true darkness code

# HOTFIX 1.2.3
Minor things
- [🐛] Fixed F11 borderless doesn't work
  - 🔥 Can replace Fullscreen with Borderless
- [🛠️] Tweak mixin page
  - 🖋️ Renamed page to "others"
  - 👟 Make options not longer request game restart
  - 🛠️ Moved config entries to default config file
    - By default, it will delete old ``embeddium++mixins.toml`` file, because it was no longer used
- [🛠️] Enabled shadows on text by default again (temporal workaround)
- [🛠️] Now uses MixinExtra along some mixins (temporal JarInJar until embeddedt includes it on Embeddium)
- [🔥] Updated translation(s)
  - 🌮 Mexican spanish

# RELEASE 1.2.2
Things that I may forget

- [🐛] Fixed fps text and shadow box rendering multiple times breaking transparency
  - [🔒] Closes [#26](https://github.com/SrRapero720/EmbeddiumPlus/issues/26), [#24](https://github.com/SrRapero720/EmbeddiumPlus/issues/24)
- [🐛] \[1.18.2\] Removed performance Mixins
  - ℹ️ These mixins causes more problems than solutions, even make performance worse (after some deep benches and possible fixes)
- [🍜] Updated Simplified Chinese translation (by qznfbnj)

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
- [🔥] \[1.18.2\] Added a new performance feature: Replace HashMap with a Object2ObjectArrayMap
  - 🛠️ Can be disabled on mixins page
- [🛠️] \[1.18.2\] Make RenderType lookup performance option toggleable on mixins page
  - ℹ️ This performance feature wasn't available on 1.19.2 because Moyank already adds it.
- [🔥] Updated translations
  - 🍾 Russian translation
  - 🌮 Mexican spanish

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