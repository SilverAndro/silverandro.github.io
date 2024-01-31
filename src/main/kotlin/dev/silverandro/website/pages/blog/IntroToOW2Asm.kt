package dev.silverandro.website.pages.blog

import dev.silverandro.website.components.*
import dev.silverandro.website.util.embedFile
import kotlinx.datetime.LocalDate
import kotlinx.html.*

object IntroToOW2Asm : BlogPost("an-intro-to-ow2-asm", true) {
    override val title: String
        get() = "an (im)practical introduction to ow2 asm"
    override val description: String
        get() = "an introduction to ow2 asm, with examples based on my experience with the library"

    override val publishDate = LocalDate(2023, 11, 10)

    override fun BODY.body() {
        div("content") {
            +"please note that this post will not be a thorough introduction to "
            _a("https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-6.html", "jvm bytecode")
            +" as it is focused on using ow2 asm. i recommend checking out the documentation and trying other parts yourself (either with"
            +" asm or a project like "
            _a("https://github.com/roscopeco/jasm", "jasm")
            +"). ill be explaining some parts but i do expect you to have a basic understanding of jvm-as-a-stack-machine and some simple "
            +"instructions."

            h1 { +"whats so special about ow2 asm?" }
            _a("https://asm.ow2.io/", "ow2 asm")
            +" (or just asm for the rest of this post) is \"an all purpose Java bytecode manipulation and analysis framework.\" "
            +"this means it can read class files, create classes from scratch, and write it all out to either be written to disk or loaded live."
            br2
            +"The main reason to use asm is that its "
            i { +"fast" }
            +", outpacing many other libraries that also work on class files. This speed comes at a cost though, youre working a "
            +"lot closer to the raw class file. asm usually does not verify anything you do, and will happily output invalid "
            +"class files (other than some cases that we'll get to later)."

            h2 { +"a warning about this post" }
            +"asm uses a visitor pattern basically everywhere, as its the expected way to interface with the library. "
            +"however i "; strong { +"do not" }; +" use the visitor api when working with asm. i find it:"
            ul {
                - "clunky"
                - "hard to use"
                - "destroys control flow"
                - "various other issues"
            }
            +"so this tutorial will instead work in an procedural manner thanks to the "; inlineCode("asm-tree")
            +" library that ow2 also distributes. youre welcome to use the visitor pattern yourself, but you wont find instructions here."
            +" it can be much faster depending on what youre doing, but it is just a visitor."

            h2 { +"setting the stage" }
            +"the first few examples here are going to use this calculator code that ive intentionally left Not Great™."
            br
            codeBlock(Language.JAVA, embedFile("blog/asm/Calculator.java"), 400)

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
            +"such as your visitor, or in this case, a "; inlineCode("ClassNode"); +", which is the root of the \"tree\" structure "
            +"representing the class. we pass in "; inlineCode("0"); +" for the reader flags because we want to read everything. "
            +"if you dont need certain information like method code or debug info, use one of the static fields on "; inlineCode("ClassReader"); +"."
            br
            +"(you can also have it rewrite the frames or standardize instructions but i personally haven't encountered a use for that)"

            codeBlock(Language.JAVA, embedFile("blog/asm/readAsmClass.txt"))
            codeBlock(Language.NONE, embedFile("blog/asm/readClassResult.txt"))

            +"some important things to note about this:"
            ul {
                li {
                    +"the class version isnt directly the java version (see "
                    _a(
                        "https://docs.oracle.com/javase/specs/jvms/se20/html/jvms-4.html#jvms-4.1-200-B.2",
                        "the class file version table"
                    )
                    +")"
                }
                - "to actually get any data about what a method takes or returns, you need to handle the descriptor"
                li {  +"the \"class name\" is the "; i { +"fully qualified" }; +" name of the class, using slashes to denote the path/package" }
            }
            +"the format of descriptors and class names is important, asm will not verify them for you when modifying or generating classes. "
            +"it becomes very easy, especially with complex class generators, to accidentally use dots instead of slashes, add an extra "
            inlineCode("L"); +" to the start of a class name in a descriptor, and more. "
            +"building your own utility methods is especially useful in this case, to avoid re-creating or hardcoding work."
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
            codeBlock(Language.DIFF, embedFile("blog/asm/Calculator.patch"))
            +"the first change here is pretty simple, we switch the behavior of operations from changing the mode of the calculator to "
            +"only applying once. this means that an input like "; inlineCode("+ 2 3 4"); +" would just give us "; inlineCode("4")
            +" instead of "; inlineCode("9"); +". although it could be argued to be a feature, its "; i { +"technically" }
            +" not desired behavior, so were going to modify that."
            br2
            +"second change is very simple as well, we replace a weird \"attempt-and-catch\" with a proper check. much "
            +"easier to read! (no, java really doesn't have a built-in way of doing this, i checked several times)"
            +"this isnt the fasted method, but we can fix that later."

            br2
            +"so, lets get to this! first off, lets see how asm represents the code of our methods by dumping "; inlineCode("calculateResult"); +"."
            codeBlock(Language.NONE, embedFile("blog/asm/calcResultInsn.txt"))
            +"youll notice this isnt particularly usable at a glance. the only real information is that "; i { +"everything" }
            +" is an "; inlineCode("AbstractInsnNode"); +", which we already knew thanks to \"having a type system\""
            +"labels and line numbers are a notable part of this, "
            +"which is one of the things that asm does actually do for you. labels are the especially useful part here as you dont have to keep "
            +"track of offsets manually when working on the bytecode."
            br
            +"anyways, to actually view the bytecode of a class, youre much better off using "; inlineCode("javap"); +" which is bundled with your JDK. "
            +"so, a quick "; inlineCode("javap -c -p Calculator.class"); +" later and...."
            codeBlock(Language.NONE, embedFile("blog/asm/javap_calc.txt"), 400)

            +"looking at this output, it seems like it makes the most sense to jump right between 138 and 139, so lets target that! "
            +"remember that asm is going to add a lot of extra labels here, so these arent going to line up to exact indices."
            br
            +"its kind of tricky to get an injection point like this, theres a few options here, like using labels to figure out the specific "
            inlineCode("JumpInsnNode"); +" that corresponds to the "; inlineCode("goto"); +" at 139, matching specific patterns of instructions, "
            +"or my choice in this case, just working backwards. we can start at the last instruction and go back until we see a second"
            inlineCode("JumpInsnNode"); +" which will let us know we've reached the right location for our injection."

            codeBlock(Language.JAVA, """
                AbstractInsnNode targetNode = method.instructions.getLast();
                int remaining = 2;
                do {
                    targetNode = targetNode.getPrevious(); // null check snipped
                    if (targetNode instanceof JumpInsnNode) {
                        remaining--;
                    }
                } while (remaining != 0);
                targetNode = targetNode.getPrevious();
            """.trimIndent())
            +"now we just have to inject our instructions. we need 2 instructions here, one "; inlineCode("ldc"); +" to load the "
            +"null byte char, and a "; inlineCode("istore"); +" to actually save it to the variable. (in this case it happens to be "
            inlineCode("istore_2"); +", you can tell because thats the one used before the switch lookup)"
            br
            +"we can inject these instructions by building an "; inlineCode("InsnList"); +" and using "; inlineCode("method.instructions.insertBefore")
            +". "; inlineCode("IntInsnNode"); +" is used for the store instruction to provide it the index to store to."
            codeBlock(Language.JAVA, """
                InsnList firstPatch = new InsnList();
                firstPatch.add(new LdcInsnNode('\0'));
                firstPatch.add(new IntInsnNode(Opcodes.ISTORE, 2));
                method.instructions.insertBefore(targetNode, firstPatch);
            """.trimIndent())

            +"now we can dump the modified class file to disk and use a decompiler ("; _a("https://vineflower.org/", "vineflower"); +" in my case) "
            +"to check if we were successful. lets take the easy way out and tell our "; inlineCode("ClassWriter"); +" to rewrite the frames and max stack "
            +"for us. its not applicable in this case, but its a good general rule, especially when creating classes from scratch. "
            +"this allows us to avoid having to manage all the gunk ourselves, although it can add some overhead. its also the source of several "
            +"of the common but very obtuse errors in asm that will be covered later"
            codeBlock(Language.JAVA, """
                ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
                classNode.accept(writer);

                File outputFile = new File("calculator-modified.class");
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                outputStream.write(writer.toByteArray());
                outputStream.close();
            """.trimIndent())
            codeBlock(Language.JAVA, """
                if (stringIsInteger(part)) {
                    int value = Integer.parseInt(part);
                    switch(currentOperation) {
                        case '\u0000':
                            current = value;
                            break;
                        case '*':
                            current *= value;
                            break;
                        case '+':
                            current += value;
                            break;
                        case '-':
                            current -= value;
                            break;
                        case '/':
                            current /= value;
                    }

                    currentOperation = 0;
                } else {
            """.trimIndent())
            +"other than some decompiler artifacts (turning the null char to integer 0) thats the patch we wanted! and indeed if we run it "
            +"the behavior now matches what it should given the change in logic, a string like "; inlineCode("+ 3 4 5"); +"just gives 5 now."
            br2
            +"this is the basic chain for modifying classes with asm, although somewhat targeted, so the second patch here is pretty easy to deduce."
            codeBlock(Language.JAVA, """
                InsnList secondPatch = new InsnList();
                secondPatch.add(new IntInsnNode(Opcodes.ALOAD, 0));
                secondPatch.add(new LdcInsnNode("-?\\d+"));
                secondPatch.add(
                    new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "java/lang/String", "matches", "(Ljava/lang/String;)Z")
                );
                secondPatch.add(new InsnNode(Opcodes.IRETURN));
                
                method.instructions.clear();
                method.instructions.insert(secondPatch);
            """.trimIndent())
            +"not too bad, just a few more instructions and a method invocation. however, if you try to run this however, it crashes when trying to build the new class file!"
            codeBlock(Language.NONE, "Cannot read field \"outgoingEdges\" because \"handlerRangeBlock\" is null")
            +"this is because you need to clear the try catch blocks as well. if you dont, asm will try and construct the frames "
            +"(like we told it to), try and match it up to the handler blocks, and whoops! that doesnt line up."
            br
            +"luckily, this is super easy to fix by just clearing the try catch blocks as well as the instructions."
            codeBlock(Language.DIFF, """
                 secondPatch.add(new InsnNode(Opcodes.IRETURN));

                +method.tryCatchBlocks.clear();
                 method.instructions.clear();
            """.trimIndent())
            +"and tada, significantly cleaner when we look at the decompiled form. 🎉"
            codeBlock(Language.JAVA, """
                private static boolean stringIsInteger(String str) {
                    return str.matches("-?\\d+");
                }
            """.trimIndent())

            h2 { +"labels, how do they work?" }
            +"so far these patches have been pretty simple, linear instruction sets, but what if we what to include a conditional or "
            +"loop? thats where labels come in (which im sure you'll be familar with if youve done any sort of assembly or even complex algorithms before)"
            br
            +"labels act as placeholders in the code. they provide a way for the absolute offsets of bytecode to be represented in a mutable, dynamic way "
            +"that gets computed at assembly time. since asm inserts them into the instruction list, theyre very easy to modify and use. "
            +"since were working in java, labels work based on just copying references where required, and then inserting the label into the instruction list."
            br2
            +"when setting up a new label, its often best to define all the labels you will need first, and with good names so you dont forget what they are. "
            +"there is no way to tag or identify labels, so its up to you to watch which once youre jumping to."
        }
    }
}