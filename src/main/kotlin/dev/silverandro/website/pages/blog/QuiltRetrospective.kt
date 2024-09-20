package dev.silverandro.website.pages.blog

import dev.silverandro.website.components._a
import dev.silverandro.website.components.br2
import dev.silverandro.website.components.inlineCode
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object QuiltRetrospective : BlogPost("the-quilt-retrospective") {
    override val title = "The Quilt Retrospective"
    override val description = "Thoughts on what went right, wrong, and worse"
    override val publishDate = LocalDate(2024, 9, 20)

    override fun BODY.body() {
        div("content") {
            h2 { +"we hardly knew ye" }

            +"With the news of "; _a("https://twitter.com/doctor4t_/status/1837074573470085546", "doctor4t moving off quilt")
            +" its caused me to really understand that, yes, quilt is actually not going to recover in any meaningful form. "
            +"As such, it seems like an apt time to fully collect my thoughts and share what I think went wrong with quilt. "
            +"I put this together over a somewhat short timeframe, so I apologize if it is unclear in some way. "
            +"I will say generally that I don't think that there is any one specific thing that led to quilt's downfall, "
            +"but more a cascade of bad decisions and inexperience."

            h2 { +"bureaucratic bureaucracy" }
            +"One of the more common complaints with quilt pre-takeover was that the bureaucracy was too slow and overbearing. "
            +"I disagree with this for a few reasons but largely because I think people simply did not understand why the bureaucracy was actually failing. "
            +"I do think the RFC system was, overall, a net positive for quilt. Having specific standards for systems like staff removal "
            +"or schemas like "; inlineCode("quilt.mod.json"); +" helped smooth over what would otherwise be fairly notable pain points. "
            +"Also, it just simply did not impede the majority of work that quilt needed done. The only real interfacing with quilt was "
            +"the teams process, which is probably the one thing I do agree on needing to be removed. All the other complaints about the "
            +"RFC process wouldn't have been solved by removing the RFC process, as the proposals would have languished in random discord threads instead."
            br2
            +"Rather I think the real problem with the bureaucracy was that the majority of people in charge of it did "
            +"not want to or did not have the time to actually do anything. This will come up again later but largely I think this was THE big issue with quilt. "
            +"Repeatedly, there are instances of systems and processes simply Not Being Done because the people who should do them don't want to. "
            +"With that said though, its difficult to talk about this without mentioning the takeover."

            h2 { +"burnout (real)" }
            +"I think the takeover is mainly interesting for the amount of stress and burnout it caused. I don't recall if anyone directly quit over it, "
            +"but it certainly raised tensions internally quite significantly, and rushing out The Statement only made things worse. "
            +"The Statement could have been an important document to construct and share, but trying to combine so many items into it on a short deadline not "
            +"only ruined its possibility, but ended up pushing members past their limits. The takeover, the statement, and reconstruction all at the "
            +"same time was simply too much, even for the notably larger team. Since the takeover and reconstruction could not have been meaningfully delayed, "
            +"I am of the opinion that not putting out a short, stopgap message with a basic summation of events and doing the full thing later was a "
            +"mistake with incredibly far reaching consequences."

            h2 { +"right complaints, wrong reason" }
            +"The takeover was, bluntly, disgustingly unprofessional and the person who carried out it should never be in a position of power again. "
            +"Now, with that said however, they were absolutely correct that quilt was approaching removing racism from the space badly."
            br2
            +"I've gone back and forth on my thoughts on this several times, largely because a lot of the people having complaints about it "
            +"have been racist in some manner about it, but I think I have settled on a belief that it could have been handled much better. "
            +"I would probably classify the approach taken to moderation surrounding the effort to be "; i {+"defensive"}; +" rather than "; i{+"curative"}; +". "
            +"Quilt was declared an anti-racist community, and so people who did not fit that were seen as bad faith rather than parts of a community that "
            +"needed guidance."
            br2
            +"Its an understandable stance, but shows a misunderstanding of how culture works, in that culture is a like a contained gas. "
            +"Its not really possible to simply cutoff part of your culture, or teleport it somewhere new, you have "
            +"to slowly push the container until its in the space you're looking for. By failing to do that, quilt ended up "
            +" gaining a reputation for causing drama within their community. "
            +"This is, obviously, much easier to say as a white person with hindsight, and I don't blame anyone involved. This would have been a hard transition even if "
            +"everyone was slowly pampered into not being explicitly racist. The approach taken however should have been reconsidered once things began to spiral "
            +"out of reasonable control and cause issues for the mod team."

            h2 { +"can you stop arguing so i can resign" }
            +"Biggest thing I remember from quilt was how incredibly miserable the internal channels became. It got to the point where "
            +"stuff actively couldn't be done because even trying to bring up time-critical actions was quickly buried under arguing and debates. "
            +"It obviously didn't start out that badly, but it was never reigned in and just compounded over time. "
            br2
            +"Quitting quilt moderation was a snap decision on part. It was entirely because I went to check on the moderation chats to see if anything need my attention, and "
            +"could only "; i{+"stare"}; +" for upwards of 15 minutes or so. I was unable to do anything but watch as the chat was filled with "
            +"some random argument that was clearly going in circles. I was eventually able to form the thought of \"I can't deal with this anymore\", made up an excuse, "
            +"and got out. I genuinely wanted nothing more than to help grow and maintain the community and I couldn't even do that "
            +"because everywhere meant for discussing that was just endless fighting."
            br2
            +"My understanding is that these arguments reached a boiling point a month or 2 later, leading to dozens of resignations and putting quilt where it is now."

            h2 {+"all together now (or not)"}
            +"Its difficult to write a specific thought or action on what I think should have been done differently with quilt, but I think a lot of it "
            +"can be traced back to the community team. The failure to maintain a cohesive force of people who at least tolerated and respected each other "
            +"caused a bunch of cascading issues. That combined with a strong desire from certain members of the team to not have to do any proactive "
            +"moderation work ever, or just community work in general, led to both new issues and existing issues getting worse."
            br2
            +"I quite liked being part of the team when it was possible to actually focus on the community, but once it started fraying the whole thing just died. "
            +"Burnout internally was inevitable because there was not enough effort to remove members who were dead weight or manage those actively causing problems. "
            +"Ultimately there was just small step after small step away from a focused team into a squabbling mess that ended up taking the project with it. "
            +"Its not possible to blame any specific person for this, everyone had a justification or someone else who should have stepped in but didn't."

            hr {  }

            h2 { +"just look at them now" }
            +"I can't like, "; i{+"not"}; +" talk about modern quilt right? Its more of a rant than a retrospective, but, "
            +"I feel like greatest validation I can give to my thoughts on this is largely, \"just look at modern quilt\". Theres basically no one left, "
            +"and as far as I know, the last person who actually wanted to try and rebuild was (imo, for bigoted reasons) completely removed from the project. "
            +"The majority of people left are actively opposed to learning from what happened, and just assuming they can keep everything running, or it will be fine to steal "
            +"the reconstruction plan from the ousted person and somehow implement it without any team members."
            br2
            +"Quilt has gained cohesion in its team with the wrong people, those who aren't active and don't care about the community. "
            +"Burnout is trotted out as an excuse every time someone points out the obvious failures, but in the almost year since there has been no effort to find people, "
            +"and they aren't doing anything else to rebuild or deal with the burnout they keep claiming is a major issue. "
            +"Those who were propped up by other team members doing the real work have suddenly been faced with having to do something after they let the others fall away."
            br2
            +"Quilt is still repeating its mistakes of bureaucracy where it shouldn't be, a failure to treat the community like a community (not that theres much left), "
            +"and an active fear against having to actually tend to the space and project. Without the people who actually worked on anything, quilt is just a shambling corpse. "
            +"There is of course acknowledgement that these are issues, but no one acts on them. Supposedly there are discussions happening in an off server channel, "
            +"But even if that's true, that just reinforces it being basically impossible to rebuild, they cant onboard anyone if everything happens in a private space disconnected "
            +"from quilt itself."

            h2 { +"Conclusions-ish" }
            +"It is theoretically possible that quilt could recover internally, but with more and more mods leaving quilt, I think the user acquisition cycle of a mod-loader is over. "
            +"The months of refusing to address even trivially identifiable issues have killed quilt for now, leaving only people who clearly "
            +"don't and havent respected it as a project, and community, in charge."
        }
    }
}