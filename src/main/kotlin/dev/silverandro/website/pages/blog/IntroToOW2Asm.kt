package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.*
import dev.silverandro.website.util.embedFile
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object IntroToOW2Asm : BlogPost("an-intro-to-ow2-asm", true) {
    override val title: String
        get() = "an (im)practical introduction to ow2 asm"
    override val description: String
        get() = "an introduction to ow2 asm, with examples from my experience with the library"

    override val publishDate = LocalDate(2023, 7, 15)

    override fun BODY.body() {
        div("content") {
            +"please note that this post will not be a thorough introduction to "
            _a("https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-6.html", "jvm bytecode")
            +" as it is focused on using ow2 asm. i recommend checking out the documentation and trying other parts yourself (either with"
            +" asm or a project like "
            _a("https://github.com/roscopeco/jasm", "jasm")
            +")"

            h1 { +"whats so special about ow2 asm?" }
            _a("https://asm.ow2.io/", "ow2 asm")
            +" (or just asm for the rest of this post) is \"an all purpose Java bytecode manipulation and analysis framework.\" "
            +"this means it can read class files, create classes from scratch, and write it all out to either be written to disk or loaded live."
            br; br
            +"The main reason to use asm is that its "
            i { +"fast" }
            +", outpacing many other libraries that also work on class files. This speed comes at a cost though, youre working a "
            +"lot closer to the raw class file. asm, by and large, does not verify anything you do, and will happily output invalid class files "
            +"(other than some cases that we'll get to later)."

            h2 { +"a warning about this post" }
            +"asm uses a visitor pattern basically everywhere, as its the expected way to interface with the library. "
            +"however i "; strong { +"do not " }; +"use the visitor api when working with asm. i find it:"
            ul {
                - "clunky"
                - "hard to use"
                - "destroys control flow"
                - "various other issues"
            }
            +"so this tutorial will instead work in an procedural manner thanks to "; inlineCode("asm-tree")
            +". youre welcome to use the visitor pattern yourself, but you wont find instructions here."

            h2 { +"setting the stage" }
            +"the first few examples here are going to use this calculator code that ive intentionally left Not Great™."
            br
            codeBlock(Language.JAVA, embedFile("tutorials/asm/Calculator.java"), 400)

            +"this works pretty well, putting in a simple calculation gives us the correct result"
            codeBlock(Language.NONE, """
                enter the calculation:
                5 + 6 + 10 - 2 * 3
                result: 57
            """.trimIndent())
            +"so lets try poking at this a bit with asm! "
            br;br
            +"im going to assume for the rest of this tutorial that youve compiled the calculator into "; inlineCode("Calculator.class")
            +", if you need a copy of it, just compile the calculator, rename the output "; inlineCode(".jar"); +" to a "; inlineCode(".zip")
            +" and extract the file."

            h2 { +"reading some basic info from a class file" }
            +"to read a class file you need a "; inlineCode("ClassReader"); +" which can then export the read data to something else, "
            +"such as your visitor or in this cases, a class node. after reading in the data, its just a matter of printing it out."

            codeBlock(Language.JAVA, embedFile("tutorials/asm/readAsmClass.txt"))
            codeBlock(Language.NONE, embedFile("tutorials/asm/readClassResult.txt"))

            +"some important things to note about this:"
            ul {
                - "the class version isnt directly the java version"
                - "to actually get any data about what a method takes or gives, you need to handle the descriptor"
                li {  +"the \"class name\" is the "; i { +"fully qualified" }; +" name of the class, using slashes to denote the path/package" }
            }
            +"the format of descriptors and class names is important, asm will not verify them for you when modifying or generating classes. "
            +"it becomes very easy, especially with complex class generators, to accidentally use dots instead of slashes, add an extra "
            inlineCode("L"); +" to the start of a class name in a descriptor, and more."
            br
            +"(the "
            a("https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-4.html#jvms-4.3", "_blank") {
                +"class file specification"
            }
            +" has a good section on these)"
            br;br

            +"ok so, we can read data about these classes, but what if we want to modify it? well theres 2 major improvements we can make "
            +"to our calculator, so why dont we try applying those with asm!"

            h2 { +"modifying our class with asm (and how code is represented)" }
            +"lets make 2 patches to our class file here"
            codeBlock(Language.DIFF, embedFile("tutorials/asm/Calculator.patch"))
            +"the first change here is pretty simple, we switch the behavior of operations from changing the mode of the calculator to "
            +"only applying once. although it could be argued to be a feature, its "; i { +"technically" }; +" not desired behavior."
            br
            +"second change is very simple as well, we replace a weird \"attempt-and-catch\" with a proper check. much faster and "
            +"easier to read! (no, java really doesn't have a built-in way of checking this, i looked, lmao)"

            +"so, lets get to this! first off, lets see how asm represents the code of our methods by dumping "; inlineCode("calculateResult"); +"."
            codeBlock(Language.NONE, embedFile("tutorials/asm/calcResultInsn.txt"))
            +"youll notice this isnt particularly usable at a glance. the only real information here is that "; i { +"everything" }
            +" is an insn node here, even stuff that doesnt have an instruction. labels and line numbers are a notable part of this, "
            +"which is one of the things that asm does actually do for you. labels are the especially useful part here as you dont have to keep "
            +"track of offsets manually."
            br
            +"anyways, to actually view the bytecode of a class, youre much better off using "; inlineCode("javap"); +" which is bundled with your JDK. "
            +"so, a quick "; inlineCode("javap -c -p Calculator.class"); +" later and...."
            codeBlock(Language.NONE, embedFile("tutorials/asm/javap_calc.txt"), 400)

            +"looking at this output, it seems like it makes the most sense to jump right between 138 and 139, so lets target that! "
            +"remember that asm is going to add a lot of extra labels here, these arent going to line up to exact indices."
            br
            +"its kind of tricky to get an injection point like this, theres a few options here, like using labels to figure out the specific "
            inlineCode("JumpInsnNode"); +" that corresponds to the "; inlineCode("goto"); +" at 139, matching specific patterns of instructions, "
            +"or my choice in this case, finding something unique and just working backwards. we can start at 145 and go back until we see a "
            inlineCode("JumpInsnNode"); +" which will let us know we've reached the right location for our injection."

            codeBlock(Language.JAVA, """
                AbstractInsnNode currentNode = null;
                for (AbstractInsnNode insn : method.instructions) {
                    if (insn instanceof MethodInsnNode methodInsn) {
                        if (methodInsn.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsn.name.equals("charAt")) {
                            currentNode = methodInsn;
                            break;
                        }
                    }
                }
                
                while (!(currentNode instanceof JumpInsnNode)) {
                    currentNode = currentNode.getPrevious(); // null check snipped
                }
                currentNode = currentNode.getPrevious();
            """.trimIndent())
            +"now we just have to inject our instructions. we need 2 instructions here, one "; inlineCode("ldc"); +" to load the "
            +"null byte char, and a "; inlineCode("istore"); +" to actually save it to the variable. (in this case it happens to be "
            inlineCode("istore_2"); +", you can tell because thats the one used before the switch lookup)"
        }
    }
}