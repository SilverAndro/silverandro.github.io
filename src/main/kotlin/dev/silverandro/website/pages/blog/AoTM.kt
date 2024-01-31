package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.br2
import dev.silverandro.website.util.embedFile
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object AoTM : BlogPost("semi-theoretical-high-performance-modding") {
    override val title = "A Semi-Theoretical Approach for a High-Performance Minecraft Modding Toolchain"
    override val description = "A look at a way to build a modding toolchain that emphasizes performance above all else"
    override val publishDate = LocalDate(2024, 1, 31)

    override fun BODY.body() {
        div("content") {
            +"Performance mods are no small part of minecraft modding nowadays, with sodium notably becoming a must-have "
            +"for *any* installation, fabric's rapid launch speed being touted, and other systems like dashloader making "
            +"launching the game faster by several factors by utilizing caches. However, so far there have not been notable "
            +"attempts to center performance with a modding toolchain, hence this proposal for a theoretical, ground-up "
            +"system designed with performance as the primary goal of the project, rather than relegating it to 3rd parties."
            br2
            +"Given the tendency of modded players to often play the game over multiple launches, especially with modpacks, "
            +"the primary goal of this proposal is to improve game performance, followed by reducing the time to launch "
            +"of subsequent launches, even if this comes at the cost of the first launch."

            h2 { +"What slows down modern modded games?" }
            +"When looking at the incredible complexity of modding minecraft, 2 main “phases” become apparent, load time "
            +"and runtime. Load time affects how long it takes to actually get into a game, while runtime involves the "
            +"actual game itself. With this distinction, sodium would be considered a runtime performance mod, while "
            +"dashloader would be considered a load time performance mod. There is ambiguity within this system, of course, "
            +"such as if loading a world counts as runtime or load time. However, as there have been next to no attempts "
            +"to optimizing this stage of the game outside replacing the world format entirely, I do not believe it needs "
            +"a notable amount of attention."
            br2
            +"Load time additionally is broken down into first and subsequent launches, where a subsequent launch is a "
            +"second or further launch without modifying the mod set, configurations, or other factors that could invalidate "
            +"any caches. Caching is often done only with remapping, rather than other extensive systems without the aid "
            +"of external mods. There have also been efforts at caching game modifications, such as quilt's chasm, however "
            +"that has stalled and is being reconsidered, and I am not aware of any replacements."

            h3 { +"Why load time is slow" }
            +"When preparing to launch the game, the majority of the time spent comes down to what both the loader and the "
            +"mods themselves are doing. Improving loader performance is quite difficult, especially when compared to existing "
            +"thin loaders like fabric and quilt, but there are some things that are constant, such as remapping the game. "
            +"Remapping is a required step for any modern toolchain, as otherwise it becomes impossible for mods to be "
            +"cross-version compatible, a common expectation nowadays. Outside optimizing the remapper, this adds a fixed "
            +"3-5 second delay to first launch, more so if mods need to be remapped as well. As such, we can conclude that "
            +"the ideal form of remapping is a toolchain where mods are remapped at build time, and the loader is only "
            +"required to remap minecraft itself by making use of an intermediary system."
            br2
            +"The other major slowdown that loaders are commonly in charge of is game transformations. It is impossible to build, let alone maintain, a full API surface for the entire game that is capable of supporting all mods. As such, mod loaders often provide a standard tool or tools for modifying the game itself, independent of any standard api for the loader. These tools are typically designed to be high level, as to make it easier for developers to interface with and reason about the system. This adds a runtime overhead to the system however, as the transformation system must reason about each class, each transformation, and in most cases, how it interacts with other transformations, such as with the mixin project. As transformations are not currently cached as far as I am aware, this adds a fixed delay to *every* game launch. A large fabric modpack can spend upwards of 15-20 seconds just preparing and utilizing mixin. This shows a clear use case for a cachable transformation system, moving the cost of resolving transformations to first launch, especially with the popularity of modpacks."
            br2
            +"There are currently no systems for optimizing how long mods take to initialize, outside of thinning down how much the loader cares about each mod's specifics (such as reducing event complexity) or multithreading. The semi-theoretical approach for optimizing mod initialization discussed in this paper is discussed in the later section, “Ahead of Time Modding”."

            h3 { +"Why runtime is slow" }
            +"Minecraft itself, generally. Minecraft's various abstraction layers, although they make development easier, add overhead to several systems, as well as other issues such as rendering performance having significant room for improvement (hence the existence of sodium and before that, optifine). There is no real way to optimize slow mods automatically other than providing specialized high-performance APIs for them to use rather than building systems themselves. A toolchain focused on performance, however, could include many of the optimizations typically relegated to 3rd parties into the base API surface, providing an optimized experience even without additional mods, as well as other more specialized optimizations."
            br2
            +"This design would allow those optimizations to additionally be better supported by the toolchain by nature of being first party. A rendering API could be designed around an optimized pipeline, rather than having 3rd parties bolt it onto themselves later in development, limiting compatibility and increasing development time."

            h2 { +"Ahead of Time Modding" }
            +"The difficulty of optimizing runtime performance of mods raises a question, what if we were to move the near-impossible runtime optimizations to load time? The basic principle behind ahead of time modding (AoTM) is that mods, rather than being programs linking against an API, are at least partially instead a specification of what content modifications to be made to the game, that are collected and converted by the toolchain to direct game modifications. For example, while a traditional system might look like this:"
            unsafe { +embedFile("/blog/aotm/traditional.svg") }
            +"An AoTM system may instead look like this:"
            unsafe { +embedFile("/blog/aotm/aotm.svg") }
            +"It is obviously not possible to completely convert mods into static, load time “recipes”, hence the “Initialize Mods” step sticking around. However, given the reduced requirements of stuff that must be done at runtime, the majority of game content/modifications could be evaluated on first launch, and therefore cached, reducing the cost of initialization significantly. This would be especially prevalent in the case where content is generated on the fly due to not being included at build time, either due to bad design or because it is reactive to other content. In effect, an AoTM system would act as a dynamic jar-mod creation system, basing its patch creation off of the provided mods."

            h3 { +"Specialized Optimizations" }
            +"Aside from the obvious implications of improving cacheability of mod content, a major implication of these patches existing as a sort of recipe, is that the game could be reworked as part of the API to remove or heavily reduce the abstraction layers at load time. As a tangible example, world generation is pulled together from a variety of different classes, often instanced from a json file. An AoTM could instead bake this generation into specialized classes, allowing the JIT to better optimize and possibly provide notable speed bonuses. This system could also provide a standardized way to query information about other mod content and vanilla at runtime."
            br2
            +"These small improvements by “flattening” the game could be replicated in numerous places, each one providing just enough of a benefit that all together it creates a tangible difference. Flattening could also dynamically react to various requirements of mods, choosing a faster path only if there aren't any mods that depend on some feature, for example."

            h3 { +"Modpacks and (Lack of) Registry Sync" }
            +"Since content could be described statically in a cachable manner, modpacks could therefor implement an optimization of shipping the built caches along with their config and other content. This would significantly increase “first” launch speed, as the actual first launch occurred on the developers computer. Some would argue that this would introduce security vulnerabilities, but modpacks are already links to generic sets of mods. If a modpack developer wished to insert malicious code, it would certainly be easier to just write it into a \"modpack utility mod\" in a plain programming language."
            br2
            +"If processing order is deterministic as well, then registry sync could be entirely eliminated. Simply by adding all the patches that would require syncing to a hash, it would be trivial to check if the hashes match when joining a server, rather than going through an extended sync process. A diff process would likely still have to exist however, so that people joining could be notified of what mods they are missing, have extra, or in the case of extra mods perhaps even do a sync anyway."

            h3 { +"Optional Content and Sided Mods" }
            +"The reason that I believe a fallback system of some sort would need to exist largely comes down to optional mods, such as a server side mod with an optional client side component or vice versa. If that mod specifies a packet for exchanging data, then with the proposed hash check system, there would be no way to build an optional client side component. In such a case, there must be a secondary system that mods are capable of taking advantage of that keeps the more dynamic nature of the original interfaces."
            br2
            +"This would of course increase the complexity of building these optimizations into the game, but I believe that it would be necessary in order to keep roughly on par with current trends in modding. These bits of optional content could use the same static system luckily, perhaps with a field marking content that can be optional as optional, with it defaulting to “off”."

            h3 { +"Reloading Runtime" }
            +"One approach that would allow keeping better compatibility with the various dynamic systems would be to allow unloading classes via runtime conversion into the compiled form. This way datapacks that, for example, define world generation could be both optimized at and removed from runtime. This would incur a slight cost for content actually built AoT, but still a significant boost over the existing runtime systems."
            br2
            +"An approach like this, where dynamic content is “flattened” dynamically at runtime while mod content is flattened AoT and fed into the same system, would likely allow a toolchain following the design outlined in this document to better adapt to the future of the game. Minecraft is clearly being pulled in a direction where datapacks are capable of providing significant amounts of content, so having existing frameworks for supporting dynamic content would be quite beneficial."

            h3 { +"Why “Semi-Theoretical”?" }
            +"The main reason I propose this system as only semi-theoretical is that I believe that the majority of the components required either already exist or have working prototypes. Chasm, for example, “works” but is missing major integration into quilt loader, as well as a stable usage surface. Other projects such as the various small projects to compile `.mcfunction` files at runtime have been shown to work, or even minor components such as sodium's dynamic vertex consumers. As such, I find the idea that this project is only theoretical to be a misnomer, as it already exists in minor, less developed cases all over the place. Only a project with those tactics as its primary goal and features is missing."

            h3 { +"Conversion of Existing Loaders" }
            +"I find it unlikely that existing loaders could be reasonably moved to an AoTM system, due to the complexities involved and the significant breakages it would incur for the already built mods on the platform. As a major component of this system would be the ability for content to be described statically, any existing tooling would need to be reworked, and heavily discourage if not outright prevent traditional designs in order to take maximum advantage. The system would essentially have to be built from the ground up anyway, discarding its existing tooling or replacing it with a buggy, partially patched alternative. If a loader depended on compatibility with other traditional systems, a full conversion would also not be possible, reducing the effectiveness to the point it is unlikely to be beneficial at all."

            h2 { +"Final Thoughts" }
            +"I would be genuinely surprised if an AoTM toolchain was ever developed, let alone gained massive popularity. The complexities involved and the inertia of existing modding toolchains is unlikely to permit such a radical reworking of intent. However, such a toolchain would be more likely to persist into the far future as minecraft becomes more and more abstracted and dynamic, with the AoTM toolchain slowly but surely shifting to act more as a general optimization mod rather than a loader."
            br2
            +"I primarily wrote this document because i couldn't get the ideas out of my head otherwise, and the blog section on my website has been empty for far too long. Sorry if it isn't particularly comprehensible, but thanks for sticking through my rambling <3"
        }
    }
}