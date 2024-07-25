package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.br2
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object ShovelSoFarChasm : BlogPost("a-shovel-will-only-get-you-so-deep") {
    override val title = "A Shovel Will Only Get You So Deep"
    override val description = "An argument that the current CHASM architecture is fundamentally broken"
    override val publishDate = LocalDate(2024, 7, 24)

    override fun BODY.body() {
        div("content") {
            +"A note before I get into this: this post is not meant to be an argument about the worth of CHASM or the achieveability of its goals. "
            +"The primary focus of this post is that if you are attempting to fulfil its goals of advanced, compatible, "
            +"and pure transformations, then the current architecture for quilt's CHASM implementation is a dead end."

            h2 { +"The existing architecture" }
            +"The current existing architecture for CHASM is a 2 pass system, where transformers first \"lock\" some section "
            +"of the target, and then apply their transformations to that locked section. This is done repeatedly in a loop, "
            +"creating a cyclic system that continues until all transformations are resolved. The locking stage is done with "
            +"a system of filters and selectors to allow transformers to whittle down their selection to the exact area they "
            +"intend to modify, the solver then checks for resolvable locks and permits them to transform. In the case that "
            +"incompatible locks are found, the system instead fails."
            br2
            +"As stated in the README for the project, this is intended to "
            q {
                cite = "https://github.com/QuiltMC/chasm/blob/832525bcd2d74f90b1118d8857a5265eb588165f/README.md"
                +"allow multiple users to transform the same code"
            }
            +", however the current architecture fails at this, and seems to have been designed without a thorough exploration of "
            +"how and why transformation compatibility fails. These issues are fundamental to the 2 pass locking system, "
            +"and, I believe, makes the system a proper one to achieve its goals."

            h2 { +"Intent, Intent, Intent" }
            +"I believe one of the biggest mistakes made while developing CHASM was failing to consider how the \"intent\" of any transformation should "
            +"be handled. Locking allows transformers to express an intent of modifying an area, but that is the total extent of what can be expressed or analyzed. "
            +"Mixin, instead, is largely successful at handling collision by nature of having high level, static transformations. "
            +"Using pre-defined components limits expression, but in return allows for sound and powerful resolution. "
            +"By requiring a transformer to express its intent in a set of pre-filled boxes, it successfully implements the majority of resolution strategies."
            br2
            +"The problem with locking, thus, is that it fails to represent what a transformer actually "; i{+"does."};
            +" By only having lock data available to the solver, it can only resolve conflicts based on lock data. This becomes a "
            +"notable issue in several cases that could otherwise be handled appropriately."

            h2 { +"I failed the MSR test and all I got was this T-Shirt" }
            +"One of the most obvious cases of this approach failing is transformation duplication. If 2 different mods copy "
            +"the same transformation, how should resolution be handled? In this case the transformations are entirely identical, "
            +"both transformer A and transformer B simply, say, changing a number to the same value. "
            br2
            +"Its quite obvious to us that they should both \"apply\" or de-duplicate in some manner, but the solver only "
            +"has information on their locks. As far as CHASM can be concerned here, these are 2 transformations that want to "
            +"lock the exact same section and have no explicit ordering. The only real options besides changing architecture in this case are:"
            ul {
                li { +"Fail an otherwise trivially resolvable transformation" }
                li { +"Fork the solver and choose a working order" }
                li { +"Enforce an artificial (but pure) ordering (while still detecting conflicts between non-identical transformations)" }
            }
            +"The first 2 are obviously non-viable, (failing the transformation means losing inter-transformer compatibility, and forking "
            +"would massively increase the compute cost for miniscule benefit) but the ordering proposal is a bit more subtle."

            h2 { +"Too fast, too soon" }
            +"Artificial ordering seems like an alright solution at first, its possible to implement in a pure manner, and would "
            +"allow for forcing one of the transformers to apply, but the conflict detection is the main issue. To properly implement this, "
            +"CHASM would need to implement another, completely unique collision detector "; i{+"inside its existing collision detector. "}
            br2
            +"This, objectively, is a solution! Implementing this "; i{+"would"}; +" allow CHASM to handle duplicated transformations. "
            +"However it really only solves this singular issue, and that's the major issue here. Duplicated transformers are "
            +"not the only issue with the lock approach. Although this would solve that case, its only a superficial patch on "
            +"a still quite weak resolution system. The collision handling is still being filtered through the lock system, placing "
            +"significant limits on using transformer intent. For example, an artificial ordering couldn't handle an infinite transformer loop "
            +"where they both undo each others patch."

            h2 { +"Infinite monkeys just build sludge" }
            +"One option I didn't bring up earlier was the possibility of a negotiation API for transformers to use. Some way "
            +"for transformers to communicate and \"argue\" about what patches to apply. This would allow basically all "
            +"edge cases (like infinite cyclic loops) to be resolved before ever having to transform anything."
            br2
            i{+"But thats just CHASM 2 anyways"}
            br2
            +"By building an expansive method for transformers to discuss patches, you've just built chasm all over again, inside chasm. "
            +"Any solution to the issues of chasm being unable to handle such and such edgecase will either bloat CHASM into an unmaintainable chain "
            +"of systems, or is just starting over again. The 2 pass lock system fundamentally limits CHASM's ability to meet "
            +"its goal of handling collisions by restricting the information that can be actually used for resolution."

            h2 { +"We can do better" }
            +"When you sit down and actively try to break CHASM, its clear that not only does the current architecture fail in numerous ways, "
            +"but patching it a fool's errand. There are always going to be edge-cases as long the ability to express intent within the system is limited, "
            +"and rewrites may as well start over."
            br2
            +"I know I said this wasn't about the \"worth\" of CHASM at the start of this article, and that was mostly true, but I want "
            +"to make a point of comparison here that CHASM, unless it starts over with an intent to actively cover as many conflicts as possible, "
            +"is largely just going to exist as a harder to use, and possibly worse, Mixin. By so heavily reducing expression, "
            +"CHASM has no way to be either easier to use or have better compatibility. Turing completeness doesn't matter when the "
            +"end goal is compatibility, you need protocols for that."
            br2
            +"It can be difficult to imagine what a system meant for expressing \"intent\" would look like in code, but it can be done. "
            +"Mixin & Mixin Extras achieve part this by simply having a significant number of high-level constructs you can use. "
            +"Mixin 2 is not at all the right path for CHASM, but I do think a serious attempt should take a deep look at why they work. "
            +"I've been personally experimenting with a constraint based approach, and that is largely what has led me to my opinions "
            +"on the current architecture and inspired the \"we can do better\" title of this section. Being able to express constructs like "
            +"\"at least [value]\" or optionals like \"should not be [value]\" really makes you feel that the current approach is a mistake. "
            +"Including the intent of a transformation, even on a small enough scale as a single value change, can handle existing issues "
            +"and open paths for entirely new compatibility systems that otherwise cannot exist."

            h2 { +"No, really, we can do better" }
            +"CHASM, as it stands, even if it patches all the edge-cases within its existing architecture, has a massive "
            +"amount of stuff its leaving on the table. Evaluating from the ground up how transformers can be both expressive "
            +"and compatible does not always lead to a turing complete 2 pass lock based system. The existing architecture "
            +"is a result of building a bytecode transformer first, and a conflict handler second. We're not restricted to the "
            +"even the concept of ordering here, there is absolutely the possibility to creating something uniquely powerful. "
            +"As such, if nothing else, I believe starting with a new architecture designed explicitly for compatibility would be a major boon towards achieving its goals."
        }
    }
}
